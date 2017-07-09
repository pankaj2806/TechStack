package mongodb;

import com.mongodb.DB;
import ifaces.EmployeeDAO;
import models.Employee;

import java.util.Set;

public class EmployeeMongoDAO implements EmployeeDAO {

  public Employee getEmployee(String id) {
    return null;
  }

  public void printDatabases() {
    System.out.println(MongoDBClient.mongoClient.getDatabaseNames());
    MongoDBClient.mongoClient.listDatabaseNames();
  }

  public void printDBCollections(String dbName) {
    DB db = MongoDBClient.mongoClient.getDB(dbName);
    Set<String> tables = db.getCollectionNames();
    for(String coll : tables){
      System.out.println(coll);
    }
  }

}
