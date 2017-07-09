package mongodb;

import com.github.fakemongo.Fongo;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import connections.ConnectionManager;
import org.junit.Before;
import org.junit.Test;

public class EmployeeMongoDAOTest {

  @Before
  public void setUp() {
    Fongo fongo = new Fongo("");
    ConnectionManager.getInstance().initMongo(fongo.getMongo());
    DBCollection collection = MongoDBClient.mongoClient.getDB("mydb").getCollection("mycollection");
    collection.insert(new BasicDBObject("name", "jon"));
  }

  @Test
  public void mongoTest() {
    new EmployeeMongoDAO().printDatabases();
    new EmployeeMongoDAO().printDBCollections("mydb");
  }

}

