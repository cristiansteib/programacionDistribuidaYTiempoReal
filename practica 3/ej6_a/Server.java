import java.rmi.Naming;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String[] args) {
        try {

            String registryName = "//localhost:" + Registry.REGISTRY_PORT + "/remote";
            System.out.println(registryName);
            IfaceRemote remote = new RemoteClass();
            Naming.rebind(registryName, remote);

        } catch (Exception e) {
            System.out.println("Hey, an error occurred at Naming.rebind");
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
