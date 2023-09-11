import java.util.Scanner;

public class Loop {
	public static void main(String[] args) {
		Scanner in=new Scanner(System.in);
		for(int i=0; i<5;i--) {
			String name=in.nextLine();
			System.out.println(name);
		}
	}

}
