import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ReadWriteImplement extends UnicastRemoteObject implements IfaceReadWrite {

    protected ReadWriteImplement() throws RemoteException {
        super();
    }

    public ReadStructure readFile(String nombreA, int cant, int pos) throws RemoteException {

        ReadStructure lectura = new ReadStructure();
        try {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(nombreA));
            byte[] array = new byte[cant];
            in.skip(pos);   //managed by CHUNK_SIZE
            lectura.setCantBytesLeidos(in.read(array, 0, cant));
            lectura.setRetBytes(array);

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
        return lectura;
    }

    public int writeFile(String fileName, byte[] buffer, int cant) throws RemoteException {
        int result = 0, i = 0;
        File ficheroDestino = new File(fileName);
        BufferedOutputStream escritorFichero;
        try {
            escritorFichero = new BufferedOutputStream(new FileOutputStream(ficheroDestino, true));
            while (i < cant) {
                escritorFichero.write(buffer[i]);//se copia el flujo de byes al fichero destino.
                result++;
                i++;
            }
            System.out.println("Escritura actual finalizada");
            escritorFichero.close();
            return result;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}
