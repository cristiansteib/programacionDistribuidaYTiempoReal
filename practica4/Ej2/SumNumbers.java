package Ej2;

import jade.core.*;

import java.nio.file.Files;
import java.nio.file.Paths;

// Como ejecutarlo?
// -container -host localhost AgenteCris:Ej2.SumNumbers(Container_name, Absolute_path_to_file)
public class SumNumbers extends Agent {
    private Location origin;
    private String fileName;
    private int sumResult = 0;

    public void setup() {
        this.origin = here();
        String destination = (String) getArguments()[0];
        this.fileName = (String) getArguments()[1];

        System.out.println("\n\nAgente actual: " + getLocalName());

        try {
            ContainerID d = new ContainerID(destination, null);
            doMove(d);
        } catch (Exception e) {
            System.out.println("Error al migrar el agente");
            e.printStackTrace();
        }
    }

    protected void afterMove() {
        if (here().getID().equals(this.origin.getID())) {
            System.out.println("\n\n\nSuma: " + this.sumResult + "\n\n\n");
        } else {
            System.out.println("Agente migrada em: " + getLocalName());
            System.out.println("Accediendo al archivo:" + this.fileName);
            try {
                String[] numbers = new String(Files.readAllBytes(Paths.get(this.fileName))).split("\n");
                for (String n : numbers) {
                    this.sumResult += Integer.parseInt(n);
                }
            } catch (Exception e) {
                this.sumResult = -1;
                e.printStackTrace();
            }
            doMove(this.origin);
        }
    }
}
