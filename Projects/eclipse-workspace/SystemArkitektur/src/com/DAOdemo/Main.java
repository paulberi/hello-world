package com.DAOdemo;

public class Main {

    public static void main(String[] args) {
	    // write your code here
        CarDao dao = new CarDao();
        Car car1 = dao.get(1);
        Car car2 = dao.get(2);
        dao.save(new Car("blue", "Volvo", 210));
        Car car3 = dao.get(3);
    }
}
