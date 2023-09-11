package gitLabAndTestsPractice;

import java.util.Random;

public class StringProducer {
	
	public String generatingString(int i) {
		  int leftLimit = 97; // letter 'a'
		    int rightLimit = 122; // letter 'z'
		    long targetStringLength = i;
		    Random random = new Random();

		    String generatedString = random.ints(leftLimit, rightLimit + 1)
		      .limit(targetStringLength)
		      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
		      .toString();

		    System.out.println(generatedString);
			return generatedString;
	}
	public String reverseString(String string) {
		
		String letters="";
        char ch;
		for (int i=0; i<string.length(); i++)
	      {
	        ch= string.charAt(i); //extracts each character
	        letters= ch+letters; //adds each character in front of the existing string
	      }
	      System.out.println("Reversed word: "+ letters);
		return letters;
	}

}
