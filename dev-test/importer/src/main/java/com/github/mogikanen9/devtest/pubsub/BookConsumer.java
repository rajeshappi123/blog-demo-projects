package com.github.mogikanen9.devtest.pubsub;

import com.github.mogikanen9.devtest.domain.Book;
import com.github.mogikanen9.devtest.writer.Writer;
import com.github.mogikanen9.devtest.writer.WriterException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class BookConsumer implements Runnable {

    private BookQueue bookQueue;

    private Writer writer;

    @Override
    public void run() {

        boolean isEndOfQueue = false;
        while (!isEndOfQueue) {
            try {
                Book book = bookQueue.pull();

                try {
                    writer.write(book);
                } catch (WriterException e) {
                    log.error(e.getMessage(), e);
                } finally {
                    //Thread.sleep(20);
                    isEndOfQueue = bookQueue.isEOQMarker(book);
                }

            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
                break;
            }

        }
    }

}