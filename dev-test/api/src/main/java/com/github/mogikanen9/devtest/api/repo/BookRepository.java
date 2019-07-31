package com.github.mogikanen9.devtest.api.repo;

import java.util.List;

import com.github.mogikanen9.devtest.api.model.Book;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "book", path = "book")
public interface BookRepository extends PagingAndSortingRepository<Book, Long> {

    List<Book> findByIsbn(@Param("isbn") String name);
}