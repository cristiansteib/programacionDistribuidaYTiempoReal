package Ej3;

import jade.core.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

//-container -host localhost AgenteCris:Ej3.FileSystem(send,testFileX)

public class FileSystem extends Agent {
    private Location originLocation;
    private Location serverLocation;
    private FileData f;
    private String operation;
    private static final int CHUNK_SIZE = 10;
    private String pathToWriteFile;
    private String pathToReadFile;

    public void setup() {

        this.originLocation = here();
        operation = (String) getArguments()[0];
        String fileName = (String) getArguments()[1];
        try {
            switch (operation) {
                case "get":
                    pathToWriteFile = "fs_local/";
                    pathToReadFile = "fs_server";
                    afterMove();
                    break;
                case "send":
                    pathToWriteFile = "fs_server/";
                    pathToReadFile = "fs_local/";
                    afterMove();
                    break;
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
            doMove(this.originLocation);
            write(localFilePath, f); // escribe en el origen
            System.out.println(here());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (!f.finished());
    }

    private void sendFile(String fileName) throws IOException {
        int offset = 0;
        Location destination = new ContainerID("Main-Container",null);
        String originFilePath = "Ej3/fs_local/" + fileName;
        String serverFilePath = "Ej3/fs_server/" + fileName;
        System.out.println(destination.getID());
        System.out.println(this.originLocation.getID());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        FileData f;
        do {

            f = read(originFilePath, offset, CHUNK_SIZE); // lee en el origen el file
            offset += CHUNK_SIZE;
            System.out.println("NOW1: "+here().getName());

            doMove(destination); // Vuelve al server
            write(serverFilePath, f); // escribe en el server el file leido
            System.out.println("NOW2: "+here().getName());
            doMove(this.originLocation);

        } while (!f.finished());
    }

    private void writeAndMove(Location location, FileData f, String path) {

        write(pathToWriteFile, f); // escribe en el server el file leido

        if (!f.finished()) {
            doMove(location);
        }
    }

    private void readAndMove(Location location) {

        f = read(pathToReadFile); // escribe en el server el file leido

        if (!f.finished()) {
            doMove(location);
        }
    }

    private Boolean iAmServer(){
        return true;
    }

    @Override
    protected void afterMove() {
        switch (this.operation) {
            
            case "get":
                if (iAmServer()) {
                    readAndMove(originLocation);
                } else {
                    writeAndMove(serverLocation);
                }
                break;

            case "send":
                if (iAmServer()) {
                    writeAndMove(originLocation);
                } else {
                    readAndMove(serverLocation);
                }
                break;
        }
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