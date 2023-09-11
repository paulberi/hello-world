import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.Format;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Scanner;

public class HexaToDecimal {

	public static void main(String args[])  {
		/*using integer.toString option
		Integer hexaDecimal =25;
		String DecimalNumber =Integer.toString(hexaDecimal,10);
		System.out.println(hexaDecimal + " in bas 10:"+DecimalNumber);
		it didnt work but will come back to it
		BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));
		System.out.println("write a number");
		int number=Integer.parseInt(reader.readLine(),11);
		System.out.println("The decimal equivalent is "+number);

		Övningar
		
		
		Scanner input=new Scanner(System.in);
		
		System.out.println("Mata in en siffra:");
		int user=input.nextInt();
		String revert=Integer.toString(user);
		System.out.println(user+ " is equivalent to "+ revert+ " in stringform");
	public static void main(String args[]) {
		System.out.println("Enter the score:");
		Scanner input=new Scanner(System.in);
		int score=input.nextInt();
		int score2;
		switch(score/10) {
		case 10: System.out.println("print A");break;
		case 9: System.out.println("The grade is A");break;
		case 8: System.out.println("The grade is B");break;
		case 7: System.out.println("The grade is C");break;
		case 6: System.out.println("The grade is D");break;
		default: System.out.println("The grade is F");break;
		
		}
		int people =0;
		Scanner input= new Scanner(System.in);
		
	    while(people<3) {
	    	System.out.println("Välja en yrket från listan");
		    System.out.println("1 Builder \n2 Developer \n3 teacher \n4 baker \n5 minner");
		    int choice=input.nextInt();
		
		    switch(choice) {
		    case 1:System.out.println("builders are tough");break;
		    case 2:System.out.println("rulers of the world");break;
		    case 3:System.out.println("king makers");break;
		    case 4:System.out.println("very important");break;
		    case 5:System.out.println("killing the planet");break;
		    default:
		    people=people+1;
		
		}
	    }
		
		
		Scanner input=new Scanner(System.in);
		System.out.println("Mata in ett betyg A-F");
		String grade= input.next();
		switch(grade) {
			case "A":System.out.println("5");break;
			case "B":System.out.println("4");break;
			case "C":System.out.println("3");break;
			case "D":System.out.println("2");break;
			case "F":System.out.println("Underkänt");
		}
	*/
		Scanner input= new Scanner(System.in);
		/*LocalDate dagensDatum= LocalDate.now();
		System.out.println(dagensDatum);
		DateTimeFormatter formaterare=DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
		String idag=dagensDatum.format(formaterare);
		
		System.out.println("Mata in födelsdatum");
		
		String DateOfBirth =input.nextLine();
		LocalDate DateOfBirth2=LocalDate.parse(DateOfBirth);
		
		Period age=Period.between(DateOfBirth, idag);
		
		
		*/
		
		
		
		System.out.println("What is your date of Birth");
		
		System.out.println("YEAR");
		int dateOfB =input.nextInt();
		System.out.println("MONTH");
		int monthOfB =input.nextInt();
		System.out.println("DAY");
		int dayOfB =input.nextInt();
		
		
		LocalDate Birth=LocalDate.of(dateOfB, monthOfB, dayOfB);
		LocalDate SchoolStart=LocalDate.of(2009, 9, 23);
		Period age= Period.between(Birth, SchoolStart);
		//DateTimeFormatter age1=DateTimeFormatter.ofPattern("yyyy-MM-DD");
		//String realAge=age1.format(age);
		System.out.println("I am "+ age + " years old");
		
		
		
	}

}

