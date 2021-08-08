package net.feisbook.rabbitmq.work.fair;

import com.rabbitmq.client.*;

import java.io.IOException;

public class Work1 {
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
            connection = connectionFactory.newConnection("消费者-work1");
            // 3.通过连接获取通道
            channel = connection.createChannel();
            // 4.定义接收消息的回调
            Channel finalChannel = channel;
            // 指定指标，每次从队列中取出的数据量
            finalChannel.basicQos(1);
            // 5.通过创建交换机，声明队列，绑定关系，路由key，发送消息和接收消息
            finalChannel.basicConsume("queue1", false, new DeliverCallback() {
                @Override
                public void handle(String s, Delivery delivery) throws IOException {
//                    System.out.println(delivery.getEnvelope().getDeliveryTag());
                    System.out.println("work1接受的消息是：" + new String(delivery.getBody(), "UTF-8"));
                    try {
                        Thread.sleep(1000);
                        // 改为手动应答
                        finalChannel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, new CancelCallback() {
                @Override
                public void handle(String s) throws IOException {
                    System.out.println("接收消息失败");
                }
            });

            System.out.println("开始接受消息");
            System.in.read();


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
