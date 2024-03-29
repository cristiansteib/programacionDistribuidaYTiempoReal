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
    private FileData fileData;
    private String operation;
    private static final int CHUNK_SIZE = 10024;
    private String pathToWriteFile;
    private String pathToReadFile;
    private int currentReadOffset = 0;

    public void setup() {

        this.originLocation = here();
        this.serverLocation = new ContainerID("Main-Container", null);
        operation = (String) getArguments()[0];
        String fileName = (String) getArguments()[1];

        switch (operation) {
            case "get":
                this.pathToWriteFile = "Ej3/fs_local/" + fileName;
                this.pathToReadFile = "Ej3/fs_server/" + fileName;
                doMove(serverLocation);
                break;
            case "send":
                this.pathToWriteFile = "Ej3/fs_server/" + fileName;
                this.pathToReadFile = "Ej3/fs_local/" + fileName;
                afterMove();
                break;
            default:
                System.out.println("Invalid operation, use get|send");
                System.exit(2);
        }
    }

    private void writeAndMove(Location location) {
        write(pathToWriteFile, fileData);
        if (!fileData.finished()) {
            doMove(location);
        }
    }

    private void readAndMove(Location location) {
        fileData = read(pathToReadFile);
        doMove(location);
    }

    private Boolean iAmHome() {
        return here().getID().equals(this.originLocation.getID());
    }

    @Override
    protected void afterMove() {
        switch (this.operation) {

            case "get":
                if (iAmHome()) {
                    writeAndMove(serverLocation);
                } else {
                    readAndMove(originLocation);
                }
                break;

            case "send":
                if (iAmHome()) {
                    readAndMove(serverLocation);
                } else {
                    writeAndMove(originLocation);
                }
                break;
        }
    }

    private int write(String filePath, FileData fileData) {
        try {
            File file = new File(filePath);
            System.out.println("Escribiendo: " + filePath);
            FileOutputStream fos = new FileOutputStream(file, true);
            fos.write(fileData.getData(), 0, fileData.getAmount());
            fos.close();
            System.out.println("Se escribieron " + fileData.getAmount() + " bytes en " + filePath);
            return fileData.getAmount();
        } catch (IOException e) {
            System.out.println("Error escribiendo archivo " + filePath);
            e.printStackTrace();
            return -1;
        }
    }

    private FileData read(String filePath) {
        try {
            System.out.println(filePath);
            File file = new File(filePath);

            FileInputStream fis = new FileInputStream(file);
            fis.skip(currentReadOffset);

            byte[] bytesArray = new byte[Math.min(CHUNK_SIZE, fis.available())];
            int amountRead = fis.read(bytesArray);

            boolean finished = fis.available() == 0;
            System.out.println(fis.available());
            fis.close();

            System.out.println("Se leyeron " + amountRead + " bytes de " + filePath);
            currentReadOffset += CHUNK_SIZE;
            return new FileData(filePath, bytesArray, finished, amountRead);

        } catch (IOException e) {
            System.out.println("Error leyendo archivo " + filePath);
            e.printStackTrace();
            System.exit(127);
            return null;
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