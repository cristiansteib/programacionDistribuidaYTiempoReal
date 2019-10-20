
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.Registry; /* REGISTRY_PORT */

public class Client {

    public static void main(String[] args) {

        if (args.length < 2) {
            System.out.println("2 arguments needed: (remote) hostname filename (sin comillas)");
            System.exit(1);
        }

        String fileNameLocal = args[1] + "_local";
        String fileNameRemote = args[1] + "_remoto";

        File ficheroDestinoCliente = new File(fileNameLocal);
        ReadStructure rl;
        String fileName = args[1];

        int index = 0;  //
        final int CHUNK_SIZE = 255;
        try {
            //lookup RMI registry
            String rname = "//" + args[0] + ":" + Registry.REGISTRY_PORT + "/remote";
            IfaceReadWrite remote = (IfaceReadWrite) Naming.lookup(rname);

            BufferedOutputStream bufferLocalWriter = new BufferedOutputStream(new FileOutputStream(ficheroDestinoCliente));

            rl = remote.lectura(fileName, CHUNK_SIZE, index);
            while (rl.getCantBytesLeidos() != -1) {
                System.out.println("Realizando escritura...");
                remote.escritura(fileNameRemote, rl.getRetBytes(), rl.getCantBytesLeidos());

                // Copy the data coming from the server to the local machine.
                bufferLocalWriter.write(rl.getRetBytes(), 0, rl.getCantBytesLeidos());
                index += CHUNK_SIZE;
                rl = remote.lectura(fileName, CHUNK_SIZE, index);
            }
            bufferLocalWriter.close();
        } catch (FileNotFoundException fe) {
            System.out.println(fe.getMessage());
        } catch (RemoteException re) {
            re.printStackTrace();
        } catch (Exception e) {
            System.out.println("Error desconocido");
            e.printStackTrace();
        }
    }
}
