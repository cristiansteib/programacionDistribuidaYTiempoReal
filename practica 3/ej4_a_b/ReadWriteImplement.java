import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ReadWriteImplement extends UnicastRemoteObject implements FileSystemInterface {

    protected ReadWriteImplement() throws RemoteException {
        super();
    }

    public FileReaderModel readFile(String nombreA, int cant, int pos) throws RemoteException {

        FileReaderModel fileReader = new FileReaderModel();
        try {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(nombreA));
            byte[] array = new byte[cant];
            in.skip(pos);   //managed by CHUNK_SIZE
            fileReader.setCountBytesReaded(in.read(array, 0, cant));
            fileReader.setBytes(array);

        } catch (FileNotFoundException fe) {
            System.out.println("El archivo a abrir no existe");
        } catch (EOFException ee) {
            System.out.println("End-of-file reached!");
        } catch (IndexOutOfBoundsException ioe) {
            System.out.println("Indice fuera de rango");
            ioe.printStackTrace();
        } catch (IOException ie) {
            System.out.println("Error de I/O");
        } catch (Exception e) {
            System.out.println("Error desconocido");
        }
        return fileReader;
    }

    public int writeFile(String fileName, byte[] buffer, int count) throws RemoteException {
        int totalBytes = 0, index = 0;
        File file = new File(fileName);
        BufferedOutputStream fileBuffer;
        try {
            fileBuffer = new BufferedOutputStream(new FileOutputStream(file, true));
            while (index < count) {
                fileBuffer.write(buffer[index]); //se copia el flujo de byes al fichero destino.
                totalBytes++;
                index++;
            }
            System.out.println("Escritura actual finalizada");
            fileBuffer.close();
            return totalBytes;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}
