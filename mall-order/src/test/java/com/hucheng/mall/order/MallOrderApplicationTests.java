package com.hucheng.mall.order;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MallOrderApplicationTests {

    @Autowired
    AmqpAdmin amqpAdmin;
    /**
     * 创建Exchange
     */
    @Test
    public void createExchange() {
        DirectExchange directExchange = new DirectExchange("java-exchange", true, false);
        amqpAdmin.declareExchange(directExchange);
    }

    /**
     * 创建Queue
     */
    @Test
    public void createQueue() {
        Queue queue = new Queue("java-queue", true, false, false);
        amqpAdmin.declareQueue(queue);
    }

    /**
     * 创建Binding
     */
    @Test
    public void createBinding() {

        /**
         * destination：目的地
         * destinationType：目的地类型
         * exchange：交换机
         * routingKey：路由键
         * arguments：自定义参数
         */
        Binding binding = new Binding("java-queue", Binding.DestinationType.QUEUE,
                "java-exchange", "my-route-key", null);
        amqpAdmin.declareBinding(binding);
    }


}
