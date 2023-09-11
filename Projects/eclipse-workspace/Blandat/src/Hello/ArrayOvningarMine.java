package Hello;

import java.awt.List;
import java.util.Random;
import java.util.Scanner;

public class ArrayOvningarMine {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//reversingArrays(args);
		//slide12OfArrays(args);
		randomAlphabetArray(args);

	}
	public static void reversingArrays(String[] args) {
		
		int[] arr=new int[5];
		System.out.println("skriv när 5 nummer");
		int[] list=arrayToBeReversed(arr);
	
		for (int i=0,j=arr.length-1;i<list.length;i++,j--) {
			arr[j]=list[i];
			System.out.print(list[j]);
			//System.out.println(arr[i]);
		}
		//reverse(list,list.length);//alternative method for reversing array.
		
			
		
	}
	private static void reverse(int[] list, int n) {
		// TODO Auto-generated method stub
		int j=n;
		int[]list1=new int[n];
		for(int i=0;i<n;i++) {
			list1[j-1]=list[i];
			j=j-1;
		}
		for(int k=0;k<n;k++) {
			System.out.print(list1[k]);
		}
	}
	private static int[] arrayToBeReversed(int[] klass) {
		// TODO Auto-generated method stub
		klass =new int[5];
		Scanner input= new Scanner(System.in);
		for(int i=0;i<klass.length;i++) {
			klass[i]=input.nextInt();
		}
		return klass;
		
	}

	public static void slide12OfArrays(String args[]) {
		int[] myList={2,9,5,4,8,1,6};
	for(int i=0; i<myList.length;i++) {
		int[] newlist;
		newlist=methodToSort(myList);
		System.out.print(newlist[i]);
	}
	}
	private static int[] methodToSort(int[] myList2) {
		// TODO Auto-generated method stub
		int minidx = 0;
		for(int i=0; i<myList2.length-1;i++) {
			minidx=i;
			for(int j=i+1;j<myList2.length;j++) {
				if(myList2[j]<myList2[minidx]) {
					minidx=j;
				}
			}
			int temp=myList2[minidx];
			myList2[minidx]=myList2[i];
			myList2[i]=temp;
		}
		return myList2;
		
	
		
	}
	
	public static void randomAlphabetArray (String args[]) {
		//String a='a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z';
	
		char[] C=new char[100];
		char alphabet=count();
		
		}
		
			/*char[] alpha=new char[alphabet];
			for(int i=0;i<alpha.length; i++)
			System.out.println(alpha[i]);*/
	
	private static char slumpBokstav () {
		return 0;
		
	}
	private static char  count () {
		char alphabet = 0;
		int counter = 0;
		while(counter<100) {
			Random rnd=new Random();
			alphabet=(char)('a'+rnd.nextInt(26));
			System.out.print(alphabet);
			counter=counter+1;
		}
		return alphabet;
	}
}
