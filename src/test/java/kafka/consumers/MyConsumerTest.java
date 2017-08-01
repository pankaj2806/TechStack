package kafka.consumers;

import connections.ConnectionManager;
import org.apache.kafka.clients.consumer.MockConsumer;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;
import org.apache.kafka.clients.producer.MockProducer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MyConsumerTest {

  @Before
  public void setUp() throws Exception {
    ConnectionManager.getInstance().initConsumer(new MockConsumer<>(OffsetResetStrategy.NONE));
    ConnectionManager.getInstance().initProducer(new MockProducer<>());
  }

  @After
  public void tearDown() throws Exception {

  }

  @Test
  public void consumeMessages() throws Exception {
    MyConsumer.consumeMessages();
  }

}