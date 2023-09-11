package codeWars;

import java.util.Scanner;

public class TestAphabet {

    public static final String regularExpression = "^[a-zA-Z][a-zA-Z0-9_]{7,29}";
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */

        mine();
    }
    static void mine(){
        System.out.printf("How many iterations");

        int numberOfNames= sc.nextInt();
        int x=0;

        while(numberOfNames>0){
            String username= sc.next();

            if (username.matches(regularExpression)) {
                System.out.println("Valid");
            } else {
                System.out.println("Invalid");
            }

            numberOfNames--;
        }
        sc.close();

    }
}
