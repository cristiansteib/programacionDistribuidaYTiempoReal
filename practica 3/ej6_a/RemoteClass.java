public class RemoteClass extends java.rmi.server.UnicastRemoteObject implements IfaceRemote {

    public RemoteClass() throws java.rmi.RemoteException {
        super();
    }

    public int test_time() throws java.rmi.RemoteException {
        return 0;
    }
}
