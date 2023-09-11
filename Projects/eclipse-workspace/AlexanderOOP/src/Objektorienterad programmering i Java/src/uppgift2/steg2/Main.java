package steg2;

public class Main
{
	public static void main(String[] args)
	{
		Cat   cat1   = new Cat("Nisse", "Birman");
		Matte matte1 = new Matte("Britt", cat1);
		
		System.out.println(
				matte1.getName()+" has a "+
				matte1.getCat().getAnimal().toLowerCase()+" named "+
				matte1.getCat().getName()+" which is a "+
				matte1.getCat().getBreed().toLowerCase()+"."
		);
	}
}
