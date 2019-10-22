import java.rmi.Remote;
import java.rmi.RemoteException;


public interface RemoteClassInterface extends Remote {

    String sendThisBack(String data) throws RemoteException;

}
