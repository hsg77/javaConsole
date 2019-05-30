package com.cwgis;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.*;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
        //Consumer();
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

    //生产者线程
    public class ProducerThread implements Runnable {
        private final Producer<String,String> kafkaProducer;
        private final String topic;

        public ProducerThread(String brokers,String topic){
            Properties properties = buildKafkaProperty(brokers);
            this.topic = topic;
            this.kafkaProducer = new KafkaProducer<String,String>(properties);

        }

        private  Properties buildKafkaProperty(String brokers){
            Properties properties = new Properties();
            properties.put("bootstrap.servers", brokers);
            properties.put("acks", "all");
            properties.put("retries", 0);
            properties.put("batch.size", 16384);
            properties.put("linger.ms", 1);
            properties.put("buffer.memory", 33554432);
            properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            return properties;
        }

        @Override
        public void run() {
            System.out.println("start sending message to kafka");
            int i = 0;
            while (true){
                String sendMsg = "Producer message number:"+String.valueOf(++i);
                kafkaProducer.send(new ProducerRecord<String, String>(topic,sendMsg),new Callback(){

                    @Override
                    public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                        if(e != null){
                            e.printStackTrace();
                        }
                        System.out.println("Producer Message: Partition:"+recordMetadata.partition()+",Offset:"+recordMetadata.offset());
                    }
                });
                // thread sleep 3 seconds every time
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("end sending message to kafka");
            }
        }
    }

    //消费者线程
    public class ConsumerThread implements Runnable {
        private  KafkaConsumer<String,String> kafkaConsumer;
        private final String topic;

        public ConsumerThread(String brokers,String groupId,String topic){
            Properties properties = buildKafkaProperty(brokers,groupId);
            this.topic = topic;
            this.kafkaConsumer = new KafkaConsumer<String, String>(properties);
            this.kafkaConsumer.subscribe(Arrays.asList(this.topic));
        }

        private  Properties buildKafkaProperty(String brokers,String groupId){
            Properties properties = new Properties();
            properties.put("bootstrap.servers", brokers);
            properties.put("group.id", groupId);
            properties.put("enable.auto.commit", "true");
            properties.put("auto.commit.interval.ms", "1000");
            properties.put("session.timeout.ms", "30000");
            properties.put("auto.offset.reset", "earliest");
            properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            return properties;
        }

        @Override
        public void run() {
            while (true){
                ConsumerRecords<String,String> consumerRecords = kafkaConsumer.poll(100);
                for(ConsumerRecord<String,String> item : consumerRecords){
                    System.out.println("Consumer Message:"+item.value()+",Partition:"+item.partition()+"Offset:"+item.offset());
                }
            }
        }
    }

    //消费者线程组
    public class ConsumerGroup {
        private final String brokers;
        private final String groupId;
        private final String topic;
        private final int consumerNumber;
        private List<ConsumerThread> consumerThreadList = new ArrayList<ConsumerThread>();

        public ConsumerGroup(String brokers,String groupId,String topic,int consumerNumber){
            this.groupId = groupId;
            this.topic = topic;
            this.brokers = brokers;
            this.consumerNumber = consumerNumber;
            for(int i = 0; i< consumerNumber;i++){
                ConsumerThread consumerThread = new ConsumerThread(brokers,groupId,topic);
                consumerThreadList.add(consumerThread);
            }
        }

        public void start(){
            for (ConsumerThread item : consumerThreadList){
                Thread thread = new Thread(item);
                thread.start();
            }
        }
    }

    //测试线程组
    public  void test(){
        String brokers = kafkaBrokers;
        String groupId = "group01";
        String topic = "HelloWorld-thread";
        int consumerNumber = 3;

        Thread producerThread = new Thread(new ProducerThread(brokers,topic));
        producerThread.start();

        ConsumerGroup consumerGroup = new ConsumerGroup(brokers,groupId,topic,consumerNumber);
        consumerGroup.start();
    }

    //消费者线程池方法
    public class ConsumerThreadHandler implements Runnable {
        private ConsumerRecord consumerRecord;

        public ConsumerThreadHandler(ConsumerRecord consumerRecord){
            this.consumerRecord = consumerRecord;
        }

        @Override
        public void run() {
            System.out.println("Consumer Message:"+consumerRecord.value()+",Partition:"+consumerRecord.partition()+"Offset:"+consumerRecord.offset());
        }
    }
    public class ConsumerThreadPool {

        private final KafkaConsumer<String, String> consumer;
        private final String topic;
        // Threadpool of consumers
        private ExecutorService executor;


        public ConsumerThreadPool(String brokers, String groupId, String topic){
            Properties properties = buildKafkaProperty(brokers,groupId);
            this.consumer = new KafkaConsumer<>(properties);
            this.topic = topic;
            this.consumer.subscribe(Arrays.asList(this.topic));
        }

        public void start(int threadNumber){
            executor = new ThreadPoolExecutor(threadNumber,threadNumber,0L, TimeUnit.MILLISECONDS,
                    new ArrayBlockingQueue<Runnable>(1000), new ThreadPoolExecutor.CallerRunsPolicy());
            while (true){
                ConsumerRecords<String,String> consumerRecords = consumer.poll(100);
                for (ConsumerRecord<String,String> item : consumerRecords){
                    executor.submit(new ConsumerThreadHandler(item));
                }
            }
        }

        private  Properties buildKafkaProperty(String brokers, String groupId){
            Properties properties = new Properties();
            properties.put("bootstrap.servers", brokers);
            properties.put("group.id", groupId);
            properties.put("enable.auto.commit", "true");
            properties.put("auto.commit.interval.ms", "1000");
            properties.put("session.timeout.ms", "30000");
            properties.put("auto.offset.reset", "earliest");
            properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            return properties;
        }
    }
    //测试线程池的用法
    public   void testThreadPool(){
        String brokers = kafkaBrokers;
        String groupId = "group01";
        String topic = "HelloWorld-threadpool";
        int consumerNumber = 3;

        Thread producerThread = new Thread(new ProducerThread(brokers,topic));
        producerThread.start();

        ConsumerThreadPool consumerThread = new ConsumerThreadPool(brokers,groupId,topic);
        consumerThread.start(3);


    }

}
