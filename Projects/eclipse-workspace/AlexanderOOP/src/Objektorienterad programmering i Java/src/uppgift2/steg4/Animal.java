package steg4;

abstract class Animal
{
	protected String name;
	protected String breed;
	protected String animal;
	protected Matte  owner;


	public String getName()
	{
		return name;
	}


	public String getBreed()
	{
		return breed;
	}


	public String getAnimal()
	{
		return animal;
	}
	
	public void setMatte(Matte matte)
	{
		owner = matte;
	}
	
	public Matte getMatte()
	{
		return owner;
	}
}
