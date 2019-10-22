import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

public class Client {

    public static void main(String[] args) {
        System.setProperty("sun.rmi.transport.tcp.responseTimeout", "2000");

        if (args.length != 1) {
            System.out.println("1 argument needed: (remote) hostname");
            System.exit(1);
        }
        try {
            String rname = "//" + args[0] + ":" + Registry.REGISTRY_PORT + "/remote";
            RemoteClassInterface remote = (RemoteClassInterface) Naming.lookup(rname);
            String result;

            result = remote.sendThisBack("");
            System.out.println(result);
        }catch (RemoteException e){
            System.out.println(e.getCause());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
