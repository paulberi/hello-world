package com.DTODemo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Server {


    public void accept(String filePath) {
        try {
            FileInputStream fis = new FileInputStream(filePath);
            ObjectInputStream ois = new ObjectInputStream(fis);
            CarDTO carDTO = (CarDTO)ois.readObject();
            System.out.println("Sever obtained CarDTO");
            System.out.println(carDTO.getId() + " " + carDTO.getBrand() + " " + carDTO.getColor());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
