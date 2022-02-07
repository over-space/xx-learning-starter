package com.learning.elasticsearch.repository;

import com.learning.elasticsearch.domain.EmployeeES;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends ElasticsearchRepository<EmployeeES, Long> {


}
