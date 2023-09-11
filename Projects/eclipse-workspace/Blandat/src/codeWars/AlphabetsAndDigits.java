package codeWars;

import java.util.Scanner;

public class AlphabetsAndDigits {

    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        System.out.println("Write a character");
        String letters = in.next();
      /*   char[] c =letters.toCharArray();
       if((c[0] >= 'a' && c[0]<='z') || ((c[0] >= 'A' && c[0]<='Z'))){
            System.out.println(letters+" and "+ c[0]);
        }else{
            System.out.println("Try to Write a character again");
            letters = in.next();
            System.out.println(letters);
        }

        boolean isLetter = letters.matches("[a-zA-Z0-9]");
        boolean isDigit = letters.matches("\\d");

        if(isLetter){
            System.out.println(" its a letter");
        }if(isDigit){
            System.out.println("lie lie");
        }
        else{
            System.out.println("well");
        }*/

        System.out.println(coderByteTest(letters));
    }

    private static String coderByteTest(String str){

        char[] sizeTest= str.toCharArray();
        String firstChar= String.valueOf(sizeTest[0]);
        String lastChar= String.valueOf(sizeTest[sizeTest.length-1]);
        boolean isLetter = firstChar.matches("[a-zA-Z]");
        int countLetters =0;
        int countNumbers =0;
        int countUnderscore=0;

        if(sizeTest.length>=8 && sizeTest.length<=30){
            if(isLetter){
                if(!lastChar.equals("_")){
                    for ( int i=0; i<sizeTest.length; i++){
                        String anyChar= String.valueOf(sizeTest[i]);
                        boolean isCorrect= anyChar.matches("[A-Za-z0-9_]");
                        if(isCorrect){
                            countLetters++;
                        }else{
                            System.out.println("INVALID; "+"'"+anyChar+"'"+" character not allowed");
                            return "Invalid";
                        }
                    }
                }else {
                    System.out.println("INVALID; Username last character is underscore");
                    return "Invalid";
                }
            }else{
                System.out.println("INVALID; Username begins with non-alphabetic character");
                return "Invalid";
            }
        }else{
            System.out.println("INVALID; Username length < 8 characters");
            return "Invalid";
        }
        if(countLetters>0){
            return "Valid";
        }
        return "Invalid";
    }
}
