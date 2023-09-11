package tutorialsFromAlex;

public class CallPen {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//here we call the penAndHeadphones 
		penAndHeadphones p= new penAndHeadphones();
		
		System.out.println(p.colour);
		System.out.println(p.point);
		System.out.println(p.type);
		
		p.click();
		System.out.println(p.clicked);
		
		EarPhones e= new EarPhones();
		System.out.println(e.colour);
		
		//here we call the headphone object where we turnon and off, and check volume and mic
		e.powerOn();
		System.out.println(e.power);
		e.powerOff();
		System.out.println(e.power);
		
		e.volumeUp();
		e.volumeUp();
		e.volumeUp();
		System.out.println(e.volume);
		
		e.volumeDown();
		System.out.println(e.volume);
		
		e.micOn();
		System.out.println(e.mic);
		
		//printing out the array of the controls of the headphone
		
		for(int i=0;i<e.controls.length;i++) {
			System.out.println(e.controls[i]);
		}
		
		/*calling the car
		 * we gonna change gears up and down and try to control the speed from there
		 * and we try also enjoying some music
		 * */
		System.out.println("now we check out our car");
		Car c=new Car();
		
		
		
		c.gears();
		c.gears();
		c.gears();
		c.gears();
		c.trottle();
		int acceleration= c.gear*c.speed; //not the correct formula or practical calculations in reality but just trying to understand OOP
		System.out.println("when the gear is "+c.gear+ " the speed is "+ c.speed+"km/h and acceleration is "+acceleration);
	

	}

}
