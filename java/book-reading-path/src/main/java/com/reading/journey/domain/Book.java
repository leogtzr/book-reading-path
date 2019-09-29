package com.reading.journey.domain;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Book {

    private String id;
    private String title;
    private String author;

    public Book(String id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }

    public Book() {
    }
}
