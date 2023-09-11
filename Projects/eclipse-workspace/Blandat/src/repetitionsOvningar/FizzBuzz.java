package repetitionsOvningar;

public class FizzBuzz {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		for (int i=1; i<=100; i++) {
			if(i%3==0&&i%5==0) {
				System.out.println("FizzBuzz");
				continue;
			}
			if(i%5==0) {
				System.out.println("Buzz");
				continue;
			}
			if(i%3==0) {
				System.out.println("Fizz");
				continue;
			}else{
				System.out.println(i);
			}
		}
		fizzbuzz();

	}
	public static void fizzbuzz() {
		int x=0;
		while(x<=100) {
			if(x%3==0) {
				System.out.println("fizz");
			}
			else if(x%5==0) {
				System.out.println("buzz");
			}
			else if(x%3==0 && x%5==0 ) {
				System.out.println("fizzbuzz");
			}
			else {
				System.out.println(x);
			}
			x++;
		}
		System.out.println("end of game");
	}

}
