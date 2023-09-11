package codeWars;

import java.util.*;

public class PrintingStringArrays {


    static Scanner in = new Scanner((System.in));

    public static void main(String[] args){

        convertToBase10();

        Scanner in = new Scanner((System.in));
        int input = in.nextInt();


        for( int i=0; i<input; i++){
            String userInput=in.nextLine();
            if(userInput==""){
                userInput=in.nextLine();
            }
            printingChar(userInput);
        }

    }

    static void printingChar(String userInput){

        String odd="";
        String even="";
        char[] input=userInput.toCharArray();
        for(int i=0; i<input.length; i++){
            if(i%2==0){

                even=even +String.valueOf(input[i]);
            }else{
                odd= odd+String.valueOf(input[i]);
            }
        }
        System.out.println(even+" "+odd);
    }

    private static void convertToBase10(){

        String baseTwo=Integer.toString(5875425,2);

        String[] array=baseTwo.split("0");

        List<Integer> count=new ArrayList<>();

        for(int i=0; i< array.length; i++){

           int size= array[i].toCharArray().length;

           if(!count.contains(size)){
               count.add(size);
           }
        }
        Collections.sort(count);
        System.out.println(count.get(count.size()-1));
    }
}
