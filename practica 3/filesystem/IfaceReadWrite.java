import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IfaceReadWrite extends Remote {

    ReadStructure readFile(String nombreA, int pos, int cant) throws RemoteException;

    int writeFile(String nombreEscritura, byte[] buffer, int cant) throws RemoteException;
}
