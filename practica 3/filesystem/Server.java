import java.rmi.*;
import java.rmi.registry.*;

public class Server {
    public static void main(String[] args) {
        try {
            IfaceReadWrite robject = new ReadWriteImplement();
            String rname = "//localhost:" + Registry.REGISTRY_PORT + "/remote";
            Naming.rebind(rname, robject);

        } catch (Exception e) {
            System.out.println("Hey, an error occurred at Naming.rebind");
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
