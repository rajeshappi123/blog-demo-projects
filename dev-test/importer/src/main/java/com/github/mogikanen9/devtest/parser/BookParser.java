package com.github.mogikanen9.devtest.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import com.github.mogikanen9.devtest.domain.Book;
import com.github.mogikanen9.devtest.pubsub.BookQueue;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class BookParser {

    private Path sourceFile;
    private String delimeter;
    private String quoteSymbol;
    private String doubleQuoteSymbol;
    private String doubleQuoteReplacement;
    private boolean skipFirstLine;
    private BookQueue bookQueue;

    public BookResult parse() {
        long initTime = System.currentTimeMillis();
        log.info(String.format("Parsing sourceFile->%s", sourceFile.toString()));
        int numberOfLines = 0;
        long parsedTime = 0;
        ParserException ex = null;

        try (BufferedReader reader = Files.newBufferedReader(sourceFile, Charset.forName("UTF-8"))) {
            String line = null;
            boolean skipLine = true;

            while ((line = reader.readLine()) != null) {

                // skip header
                if (skipFirstLine && skipLine) {
                    skipLine = false;
                    continue;
                }

                Book book = this.parse(this.replaceDoubleQuoteSymbol(line, doubleQuoteSymbol, doubleQuoteReplacement),
                        this.quoteSymbol, this.delimeter);
                if (log.isDebugEnabled()) {
                    log.debug(String.format("book->%s", book));
                }

                bookQueue.push(book);

                numberOfLines++;
            }           

            Path renamedFile = this.rename(sourceFile,".parsed");
            log.info(String.format("SourceFile->%s was successfully renamed to %s.", sourceFile.getFileName(), renamedFile.getFileName()));           

            parsedTime = System.currentTimeMillis() - initTime;
            log.info(String.format("SourceFile->%s was successfully parsed in %dms.", sourceFile.toString(), parsedTime));        

            bookQueue.push(bookQueue.getEOQMarker());

        } catch (Exception x) {
            log.error(x.getMessage(), x);
            ex = new ParserException(x.getMessage(), x);
            //throw new ParserException(x.getMessage(), x);
        }
        
        return new BookResult(sourceFile,numberOfLines, parsedTime, ex);

    }

    protected Book parse(String line, String quoteSymbol, String delimeterSymbol) throws ParserException {
        if (log.isDebugEnabled()) {
            log.debug(String.format("Parsing line -> %s", line));
        }

        // String[] columns = line.split(delimeter);
        String[] columns = new String[23];
        columns = this.customSplit(line, quoteSymbol, delimeterSymbol).toArray(columns);

        if (columns.length < 10) {
            throw new ParserException("Invalid number of columns");
        } else {
            String isbn = null;
            String title = null;
            int publicationYear = 0;
            String authors = null;
            String langCode = null;
            float avgRating = 0.0f;
            String imgUrl = null;

            isbn = this.stripQuoteSymbol(columns[5], quoteSymbol);
            title = this.stripQuoteSymbol(columns[9], quoteSymbol);
            publicationYear = (int) this.readFloatValue(this.stripQuoteSymbol(columns[8], quoteSymbol));
            authors = this.stripQuoteSymbol(columns[7], quoteSymbol);
            langCode = this.stripQuoteSymbol(columns[11], quoteSymbol);
            avgRating = this.readFloatValue(this.stripQuoteSymbol(columns[12], quoteSymbol));
            imgUrl = this.stripQuoteSymbol(columns[21], quoteSymbol);

            return new Book(isbn, title, publicationYear, langCode, authors, avgRating, imgUrl);
        }

    }

    protected String stripQuoteSymbol(String value, String quoteSymbol) {

        if (value != null && quoteSymbol != null && value.startsWith(quoteSymbol) && value.endsWith(quoteSymbol)) {
            return value.substring(1, value.length() - 1).trim();
        } else {
            return value!=null?value.trim():value;
        }
    }

    protected float readFloatValue(String value) {
        if (value != null && value.length() > 0) {
            return Float.parseFloat(value);
        } else {
            return 0;
        }
    }

    protected String replaceDoubleQuoteSymbol(String line, String doubleQuoteSymbol, String replacement) {
        return line.replaceAll(doubleQuoteSymbol, replacement);
    }

    protected List<String> customSplit(String line, String quoteSymbol, String delimeterSymbol) throws ParserException {

        List<String> result = new ArrayList<>();
        boolean moveNext = true;
        int currentIndex = 0;
        int startQuoteIndex = 0;
        int endQuoteIndex = 0;

        while (moveNext) {

            int separatorIndex = line.indexOf(delimeterSymbol, currentIndex);

            startQuoteIndex = line.indexOf(quoteSymbol, endQuoteIndex == 0 ? endQuoteIndex : endQuoteIndex + 1);

            if (separatorIndex < line.length() - 1) { // not the last symbol
                if (separatorIndex < 0) {
                    result.add(line.substring(currentIndex)); // last column
                } else if (startQuoteIndex < 0 || separatorIndex < startQuoteIndex) {
                    result.add(line.substring(currentIndex, separatorIndex));
                    currentIndex = separatorIndex + 1;
                } else {
                    // find index fo the end quote
                    endQuoteIndex = line.indexOf(quoteSymbol, startQuoteIndex + 1);
                    if (endQuoteIndex > startQuoteIndex && endQuoteIndex != -1 && startQuoteIndex != -1) {
                        result.add(line.substring(startQuoteIndex + 1, endQuoteIndex));
                        currentIndex = endQuoteIndex + 2; // assume that delimeter follows quote symbol
                        endQuoteIndex++;
                    } else {
                        throw new ParserException("Cannot find second quotation symbol from from pair!");
                    }
                }
            }

            if (currentIndex < 0 || currentIndex >= line.length() - 1 || separatorIndex < 0) {
                moveNext = false;
            }

        }

        return result;
    }

    protected Path rename(Path source, String extension) throws IOException {
        Path target = Paths.get(source.getParent().toFile().getAbsolutePath(), "/".concat(source.getFileName().toString()).concat(extension));
        Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
        return target;
    }
}