package com.learning;

import com.learning.mq.tx.job.TxMessageJob;
import org.apache.shardingsphere.spring.boot.ShardingSphereAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;

/**
 * @author 李芳
 * @since 2022/10/21
 */
@SpringBootApplication(exclude = {
        ShardingSphereAutoConfiguration.class,
        KafkaAutoConfiguration.class
})
@ComponentScan(excludeFilters = {
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.learning.mq.*.*"),
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {TxMessageJob.class})
})
public class BaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(XXLearningBigdataApplication.class, args);
    }

    static class BaseFilter implements TypeFilter {
        @Override
        public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
            //获取当前类注解的信息
            AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
            //获取当前正在扫描的类的类信息
            ClassMetadata classMetadata = metadataReader.getClassMetadata();
            //获取当前类资源（类的路径）
            Resource resource = metadataReader.getResource();
            String className = classMetadata.getClassName();
            if (className.contains("controller")) {
                return true;
            }
            return false;
        }
    }
}
