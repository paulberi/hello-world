package com.strategyPattern;

import java.util.Comparator;

public class SharesComparator implements Comparator<Products> {

	@Override
	public int compare(Products o1, Products o2) {
		// TODO Auto-generated method stub
		return o1.getShares()-o2.getShares();
	}
}
