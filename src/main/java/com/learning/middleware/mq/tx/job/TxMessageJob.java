package com.learning.middleware.mq.tx.job;

import com.alibaba.fastjson.JSONObject;
import com.learning.middleware.mq.tx.entity.MsgRecordEntity;
import com.learning.middleware.mq.tx.service.MsgRecordService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * 消息补偿
 * @author 李芳
 * @since 2022/9/19
 */
@Component
public class TxMessageJob {

    private static final Logger logger = LogManager.getLogger(TxMessageJob.class);

    @Resource
    private MsgRecordService msgRecordService;
    // @Resource
    // private MessageProducer messageProducer;

    @Scheduled(cron = "0/15 * * * * ?")
    public void run(){
        // 消息补偿，15秒执行一次
        logger.info("开始-定时执行事务消息补偿......");

        // 只补偿30秒未发送的数据，避免重复。
        LocalTime localTime = LocalTime.now().plusSeconds(-30);
        LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now(), localTime);
        List<MsgRecordEntity> msgRecordEntities = msgRecordService.findByMsgStatus(MsgRecordEntity.MsgSendStatus.UNSENT, localDateTime);

        if(CollectionUtils.isEmpty(msgRecordEntities)) {
            logger.info("无消息需要补偿。");
            return;
        };

        for (MsgRecordEntity msgRecordEntity : msgRecordEntities) {
            // messageProducer.send(msgRecordEntity);
            logger.info("需要补偿的消息记录，msg:{}", JSONObject.toJSONString(msgRecordEntity));
        }

        logger.info("本次定时执行事务消息补偿结束，补偿{}条消息。", msgRecordEntities.size());
    }

}
