package net.feisbook.rabbitmq.routing;

import com.rabbitmq.client.*;

import java.io.IOException;

public class Consumer {
    private static Runnable runnable = new Runnable() {
        public void run() {
            // 1.创建连接工厂
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost("120.53.231.158");
            connectionFactory.setPort(5672);
            connectionFactory.setUsername("admin");
            connectionFactory.setPassword("admin");
            connectionFactory.setVirtualHost("/");
            final String queueName = Thread.currentThread().getName();
            Connection connection = null;
            Channel channel = null;
            try {
                // 2.创建连接connection
                connection = connectionFactory.newConnection("消费者");
                // 3.通过连接获取通道
                channel = connection.createChannel();
                // 4.定义接收消息的回调
                Channel finalChannel = channel;
                // 4.通过创建交换机，声明队列，绑定关系，路由key，发送消息和接收消息
                finalChannel.basicConsume(queueName, true, new DeliverCallback() {
                    @Override
                    public void handle(String s, Delivery delivery) throws IOException {
                        System.out.println(delivery.getEnvelope().getDeliveryTag());
                        System.out.println("接受的消息是：" + new String(delivery.getBody(), "UTF-8"));
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
    };
    public static void main(String[] args) {
        // 启动三个线程执行
        // 队列已经在rabbitmq图形界面中绑定过了，就不在代码中绑定了
        new Thread(runnable, "queue1").start();
        new Thread(runnable, "queue2").start();
        new Thread(runnable, "queue3").start();
    }
}
