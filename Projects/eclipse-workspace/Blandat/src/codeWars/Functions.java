package codeWars;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Functions {

    public static void solve(double meal_cost, int tip_percent, int tax_percent) {
        // Write your code here

        double tip = Math.round((meal_cost)*tip_percent/100);
        double tax = Math.round((meal_cost)*tax_percent/100);
        meal_cost=  (meal_cost+tip+tax);
        System.out.print((int)meal_cost);
    }



    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        double meal_cost = Double.parseDouble(bufferedReader.readLine().trim());

        int tip_percent = Integer.parseInt(bufferedReader.readLine().trim());

        int tax_percent = Integer.parseInt(bufferedReader.readLine().trim());

        Functions.solve(meal_cost, tip_percent, tax_percent);

        bufferedReader.close();
    }

}
