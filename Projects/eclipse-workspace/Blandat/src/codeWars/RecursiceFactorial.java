package codeWars;

import java.util.Scanner;

public class RecursiceFactorial {

    public static void main(String[] args){

        Scanner n= new Scanner(System.in);
        int input =n.nextInt();

        int result= factorial(input);

        System.out.println(result);

    }
    private static int factorial(int n){
        if(n<=1){
            return 1;
        }else{
            return n*(factorial(n-1));
        }

    }
}
