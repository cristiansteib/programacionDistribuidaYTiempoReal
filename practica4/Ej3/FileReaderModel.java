package Ej3;

import java.io.Serializable;

public class FileReaderModel implements Serializable {

    private byte bytes[];
    private int countBytesReaded;

    public FileReaderModel() {
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setCountBytesReaded(int cant) {
        this.countBytesReaded = cant;
    }

    public int getCountBytesReaded() {
        return countBytesReaded;
    }

}
