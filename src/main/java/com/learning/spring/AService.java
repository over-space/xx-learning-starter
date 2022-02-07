package com.learning.spring;

import com.learning.spring.entity.AEntity;
import com.learning.spring.entity.BEntity;
import com.learning.spring.repository.ARepository;
import com.learning.spring.repository.BRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @author lifang
 * @since 2022/2/7
 */
@Service
public class AService {

    @Resource
    private ARepository aRepository;
    @Resource
    private BRepository bRepository;


    @Transactional(rollbackFor = Exception.class)
    public void insert(){
        try {
            insertA();
        }catch (Exception e){
            e.printStackTrace();
        }

        BEntity b = new BEntity();
        b.setCreatedDate(LocalDateTime.now());
        b.setName("zhangsan");
        bRepository.save(b);
    }

    private void insertA(){
        AEntity a = new AEntity();
        a.setName("lisi");
        a.setAge(20);
        a.setCreatedDate(LocalDateTime.now());
        aRepository.save(a);
        int i = 10 / 0;
    }

}
