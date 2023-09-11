package Hello;

public class PrintingArray {
	
	public static void main(String args[]) {

		int [] numbers=new int[] {1,2,3};
		for(int i=0;i<numbers.length;i++) {
			System.out.println(numbers[i]);
		}
		for(int i=1;i<numbers.length;i++) {
			System.out.println(numbers[i]);
		}
		for(int i=1;i<=numbers.length-1;i++) {
			System.out.println(numbers[i]);
		}
	}
}
