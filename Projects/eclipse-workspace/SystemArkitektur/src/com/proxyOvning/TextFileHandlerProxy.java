package com.proxyOvning;

import java.util.HashSet;
import java.util.Set;

public class TextFileHandlerProxy {

    private TextFileHandler textFileHandler;
    private Set<String> badWords = new HashSet<>();

    public TextFileHandlerProxy(String filePath) {
        this.textFileHandler = new TextFileHandler(filePath);
        this.badWords.add("bad");
        this.badWords.add("Bad");
        this.badWords.add("BAD");
    }

    public void write(String string) throws InvalidLanguageException {
        if(this.badWords.contains(string)) {
            System.out.println("Logging bad word " + string);
            throw new InvalidLanguageException(string);
        } else {
            System.out.println("Logging write of word " + string);
            this.textFileHandler.write(string);
        }
    }

    public String read() {
        String str = this.textFileHandler.read();
        return str;
    }
}
