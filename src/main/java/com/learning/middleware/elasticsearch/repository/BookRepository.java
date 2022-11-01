package com.learning.middleware.elasticsearch.repository;

import com.learning.middleware.elasticsearch.domain.BookES;
import org.springframework.data.elasticsearch.annotations.Highlight;
import org.springframework.data.elasticsearch.annotations.HighlightField;
import org.springframework.data.elasticsearch.annotations.HighlightParameters;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author lifang
 * @since 2021/11/18
 */
@Repository
public interface BookRepository extends ElasticsearchRepository<BookES, Integer> {

    @Highlight(
            fields = @HighlightField(name = "content"),
            parameters = @HighlightParameters(preTags = "<font color=\"red\">", postTags = "</font>")
    )
    List<SearchHit<BookES>> findByContent(String content);

}
