import com.mongodb.MongoClient;
import connections.ConnectionManager;
import mongodb.EmployeeMongoDAO;

public class BootStrap {

  public static void main(String args[]) {
    ConnectionManager.getInstance().initMongo(new MongoClient("localhost" , 27017));
    new EmployeeMongoDAO().printDatabases();
    new EmployeeMongoDAO().printDBCollections("userDB");
  }

}
