package connections;

import com.mongodb.MongoClient;
import mongodb.MongoDBClient;

public class ConnectionManager {

    private static ConnectionManager connectionManager = new ConnectionManager();

    public static ConnectionManager getInstance() {
        return connectionManager;
    }

    private ConnectionManager() {
    }

    public void initMongo() {
        MongoDBClient.mongoClient = new MongoClient( "localhost" , 27017 );
    }

}
