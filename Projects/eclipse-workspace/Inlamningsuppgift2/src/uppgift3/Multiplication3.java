package uppgift3;

public class Multiplication3 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
	
		int x = 8;
		int Col = 1;
		for (int i=x; i>0; i--) {
			for(int j=1; j<=i*2; j++) {
				System.out.print(" ");
			}
			int value=1;
			for (int k = 1 ; k<=Col-1; k++) {
				
				System.out.print(value+" ");
				value *=3;
			}
			for (int l=Col; l>=1;l--) {
				  
				System.out.print(value+" ");
				value /=3;
			}
			System.out.print("\n");
			Col=Col+1;
		}
		
	
			
	}
}