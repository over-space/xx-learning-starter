package com.learning.middleware.elasticsearch.service;

import com.learning.middleware.elasticsearch.domain.BookES;
import com.learning.middleware.elasticsearch.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lifang
 * @since 2021/11/18
 */
@Service
public class BookServiceImpl {

    @Autowired(required = false)
    private BookRepository bookRepository;

    public void insert(BookES book) {
        bookRepository.save(book);
    }

    public List<SearchHit<BookES>> findByContent(String content) {
        return bookRepository.findByContent(content);
    }
}
