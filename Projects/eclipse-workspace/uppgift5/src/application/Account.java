package application;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Account implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String balans1;
	private String spar;
	private String uttag;
	private String balans;
	private LocalDateTime date;
	
	public Account() {
	}
	
	public Account(String balans1, String spar, String uttag, String balans, LocalDateTime date){
		this.balans1=balans1;
		this.spar=spar;
		this.uttag=uttag;
		this.balans=balans;
		this.date=date;
	}

	public String getBalans1() {
		return balans1;
	}

	public void setBalans1(String balans1) {
		this.balans1 = balans1;
	}

	public String getSpar() {
		return spar;
	}

	public void setSpar(String spar) {
		this.spar = spar;
	}

	public String getUttag() {
		return uttag;
	}

	public void setUttag(String uttag) {
		this.uttag = uttag;
	}

	public String getBalans() {
		return balans;
	}

	public void setBalans(String balans) {
		this.balans = balans;
	}

	public String getDate() {
		DateTimeFormatter printFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		String dateOutput = printFormat.format(date);
		return dateOutput;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}
	
}