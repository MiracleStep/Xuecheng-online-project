package com.xuecheng.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.xuecheng.test.rabbitmq.Config;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RabbitTestApplication {
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    public void testSendByTopics(){
        for(int i=0;i<5;i++){
            String message = "sms email inform to user"+i;
            rabbitTemplate.convertAndSend(Config.EXCHANGE_TOPICS_INFORM,"inform.email",message);
        }
    }
}
