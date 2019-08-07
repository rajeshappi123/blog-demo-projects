package com.github.mogikanen9.devtest.domain;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Book implements Serializable{

    private static final long serialVersionUID = 1L;

    private String isbn;

    private String title;

    private int publicationYear;

    private String langCode;

    private String authors;

    private float avgRating;

    private String imgUrl;

    public Book(String isbn){
        this.isbn = isbn;
    }

}