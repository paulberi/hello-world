package steg1;

public class Main
{
	public static void main(String[] args)
	{
		Dog dog1 = new Dog("Edward", "Tibetan Terrier");
		Cat cat1 = new Cat("Nisse", "Birman");

		System.out.println(dog1.getAnimal() + " " + dog1.getName() + " is a " + dog1.getBreed());
		System.out.println(cat1.getAnimal() + " " + cat1.getName() + " is a " + cat1.getBreed());
	}
}
