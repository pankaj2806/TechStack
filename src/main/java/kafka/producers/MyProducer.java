package kafka.producers;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.IOException;
import java.util.Properties;

public class MyProducer {

  public static Producer<String, String> producer;

  public static void main(String[] args) throws IOException {
    producer = getLocalProducer();
    try {
      for (int i = 0; i < 1000000; i++) {
        producer.send(new ProducerRecord<>("fast-messages",
                String.format("{\"type\":\"test\", \"t\":%.3f, \"k\":%d}", System.nanoTime() * 1e-9, i)));
        // every so often send to a different topic
        if (i % 1000 == 0) {
          producer.send(new ProducerRecord<>("fast-messages",
                  String.format("{\"type\":\"marker\", \"t\":%.3f, \"k\":%d}", System.nanoTime() * 1e-9, i)));
          producer.send(new ProducerRecord<>("summary-markers",
                  String.format("{\"type\":\"other\", \"t\":%.3f, \"k\":%d}", System.nanoTime() * 1e-9, i)));
          producer.flush();
          System.out.println("Sent msg number " + i);
        }
      }
    } catch (Throwable throwable) {
      System.out.printf("%s", throwable.getStackTrace());
    } finally {
      producer.close();
    }

  }

  public static Producer<String, String> getLocalProducer() {
    Properties props = new Properties();
    props.put("bootstrap.servers", "localhost:9092");
    props.put("acks", "all");
    props.put("retries", 0);
    props.put("batch.size", 16384);
    props.put("linger.ms", 1);
    props.put("buffer.memory", 33554432);
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    return new KafkaProducer<>(props);
  }

}
