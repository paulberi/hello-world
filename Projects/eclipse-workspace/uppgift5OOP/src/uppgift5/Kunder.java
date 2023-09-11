package uppgift5;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class Kunder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String passWord;
	private String name;
	protected String kontoNr;
	private String dateOfAccount;
	protected String incomingBalance;
	private String latestTransaction;

	public Kunder() {

	}

	public Kunder(String name, String kontoNr, String passWord, String dateOfAccount, String incomingBalance,
			String latestTransaction) {
		// TODO Auto-generated constructor stub
		this.name = name;
		this.kontoNr = kontoNr;
		this.passWord = passWord;
		this.dateOfAccount = dateOfAccount;
		this.incomingBalance = incomingBalance;
		this.latestTransaction = latestTransaction;

	}

	/**
	 * @return the passWord
	 */
	public final String getPassWord() {
		return passWord;
	}

	/**
	 * @param passWord the passWord to set
	 */
	public final void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	/**
	 * @return the name
	 */
	public final String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public final void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the kontoNr
	 */
	public String getKontoNr() {
		return kontoNr;
	}

	/**
	 * @param kontoNr the kontoNr to set
	 */
	public final void setKontoNr(String kontoNr) {
		this.kontoNr = kontoNr;
	}

	/**
	 * @return the incomingBalance
	 */
	public final String getIncomingBalance() {
		return incomingBalance;
	}

	/**
	 * @param incomingBalance the incomingBalance to set
	 */
	public final void setIncomingBalance(String incomingBalance) {
		this.incomingBalance = incomingBalance;
	}

	/**
	 * @return the latestTransaction
	 */
	public final String getLatestTransaction() {
		return latestTransaction;
	}

	/**
	 * @param latestTransaction the latestTransaction to set
	 */
	public final void setLatestTransaction(String latestTransaction) {
		this.latestTransaction = latestTransaction;
	}

	/**
	 * @return the dateOfAccount
	 */
	public String getDateOfAccount() {
		LocalDateTime time = LocalDateTime.now();
		DateTimeFormatter printFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd -- hh:mm:ss");
		String dateOutput = printFormat.format(time);
		return dateOutput;
	}

	/**
	 * @param dateOfAccount the dateOfAccount to set
	 */
	public final void setDateOfAccount(String dateOfAccount) {
		this.dateOfAccount = dateOfAccount;
	}

}