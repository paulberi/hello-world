package steg3;

import java.util.ArrayList;


public class Husse
{
	protected String         name;
	protected ArrayList<Dog> dogs = new ArrayList<Dog>();

	public Husse(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}
	
	public void addDog(Dog dog)
	{
		dogs.add(dog);
	}

	public void presentYourselfAndYourDogs()
	{
		System.out.println(name + " has " + dogs.size() + " dogs and they are: ");
		for (Dog dog : dogs)
		{
			System.out.println(dog.getName() + ", a " + dog.getBreed().toLowerCase() + ".");
		}
	}
}
