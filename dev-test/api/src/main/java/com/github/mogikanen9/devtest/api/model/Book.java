package com.github.mogikanen9.devtest.api.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "BOOK")
@Getter
@Setter
@EqualsAndHashCode
public class Book{

    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "created", columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime created; 

    @Column(name = "created_by",length = 50, nullable = false)
    private String createdBy;

    @Column(name = "updated", columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime updated; 

    @Column(name = "updated_by",length = 50, nullable = false)
    private String updatedBy;

    @Column(name = "isbn",length = 10, nullable = false, unique = false)
    private String isbn;

    @Column(name = "title",length = 255, nullable = false)
    private String title;

    @Column(name = "pub_year", nullable = false)
    private int publicationYear;

    @Column(name = "lang_code", length = 10)
    private String langCode;

    @Column(name = "authors",length = 1000, nullable = false)
    private String authors;

    @Column(name = "avg_rating")
    private float avgRating;

    @Column(name = "img_url",length = 2048)
    private String imgUrl;
}