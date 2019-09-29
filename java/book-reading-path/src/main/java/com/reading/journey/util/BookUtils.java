package com.reading.journey.util;

import com.reading.journey.domain.Book;

import java.util.List;

public class BookUtils {

    private BookUtils() {}

    public static long numberOfRead(final List<? extends Book> books) {
        return books.stream().filter(Book::isRead).count();
    }

}
