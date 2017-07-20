package kafka.producers;

import com.google.common.io.Resources;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Producer {

  public static void main(String[] args) throws IOException {
    // set up the producer
    KafkaProducer<String, String> producer;

    Properties props = new Properties();
    props.put("bootstrap.servers", "localhost:9092");
    props.put("acks", "all");
    props.put("retries", 0);
    props.put("batch.size", 16384);
    props.put("linger.ms", 1);
    props.put("buffer.memory", 33554432);
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

    producer = new KafkaProducer<>(props);
    try {
      for (int i = 0; i < 1000000; i++) {
        // send lots of messages
        producer.send(new ProducerRecord<String, String>(
                "fast-messages",
                String.format("{\"type\":\"test\", \"t\":%.3f, \"k\":%d}", System.nanoTime() * 1e-9, i)));

        // every so often send to a different topic
        if (i % 1000 == 0) {
          producer.send(new ProducerRecord<String, String>(
                  "fast-messages",
                  String.format("{\"type\":\"marker\", \"t\":%.3f, \"k\":%d}", System.nanoTime() * 1e-9, i)));
          producer.send(new ProducerRecord<String, String>(
                  "summary-markers",
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
}
