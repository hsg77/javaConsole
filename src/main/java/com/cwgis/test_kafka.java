package com.cwgis;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.*;

public class test_kafka {
    public static String kafkaBrokers="node111:9092,node112:9092,node113:9092";
    public static void main()
            throws Exception
    {
        //参考网页https://www.cnblogs.com/qizhelongdeyang/p/7355309.html
        //kafka server 2.11-2.1.0版本
        //生产者  OK
        //producer();

        //消费者
        Consumer();
    }
    public static void producer()
    {
        Properties properties = new Properties();
        //bootstrap.servers是Kafka集群的IP地址，如果Broker数量超过1个，则使用逗号分隔，
        //9092是所监听的端口

        properties.put("bootstrap.servers", kafkaBrokers);
        properties.put("acks", "all");
        properties.put("retries", 0);
        properties.put("batch.size", 16384);
        properties.put("linger.ms", 1);
        properties.put("buffer.memory", 33554432);
        //序列化类型。 Kafka消息是以键值对的形式发送到Kafka集群的
        //本例是发送文本消息到Kafka集群，所以使用的是StringSerializer
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        Producer<String, String> producer = null;
        try {
            //生产者
            producer = new KafkaProducer<String, String>(properties);
            //发送Message到Kafka集群
            //HelloWorld为topic主题
            for (int i = 0; i < 100; i++) {
                String msg = "Message " + i;
                producer.send(new ProducerRecord<String, String>("HelloWorld", msg));
                System.out.println("Sent:" + msg);
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            producer.close();
        }
    }
    public static void Consumer()
            throws Exception
    {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", kafkaBrokers);  //是指向Kafka集群的IP地址，以逗号分隔
        properties.put("group.id", "group-1");    //Consumer分组ID
        properties.put("enable.auto.commit", "true");
        properties.put("auto.commit.interval.ms", "1000");
        properties.put("auto.offset.reset", "earliest");
        properties.put("session.timeout.ms", "30000");
        //partition.assignment.strategy
        //properties.put("partition.assignment.strategy","RoundRobin");  //RoundRobin   Range
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
        //Consumer订阅了Topic为HelloWorld的消息  Arrays.asList
        kafkaConsumer.subscribe(Arrays.asList("HelloWorld"));
        while (true) {
            ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("offset = %d, value = %s", record.offset(), record.value());
                System.out.println();
            }
        }
        /*  kafka0.8.2.1版本
        java.util.List<String> tops=new ArrayList<>();
        tops.add("HelloWorld");
        kafkaConsumer.subscribe(tops);
        while (true) {
            //Consumer调用poll方法来轮循Kafka集群的消息，其中的参数100是超时时间
            //Consumer等待直到Kafka集群中没有消息为止
            //ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
            Map<String, ConsumerRecords<String,String>> MapRecords = kafkaConsumer.poll(100);
            ConsumerRecords<String, String> ValueRecords=MapRecords.get("HelloWorld");
            for (ConsumerRecord<String, String> record : ValueRecords.records()) {
                System.out.printf("offset = %d, value = %s", record.offset(), record.value());
                System.out.println();
            }
        }*/
    }
}
