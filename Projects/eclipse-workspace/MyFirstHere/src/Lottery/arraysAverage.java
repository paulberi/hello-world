package Lottery;

public class arraysAverage {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[][] myList= {
				            {(1),(2),(3),(4),(5)},
				           {6,7,8,9,10},
				           {11,12,13,14,15},
				           {16,17,18,19,20}
				           	           
		};
		System.out.println(myList[3][2]);	
		for (int i=0; i<myList.length; i++) {
			for (int j=0; j<myList[i].length; j++) {
				System.out.print(myList[i][j]);
			}
			System.out.println();
		}
		

	}
	
}
//interesting how the results turned out though that is not what I had i mind
