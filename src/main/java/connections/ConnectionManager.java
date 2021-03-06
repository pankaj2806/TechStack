package connections;

import com.mongodb.MongoClient;
import kafka.consumers.MyConsumer;
import kafka.producers.MyProducer;
import mongodb.MongoDBClient;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.producer.Producer;

public class ConnectionManager {

  private static ConnectionManager connectionManager = new ConnectionManager();

  public static ConnectionManager getInstance() {
    return connectionManager;
  }

  private ConnectionManager() {
  }

  public void initMongo(MongoClient mongoDBClient) {
    MongoDBClient.mongoClient = mongoDBClient;
  }

  public void initProducer(Producer<String, String> producer) {
    MyProducer.producer = producer;
  }

  public void initConsumer(Consumer<String, String> consumer) {
    MyConsumer.consumer = consumer;
  }

}
