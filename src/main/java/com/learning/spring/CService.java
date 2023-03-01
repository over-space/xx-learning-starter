package com.learning.spring;

import com.learning.spring.entity.CEntity;
import com.learning.spring.repository.CRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author lifang
 * @since 2022/3/8
 */
@Service
public class CService {

    private static final Logger logger = LogManager.getLogger(CService.class);


    @Resource
    private CRepository cRepository;

    @Transactional(rollbackFor = Exception.class)
    public void insert(String lockKey) {
        CEntity c = new CEntity();
        c.setGmtModified(LocalDateTime.now());
        c.setGmtCreate(LocalDateTime.now());
        c.setLockBiz("accountUser");
        c.setLockKey(lockKey);

        logger.info("cEntity: {}", c);
        cRepository.save(c);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(Long id) {

        CEntity c = cRepository.findById(id).orElse(null);

        if (c == null) return;

        Integer count = Optional.ofNullable(c.getModifiedCount()).orElse(0);

        c.setGmtModified(LocalDateTime.now());
        c.setLockBiz("updateAccountUser");
        c.setModifiedCount(++count);
        cRepository.save(c);
    }
}
