import com.mongodb.MongoClient
import connections.ConnectionManager

object BootStrapScala extends App {

  ConnectionManager.getInstance().initMongo(new MongoClient("localhost" , 27017))

}
