package net.feisbook.rabbitmq.all;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;


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
            // 4.准备消息内容
            String message = "Hello all F";
            // 5.准备交换机
            String exchangeName = "direct-message-exchange";
            // 6.定义路由Key
            String routingKey = "";
            // 7.指定交换机类型
            String type = "direct";
            // 8.声明交换机
            channel.exchangeDeclare(exchangeName, type, true);
            // 9.声明队列
            channel.queueDeclare("queue5", true, false, false, null);
            channel.queueDeclare("queue6", true, false, false, null);
            channel.queueDeclare("queue7", true, false, false, null);
            // 10.绑定队列和交换机的关系
            channel.queueBind("queue5", exchangeName, "order");
            channel.queueBind("queue6", exchangeName, "order");
            channel.queueBind("queue7", exchangeName, "course");
            // 11.发送消息给中间件rabbitmq-server
            /*
             * param1: 交换机
             * param2: 队列，路由key
             * param3: 属性配置
             * param4: 消息主体
             */
            channel.basicPublish(exchangeName, "order", null, message.getBytes());

            System.out.println("消息发送成功！");


        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("发送消息出现异常");
        } finally {
            // 12.关闭通道
            if (channel != null && channel.isOpen()) {
                try {
                    channel.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // 13.关闭连接
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
