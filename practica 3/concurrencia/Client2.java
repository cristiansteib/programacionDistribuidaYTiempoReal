import java.rmi.Naming;
import java.rmi.registry.Registry;

public class Client2 {

    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("1 argument needed: (remote) hostname");
            System.exit(1);
        }

        try {
            String rname = "//" + args[0] + ":" + Registry.REGISTRY_PORT + "/remote";
            RemoteClassInterface remote = (RemoteClassInterface) Naming.lookup(rname);
            String data = "client 2";
            remote.sendThisBack(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
