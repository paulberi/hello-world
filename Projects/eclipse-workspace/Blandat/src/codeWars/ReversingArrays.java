package codeWars;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReversingArrays {

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(bufferedReader.readLine().trim());

        List<Integer> arr = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        System.out.println(arr);

        Collections.reverse(arr);

        String spacing ="";
        for(Integer j:arr){
            spacing+=j+" ";
            }

        System.out.println(spacing);
    }

    /*
        public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(bufferedReader.readLine().trim());

        List<Integer> arr = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
            .map(Integer::parseInt)
            .collect(Collectors.toList());

            List<Integer> arr1 = new ArrayList<>();

        for(int i =arr.size()-1; i>=0; i--){
            for(Integer j:arr){
                if(arr.indexOf(j)==i){
                    arr1.add(j);
                }

            }
        }
        System.out.println(arr1.stream().map(Object::toString).collect(Collectors.joining(" ")));

        bufferedReader.close();
    }
}

    */

}
