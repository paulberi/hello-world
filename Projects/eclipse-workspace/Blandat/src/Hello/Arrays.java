package Hello;

public class Arrays {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String[] list= {"hello", "hi", "yess","No"};

		for (int i=0; i< list.length; i++){
		//	System.out.println(list[list.length-i-1]);
		}
		//listPrimeNumbers();

		reversingString();
		}

	public static void listPrimeNumbers(){

		//Prime numbers are numbers that are divisible by only one and itself
		// Test case num/i>1 !% 0
		int x=0;
		int num = 0;
		for( int i = 1; i<=100; i++){
			x=0;
			for (int j=2; j<=(i)/2; j++){
					if(i%j==0){
						x++;
					}
			}
			if(x==0 && i!=1){
				System.out.println(i);
				num++;
			}
		}
		System.out.println("num is "+ num);
	}

	public static void reversingString(){

		String target="slrig dna syob";

		String[] y ={"girls","and","boys"};

		char[] toReverse= target.toCharArray();

		System.out.println(toReverse);
		char[] reversed = new char[toReverse.length];

		for( int i=toReverse.length-1; i>0; i--){
			for (int j=0; j< reversed.length; j++){
				reversed[i]=toReverse[Math.abs(j-i)];
			}
		}
		//System.out.println(reversed);
		//reverse(y);
		reverse(toReverse);

	}
	private static String[] reverse(String[] list) {
		// TODO Auto-generated method stub
		String [] result=new String[list.length];
		for(int i=0,j=result.length-1;i<list.length; i++,j--) {
			result[j] = list[i];

			System.out.println(result[0] + " " + result[1] + " " + result[2]);
		}
		return result;
	}

	private static char[] reverse(char[] list) {
		// TODO Auto-generated method stub
		char [] result=new char[list.length];
		for(int i=0,j=result.length-1;i<list.length; i++,j--) {
			result[i] = list[j];

			System.out.println(result[i]);
		}
		return result;
	}
}
