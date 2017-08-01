import com.mongodb.MongoClient;
import connections.ConnectionManager;
import kafka.consumers.MyConsumer;
import kafka.producers.MyProducer;
import mongodb.EmployeeMongoDAO;

public class BootStrap {

  public static void main(String args[]) {
    ConnectionManager.getInstance().initMongo(new MongoClient("localhost" , 27017));
    ConnectionManager.getInstance().initProducer(MyProducer.getLocalProducer());
    ConnectionManager.getInstance().initConsumer(MyConsumer.getLocalConsumer());
  }

}
