package steg4;

public class Main
{
	public static void main(String[] args)
	{
		Matte matte1 = new Matte("Stina");
		Cat   cat1   = new Cat("Olle", "Mainecoon");
		
		matte1.setCat(cat1);
		cat1.setMatte(matte1);

		System.out.println(matte1.getName()+" has a "+matte1.getCat().getAnimal().toLowerCase()+" named "+matte1.getCat().getName()+".");
		System.out.println("The " + cat1.getAnimal().toLowerCase() + " " + cat1.getName() + "'s owner is called " + cat1.getMatte().getName() + ".");
	}

}
