package com.learning.elasticsearch;

import com.learning.elasticsearch.domain.EmployeeES;
import com.learning.elasticsearch.service.EmployeeServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author lifang
 * @since 2021/11/17
 */
@SpringBootTest
public class ElasticsearchEmployeeTest {

    private static final Logger logger = LogManager.getLogger(ElasticsearchEmployeeTest.class);


    @Autowired
    private EmployeeServiceImpl employeeService;

    @Test
    void contextLoads() {

    }

    @Test
    void testSave(){
        EmployeeES employee1 = new EmployeeES();
        employee1.setId(1L);
        employee1.setName("lisi");
        employee1.setAge(20);
        employeeService.save(employee1);

        EmployeeES employee2 = new EmployeeES();
        employee2.setId(2L);
        employee2.setName("zhangsan");
        employee2.setAge(20);
        employeeService.save(employee2);

        EmployeeES employee3 = new EmployeeES();
        employee3.setId(3L);
        employee3.setName("wangwu");
        employee3.setAge(20);
        employee3.setTags(Arrays.asList("boy", "sun", "simple", "hello world"));
        employeeService.save(employee3);
    }

    @Test
    void testFindByTags(){
        Page<EmployeeES> page = employeeService.findByTags("sun");
        logger.info("page: {}, pages:{}, total:{}", page, page.getTotalPages(), page.getTotalElements());
    }

    @Test
    void testBatchSave(){
        Random random = new Random();

        List<EmployeeES> employeeList = new ArrayList<>(100);
        for (int i = 100; i < 10000; i++) {
            EmployeeES employee = new EmployeeES();
            employee.setId((long) i);
            employee.setName("lisi" + i);
            employee.setAge(random.nextInt(80));
            employeeList.add(employee);

            if(employeeList.size() >= 100){
                employeeService.batchSave(employeeList);
                employeeList.clear();
            }
        }
        employeeService.batchSave(employeeList);
    }

    @Test
    void testPage(){
        for (int i = 0; i < 10; i++) {
            Page<EmployeeES> page = employeeService.page(i, 100);
            logger.info("page: {}, pages:{}, total:{}", page, page.getTotalPages(), page.getTotalElements());
            for (EmployeeES employeeES : page.getContent()) {
                logger.info("employeeES ：{}", employeeES);
            }
        }
    }

    @Test
    public void testMatchQuery(){

        {
            SearchHits<EmployeeES> searchHits = employeeService.matchQuery("name", "wangwu");
            logger.info("{}", searchHits);
            List<EmployeeES> employeeESList = searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());

            for (EmployeeES employeeES : employeeESList) {
                logger.info("employeeES ：{}", employeeES);
            }
        }

        {
            SearchHits<EmployeeES> searchHits = employeeService.matchQuery("tags", "hello world");
            logger.info("{}", searchHits);
            List<EmployeeES> employeeESList = searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());

            for (EmployeeES employeeES : employeeESList) {
                logger.info("employeeES ：{}", employeeES);
            }
        }

        {
            SearchHits<EmployeeES> searchHits = employeeService.matchQuery("age", 40);
            logger.info("{}", searchHits);
            List<EmployeeES> employeeESList = searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());

            for (EmployeeES employeeES : employeeESList) {
                logger.info("employeeES ：{}", employeeES);
            }
        }
    }

    @Test
    void testGetById(){
        EmployeeES employeeES = employeeService.getById(1L);
        Assertions.assertNotNull(employeeES);
        logger.info("employeeES ：{}", employeeES);
    }
}
