package com.example.springwiki.rocketmq;

import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author jessica~
 * @version 1.0
 */
@Service
@RocketMQMessageListener(consumerGroup = "default", topic = "VOTE_TOPIC")
public class VoteConsume implements RocketMQListener<MessageExt> {
    private static final Logger LOG = LoggerFactory.getLogger(VoteConsume.class);


    @Override
    public void onMessage(MessageExt messageExt) {
        byte[] body = messageExt.getBody();
        LOG.info("ROCKETMQ 收到消息:{}", new String(body));
    }
}
