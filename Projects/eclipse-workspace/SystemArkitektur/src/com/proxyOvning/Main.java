package com.proxyOvning;

public class Main {

    public static void main(String[] args) {
	    // Without proxy
        TextFileHandler textFileHandler = new TextFileHandler("c:\\temp\\textdata.txt");
        textFileHandler.write("good");
        textFileHandler.write("bad");
        System.out.println(textFileHandler.read());

        // With proxy
        TextFileHandlerProxy textFileHandlerProxy = new TextFileHandlerProxy("c:\\temp\\textdataproxy.txt");
        try {
            textFileHandlerProxy.write("good");
            textFileHandlerProxy.write("bad");
        } catch(InvalidLanguageException exception) {
            System.out.println("Exception " + exception.getMessage());
        }
        System.out.println(textFileHandlerProxy.read());
    }
}
