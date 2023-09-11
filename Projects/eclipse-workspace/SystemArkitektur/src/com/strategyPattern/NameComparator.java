package com.strategyPattern;

import java.util.Comparator;

public class NameComparator implements Comparator<Products> {

	@Override
	public int compare(Products o1, Products o2) {
		// TODO Auto-generated method stub
		return o1.getName().compareTo(o2.getName());
	}

}
