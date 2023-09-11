package RevisionGeneral;

public class BrushingTeeth {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Welcome to the teeth brushing excercise");
		int x = 0, numberOfTeeth=32;
		while(x<=numberOfTeeth) {
			System.out.println("you have brushed "+x+" teeth!");
			x++;
		}
		System.out.println("Your teeth are clean, hope you have eaten breakfast \nHave a nice day in school");
		
		for(x=0; x<=numberOfTeeth;x++) {
			System.out.println("you have brushed "+x+" teeth!");
		}
	}

}
