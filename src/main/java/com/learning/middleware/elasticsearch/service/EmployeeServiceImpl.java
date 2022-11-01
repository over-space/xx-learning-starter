package com.learning.middleware.elasticsearch.service;

import com.learning.middleware.elasticsearch.domain.EmployeeES;
import com.learning.middleware.elasticsearch.repository.EmployeeRepository;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl {

    @Autowired(required = false)
    private EmployeeRepository employeeRepository;
    @Autowired(required = false)
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    public void save(EmployeeES employee) {
        employeeRepository.save(employee);
    }

    public void batchSave(List<EmployeeES> employeeList) {
        if (employeeList == null || employeeList.isEmpty()) return;
        employeeRepository.saveAll(employeeList);
    }


    public Page<EmployeeES> page(int page, int pageSize) {
        PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "id"));
        return employeeRepository.findAll(pageRequest);
    }

    public Page<EmployeeES> findByTags(String tags) {
        EmployeeES employeeES = new EmployeeES();
        employeeES.setId(3L);

        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
        return employeeRepository.searchSimilar(employeeES, new String[]{"tags"}, pageRequest);
    }

    public SearchHits<EmployeeES> matchQuery(String field, Object value){
        MatchQueryBuilder builder = QueryBuilders.matchQuery(field, value);
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(builder);
        return elasticsearchRestTemplate.search(nativeSearchQuery, EmployeeES.class);
    }


    public SearchHits<EmployeeES> matchQuery(String field, Object value, Pageable pageable){
        MatchQueryBuilder builder = QueryBuilders.matchQuery(field, value);
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(builder).setPageable(pageable);
        return elasticsearchRestTemplate.search(nativeSearchQuery, EmployeeES.class);
    }

    public EmployeeES getById(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }
}
