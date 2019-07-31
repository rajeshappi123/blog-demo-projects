package com.github.mogikanen9.devtest.writer;

import com.github.mogikanen9.devtest.domain.Book;

public interface Writer {

    void write(Book book) throws WriterException;
}