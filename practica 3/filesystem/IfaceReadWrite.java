import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IfaceReadWrite extends Remote {

    ReadStructure lectura(String nombreA, int pos, int cant) throws RemoteException;

    int escritura(String nombreEscritura, byte[] buffer, int cant) throws RemoteException;
}
