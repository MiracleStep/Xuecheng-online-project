package com.xuecheng.test.rabbitmq.mq;

import com.rabbitmq.client.Channel;
import com.xuecheng.test.rabbitmq.config.Config;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;



@Component
public class ReceiveHandler {

    @RabbitListener(queues = {Config.QUEUE_INFORM_EMAIL})
    public void send_email(String msg, Channel channel){
        System.out.println("receive message is:"+msg);
        System.out.println(channel);
    }
}
