package tutorialsFromAlex;

public class EarPhones {
	String charge ="micro Usb";
	String [] controls= {"power","volume","skip","play/pause"};
	String colour ="red/black";
	static boolean power =false;
	static int volume=0;
	static boolean mic =false;
	
	public static void powerOn() {
		power=true;
	}
	public static void powerOff() {
		power=false;
	}
	public static void volumeUp() {
		volume++;
	}
	public static void volumeDown() {
		volume--;
	}
	public static void micOn() {
		mic=true;
	}
	public static void micOff() {
		mic= false;
	}
	

}
