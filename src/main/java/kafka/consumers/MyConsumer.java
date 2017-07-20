package kafka.consumers;

import org.HdrHistogram.Histogram;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

public class MyConsumer {

  public static KafkaConsumer<String, String> consumer;

  public static void main(String[] args) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    Histogram stats = new Histogram(1, 10000000, 2);
    Histogram global = new Histogram(1, 10000000, 2);

    Properties props = new Properties();
    props.put("bootstrap.servers", "localhost:9092");
    props.put("group.id", "test");
    props.put("enable.auto.commit", "true");
    props.put("auto.commit.interval.ms", "1000");
    props.put("session.timeout.ms", "30000");
    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    consumer = new KafkaConsumer<>(props);
    consumer.subscribe(Arrays.asList("fast-messages", "summary-markers"));
    int timeouts = 0;
    while (true) {
      ConsumerRecords<String, String> records = consumer.poll(200);
      if (records.count() == 0) {
        timeouts++;
      } else {
        System.out.printf("Got %d records after %d timeouts\n", records.count(), timeouts);
        timeouts = 0;
      }
      for (ConsumerRecord<String, String> record : records) {
        switch (record.topic()) {
          case "fast-messages":
            // the send time is encoded inside the message
            JsonNode msg = mapper.readTree(record.value());
            switch (msg.get("type").asText()) {
              case "test":
                long latency = (long) ((System.nanoTime() * 1e-9 - msg.get("t").asDouble()) * 1000);
                stats.recordValue(latency);
                global.recordValue(latency);
                break;
              case "marker":
                // whenever we get a marker message, we should dump out the stats
                // note that the number of fast messages won't necessarily be quite constant
                System.out.printf("%d messages received in period, latency(min, max, avg, 99%%) = %d, %d, %.1f, %d (ms)\n",
                        stats.getTotalCount(),
                        stats.getValueAtPercentile(0), stats.getValueAtPercentile(100),
                        stats.getMean(), stats.getValueAtPercentile(99));
                System.out.printf("%d messages received overall, latency(min, max, avg, 99%%) = %d, %d, %.1f, %d (ms)\n",
                        global.getTotalCount(),
                        global.getValueAtPercentile(0), global.getValueAtPercentile(100),
                        global.getMean(), global.getValueAtPercentile(99));

                stats.reset();
                break;
              default:
                throw new IllegalArgumentException("Illegal message type: " + msg.get("type"));
            }
            break;
          case "summary-markers":
            break;
          default:
            throw new IllegalStateException("Shouldn't be possible to get message on topic " + record.topic());
        }
      }
    }
  }
}
