import connections.ConnectionManager

object BootStrapScala extends App {

  ConnectionManager.getInstance().initMongo()

}
