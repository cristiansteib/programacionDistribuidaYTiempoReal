import java.rmi.registry.Registry; /* REGISTRY_PORT */

public class Client {
    public static void main(String[] args) {

        final int SAMPLES = 10;
        long inicio_tiempo = 0, fin_tiempo = 0;
        long[] times;

        if (args.length != 1) {
            System.out.println("1 argument needed: (remote) hostname");
            System.exit(1);
        }

        try {
            times = new long[SAMPLES];
            String rname = "//" + args[0] + ":" + Registry.REGISTRY_PORT + "/remote";
            IfaceRemote mir = (IfaceRemote) java.rmi.Naming.lookup(rname);

            for (int i = 0; i < SAMPLES; i++) {
                inicio_tiempo = System.nanoTime() / 1000;
                mir.test_time();
                fin_tiempo = System.nanoTime() / 1000;
                times[i] = fin_tiempo - inicio_tiempo;
                System.out.format("valor %d: %d \n", i, times[i]);
            }

            System.out.println("SD = " + StandardDeviation.calculateSD(times));
            System.out.println("Media = " + StandardDeviation.calculateMean(times));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
