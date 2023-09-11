package codeWars;
import java.io.*;
import java.util.*;

public class Solution {

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */

        System.out.println("count");
        Scanner in = new Scanner(System.in);
        int count = in.nextInt();

        for (int i=0; i<count; i++){
            System.out.println("Enter age");
            int age= in.nextInt();

            Person p = new Person(age);
            for (int j = 0; j < 3; j++) {
                if(p.age==0){
                    p.yearPasses();
                }else if(j==0){
                    p.yearPasses();
                    System.out.println("");
                }
                else{
                    p.setAge();
                    p.yearPasses();

                }

            }
        }

    }
}

class Person {
    int age;

    public Person(int initialAge){
        if(initialAge<0){
            this.age=0;
            System.out.println("Age is not valid, setting age to 0.");
        }else {
            this.age = initialAge;
        }
    }

    public int yearPasses(){
        return this.age++;
    }

    public void setAge(){
        if (this.age < 12){
            System.out.println("You are young.");
        }
        else if(this.age>=12 && this.age < 18){
            System.out.println("You are a teenager.");
        }
        else if(this.age >= 18){
            System.out.println("You are old.");
        }
    }
}
