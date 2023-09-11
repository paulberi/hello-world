package kattis;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ReplacingWordsWithNumbers {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// Generating words to assign numbers later
		int leftLimit = 97;
		int rightLimit = 122;
		Random random = new Random();
		List<String> stringArray = new ArrayList<>();
		List<Integer> numberArray=new ArrayList<>();

		for (int i = 1; i <= 1000; i++) {
			StringBuilder stringGenerator = new StringBuilder(random.nextInt(10));
			for (int j = 0; j < random.nextInt(10); j++) {
				int randomLimitedInt = leftLimit + random.nextInt(rightLimit - leftLimit + 1);
				stringGenerator.append((char) randomLimitedInt);
				String generatedWord = stringGenerator.toString();
				stringArray.add(generatedWord);
			}
		}
		//Generating numbers to assign to the words, assigned some negatives just to spread the range as per the requirements
		for(int j=0;j<stringArray.size(); j++) {
			for(int i=0; i<=1000; i++) {
				if(j==i) {
					if(i%2!=0) {
						numberArray.add(-(i));
					}else {
						numberArray.add(i);
					}
				}
			}
		}
		//creating definitions
		for(String word:stringArray) {
			for(Integer num:numberArray) {
				if(numberArray.indexOf(num)==stringArray.indexOf(word)) {
					System.out.println("def "+word+" "+num);
				}
			}
		}
		//calc command
		List<String> calc = new ArrayList<>();
		List<String> results=new ArrayList<>();
		for(int i=0; i<stringArray.size()/3;i++) {
			int index=random.nextInt(1000+1);
			int index2=random.nextInt(1000+1);
			int index3=random.nextInt(1000+1);
			String firstIndex=stringArray.get(index);
			String secondIndex=stringArray.get(index2);
			String thirdIndex=stringArray.get(index3);
			String calc1=("calc "+firstIndex+" + "+secondIndex+" - "+thirdIndex+" =");
			String calc2=("calc "+firstIndex+" + "+secondIndex+" =");
			String calc3=("calc "+secondIndex+" - "+thirdIndex+" =");
			calc.add(calc1);
			calc.add(calc2);
			calc.add(calc3);
			int answer1 =numberArray.get(index)+numberArray.get(index2)-numberArray.get(index3);
			int answer2 =numberArray.get(index)+numberArray.get(index2);
			int answer3 =numberArray.get(index2)-numberArray.get(index3);
		//	System.out.println("answers "+answer1+" "+answer2+" "+answer3);
			int index4 = 0;
			int index5 = 0;
			int index6 = 0;
			for(Integer num:numberArray) {
				if(answer1==num) {
					index4=numberArray.indexOf(num);
				}if(answer2==num) {
					index5=numberArray.indexOf(num);
				}if(answer3==num) {
					index6=numberArray.indexOf(num);
				}
			}
			//results, havent handled the unknowns yet
			String answer4=stringArray.get(index4);
			String answer5=stringArray.get(index5);
			String answer6=stringArray.get(index6);
			String result1=(firstIndex+" + "+secondIndex+" - "+thirdIndex+" ="+answer4);
			String result2=(firstIndex+" + "+secondIndex+" ="+answer5);
			String result3=(secondIndex+" - "+thirdIndex+" ="+answer6);
			results.add(result1);
			results.add(result2);
			results.add(result3);
		}
		for(String cal:calc) {
			System.out.println(cal);
		}
		for(String result:results) {
			System.out.println(result);
		}
	}

}
