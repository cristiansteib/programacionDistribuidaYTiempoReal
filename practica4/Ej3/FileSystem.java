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
    private static final int  CHUNK_SIZE = 20;

    public void setup() {

        this.origin = here();
        String operation = (String) getArguments()[0];
        String fileName = (String) getArguments()[1];

        switch (operation) {
            case "get":
                try {
                    getFile(fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(2);
                }
                break;
            case "send":
                try {
                    sendFile(fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(2);
                }
                break;
            default:
                System.out.println("Parameter error, user get|send operations.");
        }
    }


    private void getFile(String fileName) throws IOException {
        int offset = 0;
        Location serverLocation = new ContainerID("Main-Container", null);
        String localFilePath =  "Ej3/fs_local/" + fileName;
        String serverFilePath = "Ej3/fs_server/" + fileName;
        // Lo muevo al server

        while (true) {
            doMove(serverLocation);
            FileData f = read(serverFilePath, offset, CHUNK_SIZE); // Lee el archivo del server
            offset += CHUNK_SIZE;

            doMove(this.origin);
            write(localFilePath, f); // escribe en el origen
            if (f.finished()) {
                break;
            }
        }
    }

    private void sendFile(String fileName) throws IOException {
        int offset = 0;
        int chunkSize = 20;
        Location destination = new ContainerID("Main-Container", null);
        String originFilePath = "Ej3/fs_local/" + fileName;
        String serverFilePath = "Ej3/fs_server/" + fileName;

        FileData f = read(originFilePath, offset, chunkSize);  // Leo el archivo del origen
        offset += offset + chunkSize;
        doMove(destination);  // Lo muevo al server
        write(serverFilePath, f); // escribe en el server
        while (!f.finished()) {
            doMove(this.origin);
            f = read(originFilePath, offset, chunkSize); // lee en el origen el file
            offset += offset + chunkSize;
            doMove(destination); // Vuelve al server
            write(serverFilePath, f); // escribe en el server el file leido
        }
    }


    public int write(String filePath, FileData fileData) {
        try {
            File file = new File(filePath);
            System.out.println("Escribiendo: " + filePath);
            FileOutputStream fos = new FileOutputStream(file, true);
            fos.write(fileData.getData(), 0, fileData.getAmount());
            fos.close();
            System.out.println("Se escribieron " + fileData.getAmount() + " bytes en " + fileData.getName());
            return fileData.getAmount();
        } catch (IOException e) {
            System.out.println("Error leyendo archivo " + fileData.getName());
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