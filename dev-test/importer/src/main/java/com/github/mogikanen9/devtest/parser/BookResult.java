package com.github.mogikanen9.devtest.parser;

import java.nio.file.Path;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class BookResult {

    private Path sourceFile;
    private int numberOfLines;
    private long parsedTime;
    private ParserException exception;

}