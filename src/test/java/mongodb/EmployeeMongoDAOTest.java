package mongodb;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import connections.ConnectionManager;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class EmployeeMongoDAOTest {

    @Test
    public void mongoTest() {
        List<String> dbs = MongoDBClient.mongoClient.getDatabaseNames();
        for(String db : dbs){
            System.out.println(db);
        }

    }

    @Before
    public void setUp() {
        ConnectionManager.getInstance().initMongo();
    }

}

