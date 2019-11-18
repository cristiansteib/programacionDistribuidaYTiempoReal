import java.io.Serializable;

public class SystemInfo implements Serializable {
    String hostname;
    Long memoryAvailable= 0L;
    Double loadAverage=0D;

    @Override
    public String toString() {
        return "Host:" + hostname + " | Memory:"+memoryAvailable + " | Load:" + loadAverage;
    }
}
