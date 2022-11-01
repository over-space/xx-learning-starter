package com.learning.middleware.elasticsearch;

import com.learning.middleware.elasticsearch.domain.BookES;
import com.learning.middleware.elasticsearch.service.BookServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.SearchHit;

import java.util.Arrays;
import java.util.List;

/**
 * @author lifang
 * @since 2021/11/18
 */
@SpringBootTest
public class ElasticsearchBookTest {

    private static final Logger logger = LogManager.getLogger(ElasticsearchBookTest.class);


    @Autowired
    private BookServiceImpl bookService;

    @Test
    void contextLoads() {

    }

    @Test
    void test01() {
        {
            BookES book = new BookES();
            book.setId(1);
            book.setName("Spring data elasticsearch");
            book.setTags(Arrays.asList("java", "spring", "bigdata"));
            book.setContent("Copies of this document may be made for your own use and for distribution to others, provided that you do not charge any fee for such copies and further provided that each copy contains this Copyright Notice, whether distributed in print or electronically.");
            bookService.insert(book);
        }

        {
            BookES book = new BookES();
            book.setId(2);
            book.setName("Spring data jps");
            book.setTags(Arrays.asList("java", "spring"));
            book.setContent("The goal of the Spring Data repository abstraction is to significantly reduce the amount of boilerplate code required to implement data access layers for various persistence stores");
            bookService.insert(book);
        }

        {
            BookES book = new BookES();
            book.setId(3);
            book.setName("Hadoop & Hive & Hbase");
            book.setTags(Arrays.asList("java", "bigdata"));
            book.setContent("The central interface in the Spring Data repository abstraction is Repository. It takes the domain class to manage as well as the ID type of the domain class as type arguments. This interface acts primarily as a marker interface to capture the types to work with and to help you to discover interfaces that extend this one. The CrudRepository interface provides sophisticated CRUD functionality for the entity class that is being managed");
            bookService.insert(book);
        }
    }

    @Test
    void test02() {
        List<SearchHit<BookES>> searchHitList = bookService.findByContent("spring data");
        for (SearchHit<BookES> searchHit : searchHitList) {
            BookES book = searchHit.getContent();
            logger.info("book : {}", book);
        }
    }
}
