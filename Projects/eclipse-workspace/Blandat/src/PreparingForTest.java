import java.awt.Point;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;
import java.util.Scanner;

import javax.swing.text.DateFormatter;

public class PreparingForTest {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		// borstaTander();

		// choosingNumberThreeTimesAndComparingItToComputerChoice3Times();

		// countYearsAndDaysFromMinutes();

		// calculatingMidPointsOfColumnsOfNumbers();

		// programToTakeNumberAndDetermineifPositiveOrNot();

		// scoreGradeSwitchSats();

		// userChooseProfessionAndGetInforOnTheProfession();

		// programToCollectDataOnNameAndSexAndGiveAtitle();

		// stringsAndSubstrings();

		// translatingGradesToScores();

		// calculatingAgesOFusers();

		// countingNumberOfDaysInAMonth();

		 stenSaxPase();

		// fromIncesToCentimetres();

		//celsiusToFahrenheitAndViceVersa();
		
		//fileWriterAndFileReader();
		
		//primeNumbers();
		
		//reversingArrays();
		
		//sortingArrays();
		
		//bokaHotel(); 

	}

	private static void bokaHotel() {
		// TODO Auto-generated method stub
		
	}

	private static void sortingArrays() {
		// TODO Auto-generated method stub
		int[] sort= {2,9,5,4,8,1,6};
		for (int i=0; i<sort.length; i++) {
			int index=i;
			for(int j=i+1; j<sort.length; j++) {
				if(sort[j]<sort[index]) {
					index=j;
				}
			}
			int min=sort[index];
			sort[index]=sort[i];
			sort[i]=min;
		}
		for(int item:sort) {
			System.out.println(item);
		}
		//short form
		//Arrays.sort(sort);
		//System.out.println(Arrays.toString(sort));//short form;
		
	}

	private static void reversingArrays() {
		// TODO Auto-generated method stub
		Scanner in=new Scanner(System.in);
		System.out.println("hur lång är Arrayen");
		int n= in.nextInt();
		int[] list=new int[n];
		
		System.out.println("skriv ner siffrorna för arrayen list");
		
		for(int i=0;i<n;i++) {
			list[i]=in.nextInt();
		}
		int[]result=reverse(list);
		
		for(int j:result) {
			System.out.print(j+",");
		}
	}

	private static int[] reverse(int[] list) {
		// TODO Auto-generated method stub
		int [] result=new int[list.length];
		for(int i=0,j=result.length-1;i<list.length; i++,j--) {
			result[j]=list[i];
		}
		return result;
	}

	private static void primeNumbers() {
		// TODO Auto-generated method stub
		
		int num = 0;
		int i = 0;
		int count = 0;
		while(num<50) {
			boolean isPrime=true;
			for (i=2; i<num/2;i++) {
				if(num%i==0) {
					isPrime=false;
					break;
				}
			}
			if(isPrime &&num!=0&& num!=1) {
				System.out.println(num+" ");
			}
			num++;
		}
		
		
	}

	private static void fileWriterAndFileReader() {
		// TODO Auto-generated method stub
		
		String origin ="/Users/paulberinyuylukong/Desktop/paul1.txt";
		String temp ="/Users/paulberinyuylukong/Desktop/temp.txt";
		boolean rowEdited=false;
		int rowNumber;
		int rowCounter = 0;
		String newLine;
		//Scanner in=new Scanner(System.in);
		int i;
		try {
			Scanner in=new Scanner(System.in);
			FileWriter fw= new FileWriter(temp);
			FileReader fr=new FileReader(origin);
			System.out.println("write down the row number you want to edit");
			rowNumber=in.nextInt();
			
			if(rowNumber==1) {
				System.out.println("write the change");
				newLine=in.next();//nextLine not working check why later
			}else {
				System.out.println("write the replacement");
				newLine='\n'+in.next();
			}
			while((i=fr.read())!=-1) {
				if(i=='\n') {
					rowCounter++;
				}
				if(rowCounter==rowNumber) {
					while(!rowEdited) {
						fw.write(newLine);
						rowEdited=true;
					}
					continue;
				}
				else {
					fw.write(((char)i));
				}
				
			}
			fr.close();
			fw.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		disposingTempFile(temp,origin);
		
	}

	private static void disposingTempFile(String temp,String origin) {
		// TODO Auto-generated method stub
		
		origin ="/Users/paulberinyuylukong/Desktop/paul1.txt";
		temp ="/Users/paulberinyuylukong/Desktop/temp.txt";
		
		try {
			FileWriter fw=new FileWriter(origin);
			FileReader fr=new FileReader(temp);
			int i;
			while((i=fr.read())!=-1) {
				fw.write(((char)i));
			}
			fr.close();
			fw.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File temp1=new File(temp);
		temp1.deleteOnExit();
		
	}

	private static void celsiusToFahrenheitAndViceVersa() {
		// TODO Auto-generated method stub

		double i, j , k, l = 0;
		for (i = 0, k = 20; i <= 100 || k <= 270; i = i + 2, k = k + 5) {
			j = celsius(i);
			double j1=Math.round(j*1000)/1000.0;
			if (i > 2 && i < 98) {
				if (i == 4) {
					System.out.println("\t...");
					
				}
				continue;
			}
			l = fahrenheit(k);
			double l1=Math.round(l*1000)/1000.0;
			if(k>25 && k<265) {
				if(k==30) {
					System.out.println("\t...");
					
				}
				continue;
			}
			System.out.println("\t" + i + "\t\t" + j1 + "\t|\t" + k + "\t\t\t" + l1);

		}

	}

	private static double fahrenheit(double fahrenheit) {
		// TODO Auto-generated method stub
		// method to calculate celsius for the method above
		double celsius = (fahrenheit-32)/1.8;
		return celsius;
	}

	private static double celsius(double celsius) {
		// TODO Auto-generated method stub
		// method to calculate celsius to fahren for the method 2 steps above

		double fahrenheit = celsius*1.8+32;;
		return fahrenheit;
	}

	private static void fromIncesToCentimetres() {
		// TODO Auto-generated method stub
		double inches = 1;
		for (double i = 1; i <= 10; i++) {
			double j = i * 2.54;
			if (i >= 3 && i <= 8) {
				if (i == 3) {
					System.out.println("\t...");
				}

				continue;

			}
			System.out.println("\t" + i + "\t\t" + j);
		}

	}

	private static void stenSaxPase() {
		// TODO Auto-generated method stub

		Scanner input = new Scanner(System.in);

		Random in = new Random();

		int rounds = 0;
		while (rounds < 3) {
			System.out.println("välja \n1 för sten\n2 för sax\n3 för påse");
			int player = input.nextInt();

			// för cpu 1 för sten, 2 för sax och 3 för påse
			int cpu = in.nextInt(3) + 1;
			System.out.println("cpu choice is " + cpu);

			if (cpu == player) {
				System.out.println("its a draw play again.");
			}
			if (cpu == 1 && player == 2) {
				System.out.println("cpu wins");
			}
			if (cpu == 1 && player == 3) {
				System.out.println("player wins");
			}
			if (cpu == 2 && player == 1) {
				System.out.println("player wins");
			}
			if (cpu == 2 && player == 3) {
				System.out.println("cpu wins");
			}
			if (cpu == 3 && player == 1) {
				System.out.println("cpu wins");
			}
			if (cpu == 3 && player == 2) {
				System.out.println("player wins");
			}
			rounds++;
		}
		System.out.println("you have played " + rounds + " times, game over");
	}

	private static void countingNumberOfDaysInAMonth() {
		// TODO Auto-generated method stub
		Scanner in = new Scanner(System.in);
		String year = in.next();
		String month = in.next();
		String day = in.next();

		Calendar c = Calendar.getInstance();

		c.set(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day));

		String dayOfWeek = getDayOfWeek(c.get(Calendar.DAY_OF_WEEK));
		System.out.println(dayOfWeek.toUpperCase());

	}

	private static String getDayOfWeek(int value) {
		// TODO Auto-generated method stub
		String day = null;
		switch (value) {
		case 1:
			day = "sunday";
			break;
		case 2:
			day = "monday";
			break;
		case 3:
			day = "tuesday";
			break;
		case 4:
			day = "wednesday";
			break;
		case 5:
			day = "thursday";
			break;
		case 6:
			day = "friday";
			break;
		case 7:
			day = "saturday";
			break;

		}
		return day;
	}

	private static void calculatingAgesOFusers() throws IOException {
		// TODO Auto-generated method stub
		System.out.println("Nu ska vi räkna ut hur gamalt du är");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("year:");
		String year = br.readLine();
		System.out.println("month");
		String month = br.readLine();
		System.out.println("day");
		String day = br.readLine();
		int years = Integer.parseInt(year);
		int months = Integer.parseInt(month);
		int days = Integer.parseInt(day);

		System.out.println("you date of birth is " + day + " " + month + " " + year);

		LocalDate ld = LocalDate.now();
		DateTimeFormatter ldf = DateTimeFormatter.ofPattern("yyyy,MM,dd");
		LocalDate birthday = LocalDate.of(years, months, days);

		Period age = calcutaion(birthday, ld);
		System.out.println("You are " + age + "years old");
	}

	private static Period calcutaion(LocalDate birthday, LocalDate ld) {
		// TODO Auto-generated method stub
		Period age = Period.between(birthday, ld);
		return age;
	}

	private static void translatingGradesToScores() {
		// TODO Auto-generated method stub
		Scanner input = new Scanner(System.in);
		System.out.println("what is your grade");
		String grade = input.next();

		switch (grade) {
		case "A":
			System.out.println("you have a score of 5");
			break;
		case "B":
			System.out.println("you have a score of 4");
			break;
		case "C":
			System.out.println("you have a score of 3");
			break;
		case "D":
			System.out.println("you have a score of 2");
			break;
		case "F":
			System.out.println("you have a failed");
			break;
		}
	}

	private static void stringsAndSubstrings() {
		// TODO Auto-generated method stub

		String str = "Hej det här är en lång sträng";

		String strSub = str.substring(18, 22);
		int ln = strSub.length();
		System.out.println(strSub + ": has " + ln + " letters");
	}

	private static void programToCollectDataOnNameAndSexAndGiveAtitle() throws IOException {
		// TODO Auto-generated method stub
		System.out.println("Hej, hur mår du idag, vad är dit namn och kön:");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Namn:");
		String name = br.readLine();
		System.out.println("Kön");
		String user = br.readLine();

		boolean results = determineGender(user);
		System.out.println(results);
		if (results) {
			System.out.println("Mr " + name);
		} else {
			System.out.println(("Mrs " + name));
		}
	}

	private static boolean determineGender(String user) {

		// TODO Auto-generated method stub
		boolean isMale = true;

		/*
		 * if (user.equals("man")) { isMale=true; } else if(user.equals("kvinnan")) {
		 * isMale=false; } return isMale;
		 */
		return isMale = (user.equals("man")) ? true : false; // ternery as alternative method and works well

	}

	private static void userChooseProfessionAndGetInforOnTheProfession() throws IOException {
		// TODO Auto-generated method stub
		BufferedReader bw = new BufferedReader(new InputStreamReader(System.in));
		System.out.println(
				"Choose a profession between \n1 Teacher \n2 Bussdriver \n3 Programmer \n4 Cook \n5 Physician");
		String profession = bw.readLine();

		switch (profession) {
		case "Teachers":
			System.out.println("Teachers are the base of any society, they made us all");
			break;
		case "Bussdriver":
			System.out.println("Basic societal need. Simple but there is no life without collective transportation");
			break;
		case "Programmer":
			System.out.println("Priest of the secular world, they lay down their lives so you can have one");
			break;
		case "Cook":
			System.out.println("Good food is like good sex, it makes life even more worthy");
			break;
		case "Physician":
			System.out.println(
					"Self proclaimed saviours but what their understanding does to our natural abilities is a shcame");
			break;
		default:
			System.out.println("Your choice is invalid");
			break;
		}
	}

	private static void scoreGradeSwitchSats() {
		// TODO Auto-generated method stub
		System.out.println("program to allocate grades to exams scores");
		Scanner input = new Scanner(System.in);
		System.out.println("what is your exam score:");
		int score = input.nextInt();

		switch (score / 10) {

		case 9:
			System.out.println("you have an A");
			break;
		case 8:
			System.out.println("you have an B");
			break;
		case 7:
			System.out.println("you have an C");
			break;
		case 6:
			System.out.println("you have an D");
			break;
		default:
			System.out.println("you have an F");
			break;
		}

	}

	private static void programToTakeNumberAndDetermineifPositiveOrNot() {
		// TODO Auto-generated method stub
		Scanner input = new Scanner(System.in);
		System.out.println("write down a number either + or-:");
		int user = input.nextInt();
		if (user < 0) {
			System.out.println("its a negative number");
		} else {
			System.out.println("its a positive number");
		}

	}

	private static void calculatingMidPointsOfColumnsOfNumbers() {
		// TODO Auto-generated method stub
		System.out.println("\t A \t\t B\t\t Middle Point");
		DecimalFormat df = new DecimalFormat("#.###");
		df.setRoundingMode(RoundingMode.HALF_UP);
		System.out
				.format("\t(0,0)\t\t(2,1)\t\t  " + "(" + Math.floor((0 + 2) / 2) + "," + Math.floor((0 + 1) / 2) + ")");
		System.out.format(
				"\n\t(1,4)\t\t(4,2)\t\t  " + "(" + Math.floor((1 + 4) / 2) + "," + Math.floor((4 + 2) / 2) + ")");
		System.out.format(
				"\n\t(2,7)\t\t(6,3)\t\t  " + "(" + Math.floor((2 + 6) / 2) + "," + Math.floor((7 + 3) / 2) + ")");
		System.out.format(
				"\n\t(3,9)\t\t(10,5)\t\t  " + "(" + Math.floor((3 + 10) / 2) + "," + Math.floor((9 + 5) / 2) + ")");
		System.out.format(
				"\n\t(4,11)\t\t(12,7)\t\t  " + "(" + Math.floor((4 + 12) / 2) + "," + Math.floor((+7) / 2) + ")");
		System.out
				.format("\n\t(0,0)\t\t(2,1)\t\t  " + "(" + (df.format((0 + 2) / 2)) + "," + (df.format((1) / 2)) + ")");// line
																														// is
																														// just
																														// to
																														// try
																														// to
																														// fix
																														// the
																														// decimal
																														// thing

	}

	private static void countYearsAndDaysFromMinutes() {
		// TODO Auto-generated method stub
		System.out.println("Mata in antal minuter för att kolla hur många år och dagar den är");
		Scanner input = new Scanner(System.in);
		int minutes = input.nextInt();

		double hours = minutes / 60;

		double days = hours / 24;

		double years = days / 365;

		System.out.println(minutes + "minutes is equivalent to " + days + " days which is " + years + " years");

	}

	private static void choosingNumberThreeTimesAndComparingItToComputerChoice3Times() {
		// TODO Auto-generated method stub
		Random input = new Random();
		Scanner in = new Scanner(System.in);

		int choice = 0;
		while (choice < 3) {
			System.out.println("choose a number between 1 and 100");
			int userInput = in.nextInt();
			int cpuInput = input.nextInt(100) + 1;
			System.out.println("cpu " + cpuInput);

			if (userInput == cpuInput) {
				System.out.println("bra jobbat");

			} else {
				System.out.println("du är dumt");
				// System.out.println("choose a number between 1 and 100");
				// userInput=in.nextInt();

			}

			choice++;

		}
		System.out.println(choice + "too bad");

	}

	private static void borstaTander() {
		// TODO Auto-generated method stub
		System.out.println("Det är dags att borsta tänderna");
		System.out.println("läng tandcreme på tandborsten");
		int cleanedTeeth = 0;
		while (cleanedTeeth <= 32) {
			System.out.println("cleaned teeth =" + cleanedTeeth);
			cleanedTeeth++;
		}
		System.out.println(
				"Congratulations your teeth are clean now\nkeep the tooth brush wash the sink and leave the toilet");

	}

}
