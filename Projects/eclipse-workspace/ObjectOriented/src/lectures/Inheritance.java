package lectures;

public class Inheritance extends polymorphism implements Interfacee
{
	String name;
	
	public void sayName() {
		System.out.println("my name");
	}
	public void sayName(String name) {
		System.out.println("my name is "+name);
		
	}
	public void sayHi() {
		
	}
	public static void main(String [] args) {
		polymorphism p= new polymorphism();
		
		p.sayHi();
		encapsulation e=new encapsulation();
		e.setFlavor("beef");
		System.out.println(e.getFlavor());
		
		//Interfacee i=new Interfacee();
		//i.openbag();
	}
	@Override
	public void openbag() {
		// TODO Auto-generated method stub
		
	}
	

}
