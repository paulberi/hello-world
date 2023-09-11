package steg4;

public class Matte
{
	protected String name;
	protected Cat    cat;

	public Matte(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public Cat getCat()
	{
		return cat;
	}
	
	public void setCat(Cat cat)
	{
		this.cat = cat;
	}
}