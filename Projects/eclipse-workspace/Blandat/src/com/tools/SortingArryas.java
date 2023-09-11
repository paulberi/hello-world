package com.tools;

public class SortingArryas {
	public static void main(String[] args) {
		int[] sort= {2,9,5,4,8,1,6};
		for (int i=0; i<sort.length; i++) {
			int index=i;
			for(int j=i+1; j<sort.length; j++) {
				if(sort[j]<sort[index]) {
					index=j;
				}
			}
			int min=sort[index];
			sort[index]=sort[i];
			sort[i]=min;
		}
		for(int item:sort) {
			System.out.println(item);
		}
	}
}
