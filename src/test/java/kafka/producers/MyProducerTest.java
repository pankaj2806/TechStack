package kafka.producers;

import connections.ConnectionManager;
import kafka.admin.AdminUtils;
import kafka.admin.RackAwareMode;
import kafka.server.KafkaConfig;
import kafka.server.KafkaServer;
import kafka.utils.MockTime;
import kafka.utils.TestUtils;
import kafka.utils.ZKStringSerializer$;
import kafka.utils.ZkUtils;
import kafka.zk.EmbeddedZookeeper;
import org.I0Itec.zkclient.ZkClient;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.MockProducer;
import org.apache.kafka.common.utils.Time;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Properties;

import static org.apache.kafka.common.requests.OffsetsForLeaderEpochResponse.TOPIC;
import static org.junit.Assert.*;

public class MyProducerTest {

  private static final String ZKHOST = "127.0.0.1";
  private static final String BROKERHOST = "127.0.0.1";
  private static final String BROKERPORT = "9092";

  @Before
  public void setUp() throws Exception {
    ConnectionManager.getInstance().initProducer(new MockProducer<>());
  }

  @After
  public void tearDown() throws Exception {

  }

  @Test
  public void sendSampleMessages() throws Exception {
    EmbeddedZookeeper zkServer = new EmbeddedZookeeper();
    String zkConnect = ZKHOST + ":" + zkServer.port();
    ZkClient zkClient = new ZkClient(zkConnect, 30000, 30000, ZKStringSerializer$.MODULE$);
    ZkUtils zkUtils = ZkUtils.apply(zkClient, false);


    // setup Broker
    Properties brokerProps = new Properties();
    brokerProps.setProperty("zookeeper.connect", zkConnect);
    brokerProps.setProperty("broker.id", "0");
    brokerProps.setProperty("log.dirs", Files.createTempDirectory("kafka-").toAbsolutePath().toString());
    brokerProps.setProperty("listeners", "PLAINTEXT://" + BROKERHOST +":" + BROKERPORT);
    KafkaConfig config = new KafkaConfig(brokerProps);
    Time mock = new MockTime();
    KafkaServer kafkaServer = TestUtils.createServer(config, mock);

    // create topic
    AdminUtils.createTopic(zkUtils, TOPIC, 1, 1, new Properties(), RackAwareMode.Disabled$.MODULE$);


    // setup consumer
    Properties consumerProps = new Properties();
    consumerProps.setProperty("bootstrap.servers", BROKERHOST + ":" + BROKERPORT);
    consumerProps.setProperty("group.id", "group0");
    consumerProps.setProperty("client.id", "consumer0");
    consumerProps.setProperty("key.deserializer","org.apache.kafka.common.serialization.IntegerDeserializer");
    consumerProps.setProperty("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");
    consumerProps.put("auto.offset.reset", "earliest");  // to make sure the consumer starts from the beginning of the topic
    KafkaConsumer<Integer, byte[]> consumer = new KafkaConsumer<>(consumerProps);
    consumer.subscribe(Arrays.asList(TOPIC));

    // setup producer
    Properties producerProps = new Properties();
    producerProps.setProperty("bootstrap.servers", BROKERHOST + ":" + BROKERPORT);
    producerProps.setProperty("key.serializer","org.apache.kafka.common.serialization.IntegerSerializer");
    producerProps.setProperty("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
    KafkaProducer<Integer, byte[]> producer = new KafkaProducer<Integer, byte[]>(producerProps);

    // starting consumer
    ConsumerRecords<Integer, byte[]> records = consumer.poll(10000);
    assertEquals(1, records.count());
    Iterator<ConsumerRecord<Integer, byte[]>> recordIterator = records.iterator();
    ConsumerRecord<Integer, byte[]> record = recordIterator.next();
    System.out.printf("offset = %d, key = %s, value = %s", record.offset(), record.key(), record.value());
    assertEquals(42, (int) record.key());
    assertEquals("test-message", new String(record.value(), StandardCharsets.UTF_8));

    kafkaServer.shutdown();
    zkClient.close();
    zkServer.shutdown();
  }

}