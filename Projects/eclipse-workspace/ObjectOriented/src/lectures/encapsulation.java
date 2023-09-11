package lectures;

public class encapsulation implements Interfacee{
	
	private String flavor;
	public void setFlavor(String newFlavor) {
		flavor=newFlavor;
	}
	public String getFlavor() {
		return flavor;
	}
	
	public void openBag() {
		System.out.println("bag is open");
	}
	
	public static void main(String[] args) {
		//Interfacee i= new Interfacee();
	}
	@Override
	public void openbag() {
		// TODO Auto-generated method stub
		
	}
	

}
