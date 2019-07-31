package com.github.mogikanen9.devtest.writer;

import java.time.LocalDateTime;

import com.github.mogikanen9.devtest.domain.Book;

import lombok.Data;

@Data
public class RestBook {
   
    private long id;

    private String created; 

    private String createdBy;

    private String updated; 

    private String updatedBy;

    private String isbn;

    private String title;

    private int publicationYear;

    private String langCode;

    private String authors;

    private float avgRating;

    private String imgUrl;

    public static RestBook from(Book book){
        RestBook restBook = new RestBook();
        restBook.setAuthors(book.getAuthors());
        restBook.setAvgRating(book.getAvgRating());
        restBook.setCreated(LocalDateTime.now().toString());
        //restBook.setCreated("2019-07-19T10:30:29");
        restBook.setCreatedBy("book-importer");
        restBook.setImgUrl(book.getImgUrl());
        restBook.setIsbn(book.getIsbn());
        restBook.setLangCode(book.getLangCode());
        restBook.setPublicationYear(book.getPublicationYear());
        restBook.setTitle(book.getTitle());
        restBook.setUpdated(restBook.getCreated());
        restBook.setUpdatedBy(restBook.getCreatedBy());
        return restBook;
    }
}