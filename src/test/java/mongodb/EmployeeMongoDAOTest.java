package mongodb;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import org.junit.Test;

import java.util.List;

public class EmployeeMongoDAOTest {

    @Test
    public void mongoTest() {
        MongoClient mongo = new MongoClient("localhost", 27017);
//        DB db = mongo.getDB("database name");
        List<String> dbs = mongo.getDatabaseNames();
        for(String db : dbs){
            System.out.println(db);
        }
    }

}

