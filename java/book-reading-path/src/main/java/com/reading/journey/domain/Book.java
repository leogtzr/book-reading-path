package com.reading.journey.domain;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Book {

    private String id;
    private String title;
    private String author;
    private boolean read;

    public Book(final String id, final String title, final String author, final boolean read) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.read = read;
    }

    public Book() {
    }

    public String toHTML() {
        if (this.read) {
            return String.format("<li class=\"read\">(%s) \"%s\" by %s</li>", this.id, this.title, this.author);
        }
        return String.format("<li class=\"noread\">(%s) \"%s\" by %s</li>", this.id, this.title, this.author);
    }
}
