package mongodb;

import com.github.fakemongo.Fongo;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import connections.ConnectionManager;
import org.junit.Before;
import org.junit.Test;

public class EmployeeMongoDAOTest {

  @Before
  public void setUp() {
    Fongo fongo = new Fongo("");
    ConnectionManager.getInstance().initMongo(fongo.getMongo());
    DBCollection collection = MongoDBClient.mongoClient.getDB("mydb").getCollection("mycollection");
  }



  @Test
  public void mongoTest() {
    new EmployeeMongoDAO().printDatabases();
    new EmployeeMongoDAO().printDBCollections("mydb");




    new EmployeeMongoDAO().insert("mydb");
    new EmployeeMongoDAO().printAllDocuments("mydb");
    new EmployeeMongoDAO().printDBCollections("mydb");
//    new EmployeeMongoDAO().delete("mydb ");
    new EmployeeMongoDAO().update("mydb");
    new EmployeeMongoDAO().printAllDocuments("mydb");

  }

}

