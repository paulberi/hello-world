package steg2;

public class Matte
{
	protected String name;
	protected Cat    cat;

	public Matte(String name, Cat cat)
	{
		this.name = name;
		this.cat  = cat;
	}

	public String getName()
	{
		return name;
	}

	public Cat getCat()
	{
		return cat;
	}
}
