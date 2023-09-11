package repetitionsOvningar;

import java.awt.List;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ArrayPrograms {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//maxInArray(args);
		joiningArrays(args);
		
	}

	private static void joiningArrays(String[] args) {
		// TODO Auto-generated method stub
		int[] tal = {1,2,3,4,5};
		int[] tal1 = {6,7,8,9,0};
		
		int []result=new int[tal.length+tal1.length];
		
		System.arraycopy(tal, 0, result, 0, tal.length);
		System.arraycopy(tal1, 0, result, tal.length, tal1.length);
		
		System.out.println(Arrays.toString(result));
		
		String[]hi= {"aron","moses"};
		String []he= {"kain","abel"};
		
		String[] add=new String[hi.length+he.length];
		
		System.arraycopy(hi, 0, add, 0, hi.length);
		System.arraycopy(he,0 , add ,hi.length , he.length);
		String a=Arrays.toString(result);
		String b=Arrays.toString(add);
		
		System.out.println(Arrays.toString(add));
		
		System.out.println(a+ " and "+b);
		
		
		try {
			FileWriter fw=new FileWriter("/Users/paulberinyuylukong/Desktop/paul1");
			fw.write(a + b);
			fw.close();
		}catch(Exception e) {
			System.out.println(e);
		}
		
	}

	private static void maxInArray(String[] args) {
		// TODO Auto-generated method stub
		try {
		int [] tal= {5,4,4,9,8,6,7,2};
		int min = 0, max = 0;
		Arrays.sort(tal);
		for(int i=0; i<tal.length; i++) {
			max=tal[i];min=tal[i];
			for (int j=1+i; j<tal.length; j++) {
				if(max>tal[j]);{
					max=tal[j];
				}
				
					//if(min<tal[j]);{
					//min=tal[j];
				//}
				
					
			
		}
		
		
		}
		System.out.print(max);
		}
		catch(Exception e) {
			System.out.println(e);
	}
	}
}
	