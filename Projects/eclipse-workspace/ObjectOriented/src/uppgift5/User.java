package uppgift5;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class User implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Account[] getTransactions() {
		return transactions;
	}

	public void setTransactions(Account[] transactions) {
		this.transactions = transactions;
	}

	public LoginInfor[] getCustomers() {
		return customers;
	}

	public void setCustomers(LoginInfor[] customers) {
		this.customers = customers;
	}

	private Account[] transactions;
	private LoginInfor[]customers;
	
	public User(Account[]transactions,LoginInfor[]customers) {
		this.transactions=transactions;
		this.customers=customers;
	}

	
}