package Ej3;

import jade.core.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

//-container -host localhost AgenteCris:Ej3.FileSystem(send,testFileX)

public class FileSystem extends Agent {
    private Location origin;
    private static final int CHUNK_SIZE = 10;

    public void setup() {

        this.origin = here();
        String operation = (String) getArguments()[0];
        String fileName = (String) getArguments()[1];
        try {
            switch (operation) {
                case "get":
                    getFile(fileName);
                    break;
                case "send":
                    sendFile(fileName);
                    break;
                default:
                    System.out.println("Parameter error, user get|send operations.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(2);
        }

    }


    private void getFile(String fileName) throws IOException {
        int offset = 0;
        Location serverLocation = new ContainerID("Main-Container", null);
        String localFilePath = "Ej3/fs_local/" + fileName;
        String serverFilePath = "Ej3/fs_server/" + fileName;

        FileData f;
        do {
            doMove(serverLocation);
            f = read(serverFilePath, offset, CHUNK_SIZE); // Lee el archivo del server
            offset += CHUNK_SIZE;
            doMove(this.origin);
            write(localFilePath, f); // escribe en el origen
        } while (!f.finished());
    }

    private void sendFile(String fileName) throws IOException {
        int offset = 0;
        Location destination = new ContainerID("Main-Container", null);
        String originFilePath = "Ej3/fs_local/" + fileName;
        String serverFilePath = "Ej3/fs_server/" + fileName;

        FileData f;
        do {
            doMove(this.origin);
            f = read(originFilePath, offset, CHUNK_SIZE); // lee en el origen el file
            offset += CHUNK_SIZE;
            doMove(destination); // Vuelve al server
            write(serverFilePath, f); // escribe en el server el file leido
        } while (!f.finished());
    }


    public int write(String filePath, FileData fileData) {
        try {
            File file = new File(filePath);
            System.out.println("Escribiendo: " + filePath);
            FileOutputStream fos = new FileOutputStream(file, true);
            fos.write(fileData.getData(), 0, fileData.getAmount());
            fos.close();
            System.out.println("Se escribieron " + fileData.getAmount() + " bytes en " + filePath);
            return fileData.getAmount();
        } catch (IOException e) {
            System.out.println("Error leyendo archivo " + filePath);
            return -1;
        }
    }

    public FileData read(String filePath, int offset, int amount) throws IOException {
        try {
            System.out.println(filePath);
            File file = new File(filePath);

            FileInputStream fis = new FileInputStream(file);
            fis.skip(offset);

            byte[] bytesArray = new byte[Math.min(amount, fis.available())];
            int amountRead = fis.read(bytesArray);

            boolean finished = fis.available() == 0;
            System.out.println(fis.available());
            fis.close();

            System.out.println("Se leyeron " + amountRead + " bytes de " + filePath);
            System.out.println(finished);

            return new FileData(filePath, bytesArray, finished, amountRead);

        } catch (IOException e) {
            System.out.println("Error leyendo archivo " + filePath);
            e.printStackTrace();
            throw new IOException();
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