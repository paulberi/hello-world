import java.util.Scanner;

public class SelektionerSlides {

	public static void main(String args[]) {
		Scanner input=new Scanner(System.in);
		/*int score=input.nextInt();
		if (score>=90) 
			System.out.println("A");
			else if(score>=80)
				System.out.println("B");
			else if(score>=70)
				System.out.println("C");
			else if(score>=60)
				System.out.println("D");
			else 
				System.out.println("F");
		
		
		
		System.out.println("Choose a profession from the list below");
		String builder="Very hard working people";
		String lawyer="Kind of tricky people";
		String teacher="King makers";
		String systemDeveloper="redefining society";
		String Plumber = "Only God knows";
		System.out.println("1 builder");
		System.out.println("2 lawyer");
		System.out.println("3 teacher");
		System.out.println("4 systemdeveloper");
		System.out.println("5 Plumber");
		
		
		String profession = input.next();
		switch(profession) {
		case "builder":System.out.println(builder);break;
		case "lawyer":System.out.println(lawyer);break;
		case "teacher":System.out.println(lawyer);break;
		case "systemdeveloper":System.out.println(lawyer);break;
		case "Plumber":System.out.println(lawyer);break;
		
		System.out.println("What is your name");
		String name=input.next();
		System.out.println("What is your gender");
		
		String gender=input.next();
		
		String female = "female", girl = "girl";
		
		/*if(gender.equals(female) || gender.equals(girl) ) {
			
			System.out.println("Mrs " + name);
			
		}else {
			System.out.println("Mr " + name);
	
		}*/
		/*System.out.print(myMethod(gender)+" "+ name);
		
	}

	private static String myMethod(String sex) {
		// TODO Auto-generated method stub
		String female = "female", girl = "girl";
		if(sex.equals(female) || sex.equals(girl) ) {
			return "Mrs";
		}else {
			return "Mr";
		}
		if (sex.equals(female)||sex.equals(girl))?(Mr):(Mrs) != null;
		
		
		String learning="Hej det här är en lång sträng";
		String learning2=learning.substring(18,22);
		System.out.println(learning2 +" och det innehåller "+ learning2.length()+" bokstaver");*/
		
		System.out.println("Skriv din betyg");
		String betyg=input.next();
		switch(betyg) {
		case "A":System.out.println("Din score är 5");break;
		case "B":System.out.println("Din score är 4");break;
		case "C":System.out.println("Din score är 3");break;
		case "D":System.out.println("Din score är 2");break;
		case "F":System.out.println("Din score är underkänt");break;
		}
	}

}
