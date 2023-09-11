package upp5;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class User extends Account implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String userName;
	private String passWord;
	private String name;
	private String kontoNr;
	private LocalDateTime dateOfAccount;

	public User() {
		
	}
	
	public User(String name, String kontoNr, String userName, String passWord,LocalDateTime dateOfAccount,String lastDeposit, String lastWithDrawal) {
		// TODO Auto-generated constructor stub
		this.name=name;
		this.kontoNr=kontoNr;
		this.userName=userName;
		this.passWord=passWord;
		this.dateOfAccount=dateOfAccount;
		this.lastDeposit=lastDeposit;
		this.lastWithDrawal=lastWithDrawal;
	}
	public User(String name, String kontoNr, String userName, String passWord,LocalDateTime dateOfAccount,String balans1, String spar, String uttag, String balans,String lastDeposit, String lastWithDrawal) {
		this.name=name;
		this.kontoNr=kontoNr;
		this.userName=userName;
		this.passWord=passWord;
		this.dateOfAccount=dateOfAccount;
		this.balans1=balans1;
		this.spar=spar;
		this.uttag=uttag;
		this.balans=balans;
		this.lastDeposit=lastDeposit;
		this.lastWithDrawal=lastWithDrawal;
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

	public String getKontoNr() {
		return kontoNr;
	}



	public void setKontoNr(String kontoNr) {
		this.kontoNr = kontoNr;
	}



	public String getDateOfAccount() {
		DateTimeFormatter printFormat = DateTimeFormatter.ofPattern("yyyy.MM.dd");
		String dateOutput = printFormat.format(dateOfAccount);
		return dateOutput;
	}



	public void setDateOfAccount(LocalDateTime dateOfAccount) {
		this.dateOfAccount = dateOfAccount;
	}


}
