package uppgift5;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Kunder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Object[] transactions;
	private String balans1;
	private String spar;
	private String uttag;
	private String balans;
	private LocalDateTime date;
	private String userName;
	private String passWord;
	private String name;
	private String dob;
	private String kontoNr;
	private LocalDateTime dateOfAccount;

	public Kunder() {

	}

	public Kunder(String balans1, String spar, String uttag, String balans, LocalDateTime date) {
		this.balans1 = balans1;
		this.spar = spar;
		this.uttag = uttag;
		this.balans = balans;
		this.date = date;
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

	public Kunder(String name, String dob, String kontoNr, String userName, String passWord,
			LocalDateTime dateOfAccount) {
		// TODO Auto-generated constructor stub
		this.name = name;
		this.dob = dob;
		this.kontoNr = kontoNr;
		this.userName = userName;
		this.passWord = passWord;
		this.dateOfAccount = dateOfAccount;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getKontoNr() {
		return kontoNr;
	}

	public void setKontoNr(String kontoNr) {
		this.kontoNr = kontoNr;
	}

	public String getDateOfAccount() {
		DateTimeFormatter printFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		String dateOutput = printFormat.format(dateOfAccount);
		return dateOutput;
	}

	public void setDateOfAccount(LocalDateTime dateOfAccount) {
		this.dateOfAccount = dateOfAccount;
	}

}