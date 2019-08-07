package com.github.mogikanen9.devtest.pubsub;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.github.mogikanen9.devtest.domain.Book;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BookQueue {

    private String name;

    private BlockingQueue<Book> bookQueue;

    // private Semaphore holder;

    public BookQueue(String name, int processCapacity) {
        // holder = new Semaphore(processCapacity);
        this.bookQueue = new ArrayBlockingQueue<>(processCapacity);
        this.name =  String.format("QUEUE-%d-%s",System.currentTimeMillis(), name);
    }

    public void push(Book book) throws InterruptedException {
        if (log.isDebugEnabled()) {
            log.debug(String.format("Adding book with ISBN=%s to shared buffer/queue->%s", book.getIsbn(), this.name));
        }
        // holder.acquire();
        bookQueue.put(book);
    }

    public Book pull() throws InterruptedException {
        // try {
        Book book = bookQueue.take();
        if (log.isDebugEnabled()) {
            log.debug(String.format("Retrivieng book with ISBN=%s from shared buffer/queue->%", book.getIsbn(),
                    this.name));
        }
        return book;
        // } finally {
        // holder.release();
        // }
    }

    public Book getEOQMarker() {
        return new Book(this.name);
    }

    public boolean isEOQMarker(Book book){
        return book.getIsbn().equalsIgnoreCase(this.name);
    }
}