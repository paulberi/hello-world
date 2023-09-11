package steg3;

import java.util.ArrayList;


public class Main
{
	public static void main(String[] args)
	{
		Husse husse1 = new Husse("Peter");
		Dog   dog1   = new Dog("Sam", "German Shepherd");
		Dog   dog2   = new Dog("Penny", "Japanese Spitz");

		husse1.addDog(dog1);
		husse1.addDog(dog2);

		husse1.presentYourselfAndYourDogs();
	}
}
