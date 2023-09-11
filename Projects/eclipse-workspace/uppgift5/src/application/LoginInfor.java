package application;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoginInfor implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String userName;
	private String passWord;
	private String name;
	private String dob;
	private String kontoNr;
	private LocalDateTime dateOfAccount;

	public LoginInfor() {
		
	}
	
	public LoginInfor(String name, String dob, String kontoNr, String userName, String passWord,LocalDateTime dateOfAccount) {
		// TODO Auto-generated constructor stub
		this.name=name;
		this.dob=dob;
		this.kontoNr=kontoNr;
		this.userName=userName;
		this.passWord=passWord;
		this.dateOfAccount=dateOfAccount;
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
