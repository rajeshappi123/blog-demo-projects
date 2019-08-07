package com.github.mogikanen9.devtest;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import com.github.mogikanen9.devtest.parser.BookParser;
import com.github.mogikanen9.devtest.parser.BookResult;
import com.github.mogikanen9.devtest.parser.ParserException;
import com.github.mogikanen9.devtest.pubsub.BookConsumer;
import com.github.mogikanen9.devtest.pubsub.BookQueue;
import com.github.mogikanen9.devtest.scanner.FileScanner;
import com.github.mogikanen9.devtest.writer.RestWriter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {

    public static void main(String[] args) {

        log.info("App main...");

        final String delimeterSymbol = ",";
        final String quoteSymbol = "\"";
        final String doubleQuoteSymbol = "\"\"";
        final String doubleQuoteReplacement = "'";
        final String apiBaseUrl = "http://localhost:8080";
        final String apiUname = "importer";
        final String apiPwd = "Welcome13";

        int producers = 2;        
        int ratio = 4;
        int consumers = producers*ratio;
        int capacity = 10;

        ExecutorService bookConsumerExecutor = Executors.newFixedThreadPool(consumers);       
        ExecutorService bookProducerExecutor = Executors.newFixedThreadPool(producers);

        Function<Path, BookResult> bookFileParser = (path) -> {
            try {

                BookQueue buffer = new BookQueue(path.toFile().getName() ,capacity);

                //create producer
                Future<BookResult> bookParser = bookProducerExecutor.submit(() -> {
                    return new BookParser(path, delimeterSymbol, quoteSymbol, doubleQuoteSymbol, doubleQuoteReplacement,
                            true, buffer).parse();
                });

                //create and start consumers based on prod-cons ratio
                for(int i=0;i<ratio;i++){
                    bookConsumerExecutor.execute(new BookConsumer(buffer,  new RestWriter(apiBaseUrl, apiUname, apiPwd)));
                }
               
                //start producer
                return bookParser.get();

            } catch (InterruptedException | ExecutionException e) {
                log.error(e.getMessage(), e);
                return new BookResult(path, 0, 0, new ParserException(e.getMessage()));
            }

        };

        Runnable bookImporter = () -> {
            log.info("Importing books...");
            new FileScanner(Paths.get("c://temp"), 10).listAll().stream().map(bookFileParser).forEach((rs) -> {
                log.info(String.format("Parsed result->%s",rs.toString()));
            });
        };

        ScheduledExecutorService srcScanner = Executors.newSingleThreadScheduledExecutor();
        srcScanner.scheduleWithFixedDelay(bookImporter, 10, 100, TimeUnit.SECONDS);

    }

}
