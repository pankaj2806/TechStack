package kafka;

import kafka.consumers.MyConsumer;
import kafka.producers.MyProducer;

import java.io.IOException;

public class Driver {

  public static void produceAndConsume() {
    MyProducer.sendSampleMessages();
    try {
      MyConsumer.consumeMessages();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
