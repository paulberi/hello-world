package randomExcercisesAndReminderExcercises;

public class SortingPrimeNumbers {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		primeNumbers();
		prime();

	}
	static void primeNumbers(){
		int num = 0;
		int i = 0;
		while(num<50) {
			boolean isPrime=true;
			for (i=2; i<num;i++) {
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
	static void prime() {
		int num=0;
		int i=0;
		
		while(num<100) {
			boolean isPrime=true;
			for (i=2; i<num; i++){
				if(num%i==0) {
					isPrime=false;
					break;
				}
			}
			if(isPrime && num!=0 && num!=1) {
				System.out.println(num + " is a prime number");
			}
			num++;
		}
	}
}
