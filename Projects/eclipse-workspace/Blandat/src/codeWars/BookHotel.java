package codeWars;

import java.awt.List;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class BookHotel {
	static ArrayList<Integer> choices = new ArrayList<Integer>();
	static int[] rumTypes = { 100, 200, 300 };
	static int[][] rums = { { 101, 102, 103, 104 }, { 201, 202, 203, 204 }, { 301, 302, 303, 304 } };
	static Scanner input = new Scanner(System.in);
	/*
	 * choices is a container list to store user choices along the booking process
	 * RumTypes is an array than contanins the different rum categories standard
	 * rums represented by(100), Deluxe (200) and Källarsviten(300) rums is an rray
	 * that shows the different rums that are available for each class scanner för
	 * att tar in information från användaren
	 **/

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		boolean bokning=true;
		while(bokning) {
			System.out.println("välkommen till Africket online services");
			System.out.println("Vil du boka eller avboka en rum, tryck");
			System.out.println("1 För att boka: \n2 För att avboka:");
			int bokAvBok = bokaEllerAv();

			if (bokAvBok == 1) {

				int typVal = rumTyp(rumTypes);
				System.out.println(typVal);
				boolean yeah=true;
				int rum = 0;
				if (typVal == 100) {
					rum=rum100(rums);
					while(choices.contains(rum)) {
						System.out.println("rummet är upptagen, prova annan rum");
						for(int i=0; i<choices.size();i++) {
							System.out.println("choices has "+choices.get(i));
						}
						rum=rum100(rums);
					}
				}
				else if (typVal == 200) {
					rum = rum200(rums);
					while(choices.contains(rum)) {
						System.out.println("rummet är upptagen, prova annan rum");
						for(int i=0; i<choices.size();i++) {
							System.out.println("choices has "+choices.get(i));
						}
						rum = rum200(rums);
					}
					
				}
				else if (typVal == 300) {
					rum=rum300(rums);
					while(choices.contains(rum)) {
						System.out.println("rummet är upptagen, prova annan rum");
						for(int i=0; i<choices.size();i++) {
							System.out.println("choices has "+choices.get(i));
						}
						rum=rum300(rums);
					}
				}
				System.out.println("Gratis, "+rum+ " är nu reserverad för dig.");

			}else if(bokAvBok==2) {
				avboka(rums);
			}
			System.out.println("Om du är klart skriv: 0 och 1 för att fortsätta");
			int avslutning=input.nextInt();
			if(avslutning==0) {
				bokning=false;
			}if(avslutning==1) {
				bokning=true;
			}
			
		}

	}
	private static void avboka(int[][] rums2) {
		// TODO Auto-generated method stub
		/*metod för att avboka.
		 * */
		System.out.println("vad är din rum nummer");
		boolean yeah=true;
		while(yeah) {
			try {
				int rumNummer=input.nextInt();
			}catch(InputMismatchException e) {
				System.err.println("mata in bara tal");
				input.nextLine();
				continue;
			}
			System.out.println("Tack för din tid med oss, vi har tagit emot din ansökant och ska\n"
					+ "avboka dig. Vi skickar en email när vi är klara. Ha det så bra");
		}
	}
	private static int rum300(int[][] rums2) {
		// TODO Auto-generated method stub
		/*
		 * metoden för att välja bland källarsviten rum samt tildelar de och märka som upptagen*/
		System.out.println("Det finns fyra rum i din kagori kvar att boka.");
		System.out.println("301,302,303,304");
		System.out.println("vilken vill du ha");
		int rum = 0;
		int position = 0;
		boolean yeah = true;
		while (yeah) {
			try {
				rum=input.nextInt();

			} catch (InputMismatchException e) {
				System.err.println("Du kan bara skrivaheltal");
				input.nextLine();
				continue;
			}
			if (rum == 301 || rum == 302 || rum == 303 || rum == 304) {
				System.out.println("Excellent val");
			} else {
				System.out.println(" fel val, prova igen");
				continue;
			}
			yeah = false;
		}
		if (rum == 301) {
			rums[2][0] = position;
			choices.add(position);
		} else if (rum == 302) {
			rums[2][1] = position;
			choices.add(position);
		} else if (rum == 303) {
			rums[2][2] = position;
			choices.add(position);
		} else if (rum == 304) {
			rums[2][3] = position;
			choices.add(position);
		}
		return rum;
		
	}
	private static int rum200(int[][] rums2) {
		// TODO Auto-generated method stub
		/*
		 * metoden för att välja bland deluxe rum samt tildelar de och märka som upptagen*/
		System.out.println("Det finns fyra rum i din kagori kvar att boka.");
		System.out.println("201,202,203,204");
		System.out.println("vilken vill du ha");
		int rum = 0;
		int position = 0;
		boolean yeah = true;
		while (yeah) {
			try {
				rum=input.nextInt();

			} catch (InputMismatchException e) {
				System.err.println("Du kan bara skrivaheltal");
				input.nextLine();
				continue;
			}
			if (rum == 201 || rum == 202 || rum == 203 || rum == 204) {
				System.out.println("Excellent val");
			} else {
				System.out.println(" fel val, prova igen");
				continue;
			}
			yeah = false;
		}
		if (rum == 201) {
			rums[1][0] = position;
			choices.add(position);
		} else if (rum == 202) {
			rums[1][1] = position;
			choices.add(position);
		} else if (rum == 203) {
			rums[1][2] = position;
			choices.add(position);
		} else if (rum == 204) {
			rums[1][3] = position;
			choices.add(position);
		}
		return rum;
		
	}
	private static int rum100(int[][] rums2) {
		// TODO Auto-generated method stub
		/*
		 * metoden för att välja bland standard rum samt tildelar de och märka som upptagen*/
		System.out.println("Det finns fyra rum i din kagori kvar att boka.");
		System.out.println("101,102,103,104");
		System.out.println("vilken vill du ha");
		int rum = 0;
		int position = 0;
		boolean yeah = true;
		while (yeah) {
			try {
				rum=input.nextInt();

			} catch (InputMismatchException e) {
				System.err.println("Du kan bara skrivaheltal");
				input.nextLine();
				continue;
			}
			if (rum == 101 || rum == 102 || rum == 103 || rum == 104) {
				System.out.println("Excellent val");
			} else {
				System.out.println(" fel val, prova igen");
				continue;
			}
			yeah = false;
		}
		if (rum == 101) {
			rums[0][0] = rum;
			choices.add(rum);
		} else if (rum == 102) {
			rums[0][1] = rum;
			choices.add(rum);
		} else if (rum == 103) {
			rums[0][2] = rum;
			choices.add(rum);
		} else if (rum == 104) {
			rums[0][3] = rum;
			choices.add(rum);
		}
		return rum;

	}
	private static int rumTyp(int[] rumTypes2) {
		// TODO Auto-generated method stub
		/*metod för att välja vilken typ av rum kunden vill boka
		 * */
		System.out.println("Vi har tre klasser av bokbar rum");
		System.out.println("100 För standard rum:\n200 För deluxe rum:\n300 För källarsviten:");
		
		int typNummer = 0;
		boolean yeah=true;
		while(yeah) {
			try {
				typNummer=input.nextInt();
			
			}catch(InputMismatchException e) {
			System.err.println("bara heltal");
			input.nextLine();
			continue;
			
			}
			if(typNummer==100 || typNummer==200 ||typNummer==300) {
				System.out.println("Good choice");
				
			}else {
				System.out.println("fel val, prova igen");
				continue;
			}
			yeah=false;
			
			
		}
		/*här tildelar vi kundens val til listen choices.
		 * */
		if(typNummer==100) {
			rumTypes2[0]=typNummer;
			choices.add(typNummer);
		}else if(typNummer==200) {
			rumTypes2[1]=typNummer;
			choices.add(typNummer);
		}else if(typNummer==300) {
			rumTypes2[2]=typNummer;
			choices.add(typNummer);
		}
		
		return typNummer;
		
		
		
	}

	private static int bokaEllerAv() {
		// TODO Auto-generated method stub
		/*
		 * här får kunden välja om de vill boka eller avboka
		 * */
		int bokAvBok = 0;
		boolean boka = true;
		while (boka) {
			try {
				bokAvBok = input.nextInt();
			} catch (InputMismatchException e) {
				System.err.println("1 För att boka och 2 för att avboka");
				input.nextLine();
				continue;
			}
			if (bokAvBok < 1 || bokAvBok > 2) {
				System.out.println("Fel vid inmatning försök igen");
				continue;
			}
			boka = false;

		}
		return bokAvBok;

	}

}
