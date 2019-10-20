import java.io.Serializable;

public class ReadStructure implements Serializable {

    private byte retBytes[];
    private int cantBytesLeidos;

    public ReadStructure() {
    }

    public void setRetBytes(byte[] bytes) {
        this.retBytes = bytes;
    }

    public byte[] getRetBytes() {
        return retBytes;
    }

    public void setCantBytesLeidos(int cant) {
        this.cantBytesLeidos = cant;
    }

    public int getCantBytesLeidos() {
        return cantBytesLeidos;
    }


}
