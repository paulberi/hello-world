package kattis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RollCall {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int leftLimit = 97;
		int rightLimit = 122;
		int nameLength;
		String firstName = null;
		String lastName = null;
		Random random = new Random();
		List<String> classList=new ArrayList<>();

		for (int i = 1; i <= 200; i++) {
			nameLength = random.nextInt(20+1);
			StringBuilder firstNameGenerator = new StringBuilder(nameLength);
			StringBuilder secondNameGenerator = new StringBuilder(nameLength);
			for (int j = 0; j < nameLength; j++) {
				int randomLimitedInt = leftLimit + random.nextInt(rightLimit - leftLimit + 1);
				int randomLimitedInts = leftLimit + random.nextInt(rightLimit - leftLimit + 1);
				firstNameGenerator.append((char) randomLimitedInt);
				firstName = firstNameGenerator.toString();
				firstName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1).toLowerCase();
				secondNameGenerator.append((char) randomLimitedInts);
				lastName = secondNameGenerator.toString();
				lastName = lastName.substring(0, 1).toUpperCase() + lastName.substring(1).toLowerCase();

			}
			String generatedName = firstName + " " + lastName;
			classList.add(generatedName);
		}

		System.out.println(classList);
		Collections.sort(classList, (o1,o2) ->  o1.substring(o1.lastIndexOf(" ")+1)
				.compareTo(o2.substring(o2.lastIndexOf(" ")+1)));

		int firstNameCount;
		for(String name:classList) {
			firstNameCount=0;
			int i = name.indexOf(' ');
			String firstWord = name.substring(0, i);
			String rest = name.substring(i);
			for(String name1:classList) {
				if(name1.contains(firstWord)) {
					firstNameCount++;
				}
			}if(firstNameCount>1) {
				System.out.println(firstWord+" "+rest);
			}else {
				System.out.println(firstWord);
			}
		}
	}

}
