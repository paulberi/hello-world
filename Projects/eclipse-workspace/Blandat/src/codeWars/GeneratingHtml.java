package codeWars;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeneratingHtml {

    private static final Pattern TAG_REGEX = Pattern.compile("<([^<>/]+)>([^<>]+)</(\\1)>");

    public static void main(String[] args) {

        //helloWorld();
        Scanner in = new Scanner(System.in);
        int testCases = Integer.parseInt(in.nextLine());
        while (testCases > 0) {
            String line = in.nextLine();
            List<String> tagValues = getTagValues(line);
            for (String str : tagValues) {
                System.out.println(str);
            }
            testCases--;
        }
    }

    public static List<String> getTagValues(final String str) {
        final List<String> tagValues = new ArrayList<String>();
        final Matcher matcher = TAG_REGEX.matcher(str);
        if (matcher.find()) {
            matcher.reset();
            while (matcher.find()) {
                tagValues.add(matcher.group(2));
            }
            //tagValues.add(matcher.group(2));
        } else {
            tagValues.add("None");
        }
        return tagValues;
    }

    static void helloWorld(){
        Scanner in = new Scanner(System.in);
        String userInput= in.nextLine();
        System.out.println("Hello, World. \n" +
                userInput);

    }
    static void operators(){

    }
}

