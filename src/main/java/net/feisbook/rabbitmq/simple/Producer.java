package net.feisbook.rabbitmq.simple;

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
            // 4.通过创建交换机，声明队列，绑定关系，路由key，发送消息和接收消息
            String queueName = "queue1";

            /*
             * @param1: 队列的名称
             * @param2: 队列是否要持久化durable=false，非持久化队列也会存盘，但服务重启会丢失
             * @param3: 排他性，是否是独占独立队列
             * @param4: 是否自动删除，随着最后一个消费者消息完毕后是否删除队列
             * @param5: 携带附加参数
             */
            channel.queueDeclare(queueName, false, false, false, null);

            // 5.准备消息内容
            String message = "Hello F";
            // 6.发送消息给队列
            /*
             * param1: 交换机
             * param2: 队列，路由key
             * param3: 消息状态控制
             * param4: 消息主体
             */
            channel.basicPublish("", queueName, null, message.getBytes());

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
