package Ej3;
import jade.core.*;

import java.lang.IndexOutOfBoundsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;

//-container -host localhost AgenteCris:Ej3.FileSystem(send,testFileX)
public class FileSystem extends Agent {
    private Location origin;
    private String fileName;
    private String mode;

    private FileData file;
    private int offset = 0;

    public void setup() {
        try {
            this.origin = here();
            this.mode = (String)getArguments()[0];
            this.fileName = (String)getArguments()[1];

            if(this.mode.equals("get") && this.mode.equals("send")) {
                throw new IllegalArgumentException();
            }

            System.out.println("Se traera el archivo " + this.fileName);
            doMove(new ContainerID("Main-Container", null));
        } catch(IllegalArgumentException e) {
            System.out.println("Parameter error, user read|write operations.");
        } catch (Exception e) {
            System.out.println("Error al miragrar el agente");
            e.printStackTrace();
        }
    }

    protected void readFile(Location next){
        System.out.println("\nLeo\n");

        if(this.file == null || !this.file.finished()) {
            try {
                this.file = read(this.fileName, this.offset, 1024);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(2);
            }
            doMove(next);
        } else {
            System.out.println("\nSe termino de transferir el archivo " + this.fileName + "\n");
        }
    }

    protected void afterMove() {
        Location next = atHome() ? new ContainerID("Main-Container", null) : this.origin;

        System.out.println("\nqeu onda\n");

        if((atHome() && this.mode.equals("get")) || (!atHome() && this.mode.equals("send"))) {
            if (this.file == null){
                System.out.println("\nEs null, muevo\n");
                readFile(next);

            }
            System.out.println(this.file);

            write(this.file);
            this.offset += this.file.getAmount();
            if (!this.file.finished()) {

                doMove(next);
            } else {
                System.out.println("\nSe termino de transferir el archivo " + this.fileName + "\n");
            }
        } else {
            readFile(next);

        }
    }

    public boolean atHome() {
        return here().getID().equals(this.origin.getID());
    }

    public int write(FileData fileData) {
        try {
            File file = new File("Ej3/local/" + fileData.getName());
            System.out.println("Escribiendo: "+fileData.getName());

            FileOutputStream fos = new FileOutputStream(file,true);
            fos.write(fileData.getData(),0,fileData.getAmount());
            fos.close();
            System.out.println("Se escribieron " + fileData.getAmount() + " bytes en " + fileData.getName());
            return fileData.getAmount();
        } catch (IOException e) {
            System.out.println("Error leyendo archivo " + fileData.getName());
            return -1;
        }
    }

    public FileData read(String fileName, int offset, int amount) throws IOException {
        try {
            File file = new File("Ej3/SRC_SERVER/" + fileName);
            System.out.println("LEyendo: "+fileName);
            FileInputStream fis = new FileInputStream(file);
            fis.skip(offset);
            byte[] bytesArray = new byte[Math.min(amount,fis.available())];
            int amountRead = fis.read(bytesArray);
            boolean finished = fis.available() == 0;
            fis.close();
            System.out.println("Se leyeron " + amountRead + " bytes de " + fileName);
            return new FileData(fileName,bytesArray,finished,amountRead);
        } catch (IOException e) {
            System.out.println("Error leyendo archivo " + fileName);
            e.printStackTrace();
            throw  new IOException();
        }
    }
}

class FileData implements Serializable {

    private String name;
    private byte[] data;
    private boolean finished;
    private int amount;

    public FileData(String name, byte[] data, boolean finished, int amount) {
        this.name = name;
        this.data = data;
        this.finished = finished;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public byte[] getData() {
        return data;
    }

    public boolean finished() {
        return finished;
    }

    public int getAmount() {
        return amount;
    }
}