import connections.ConnectionManager;

public class BootStrap {

    public static void main(String args[]) {
        ConnectionManager.getInstance().initMongo();
    }

}
