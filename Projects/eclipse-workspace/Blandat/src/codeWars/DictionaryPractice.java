package codeWars;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class DictionaryPractice {

    public static void main(String[] args) throws IOException {

        Scanner in = new Scanner(System.in);

        int numberOfItems= in.nextInt();

        Map<String, String> dictionary = new HashMap<String, String>();

        for (int i=0; i<numberOfItems; i++){


            String n = in.nextLine();
            if(n.equals("")){
                n = in.nextLine();
            }

            String[] userInfo = n.split("\\s+");
            String name = userInfo[0];
            String phone = userInfo[1];

            dictionary.put(name, phone);
        }



        while(in.hasNextLine()){

            String name = in.nextLine();;

            if(dictionary.containsKey(name)){
                System.out.println(name+"="+dictionary.get(name));
            }else{
                System.out.println("Not found");
            }
        }
    }
}
