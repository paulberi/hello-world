package com.DTODemo;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Main {

    public static void main(String[] args) {
        Car car = new Car("Blue", "Ford", 190);
        CarDTO carDTO = new CarDTO(car);

        String filePath = "c:\\temp\\test.dat";
        try {
            // Should be network stream...
            FileOutputStream fos = new FileOutputStream(filePath);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(carDTO);
            out.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        Server server = new Server();
        server.accept(filePath); // Network call ...

    }
}
