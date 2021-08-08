package net.feisbook.rabbitmq.work.polling;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;


public class Producer {
    public static void main(String[] args) {
        // 1.创建连接工程
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("120.53.231.158");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        connectionFactory.setVirtualHost("/");
        Connection connection = null;
        Channel channel = null;
        try {
            // 2.创建连接connection
            connection = connectionFactory.newConnection("生成者");
            // 3.通过连接获取通道
            channel = connection.createChannel();
            // 4.准备发送消息的内容
            for(int i=0; i<20; i++) {
                String msg = "HF" + i;
                // 5.发送消息给中间件rabbitmq-server
                /*
                 * param1: 交换机
                 * param2: 队列，路由key
                 * param3: 属性配置
                 * param4: 消息主体
                 */
                channel.basicPublish("", "queue1", null, msg.getBytes(StandardCharsets.UTF_8));
            }
            System.out.println("消息发送成功！");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 7.关闭通道
            if (channel != null && channel.isOpen()) {
                try {
                    channel.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // 8.关闭连接
            if (connection != null && connection.isOpen()) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
