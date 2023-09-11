package tutorialsFromAlex;

public class Polymorphismosv {
	static String name=" Ambe"; /*polymorphism is done using overloading because they have same name
	their difference is the number of parameters declared in the method
	or type of parameters
	Another type of polymorphism is overriding and here it involves calling 
	the method to another class or project
	*/
				//example
	public void sayName() {
		System.out.println("my name!");
	}
	public void sayName(String name) {
		System.out.println("My name is "+name);
	}
	public static void main(String args[]) {
		ClassToOveridePolym c=new ClassToOveridePolym();
		
		c.getFlavour();
		c.setFlavour("beef");
		System.out.println(c.getFlavour());
	}
	
	


}
