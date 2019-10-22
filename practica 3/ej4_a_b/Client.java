
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
        FileReaderModel remoteFile;
        String fileName = args[1];

        int index = 0;  //
        final int CHUNK_SIZE = 255;
        try {
            //lookup RMI registry
            String registryAddress = "//" + args[0] + ":" + Registry.REGISTRY_PORT + "/remote";
            FileSystemInterface remote = (FileSystemInterface) Naming.lookup(registryAddress);

            BufferedOutputStream bufferLocalWriter = new BufferedOutputStream(new FileOutputStream(ficheroDestinoCliente));

            remoteFile = remote.readFile(fileName, CHUNK_SIZE, index);
            
            while (remoteFile.getCountBytesReaded() != -1) {
                System.out.println("Realizando escritura...");
                remote.writeFile(fileNameRemote, remoteFile.getBytes(), remoteFile.getCountBytesReaded());

                // Copy the data coming from the server to the local machine.
                bufferLocalWriter.write(remoteFile.getBytes(), 0, remoteFile.getCountBytesReaded());
                index += CHUNK_SIZE;
                remoteFile = remote.readFile(fileName, CHUNK_SIZE, index);
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
