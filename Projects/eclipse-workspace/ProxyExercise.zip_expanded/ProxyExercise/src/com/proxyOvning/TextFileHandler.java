package com.proxyOvning;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class TextFileHandler {
    private String filePath;

    public TextFileHandler(String filePath) {
        this.filePath = filePath;
    }

    public void write(String string) {
        try {
            FileWriter fileWriter = new FileWriter(this.filePath, true);
            fileWriter.write(string + "\n");
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String read() {
        ArrayList<String> lines = new ArrayList<>();
        try {
            lines = (ArrayList<String>)Files.readAllLines(Paths.get(this.filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String result = "";
        for(String line : lines) {
            result += line + "\n";
        }
        return result;
    }


}
