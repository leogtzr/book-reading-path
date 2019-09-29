package com.reading.journey.batch;

import com.reading.journey.domain.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class BookReadingProcessor implements ItemProcessor<Book, Book> {
    @Override
    public Book process(final Book book) throws Exception {
        return book;
    }
}
