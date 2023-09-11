package tutorialsFromAlex;

public class Car {
	
	int wheels= 4;
	String type="fwd SUV";
	String reg="personal transport";
	int passengers=5;
	
	String [] actionables= {"gear","brakes", "trottle","horn","stereo"};
	static int gear=0;
	static int speed=0;
	static boolean horn =false;
	static boolean music=true;
	
	public static void gears() {
		gear++;
	}
	public static void brakes() {
		speed=gear/10;
	}
	public static void trottle() {
		speed=gear*10;
	}
	public static void stereo() {
		music=true;
	}
	
}
