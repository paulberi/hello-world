package upp5;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Account implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String balans1;
	protected String spar;
	protected String uttag;
	protected String balans;
	private LocalDateTime date;
	protected String lastDeposit;
	protected String lastWithDrawal;
	
	public Account() {
	}
	
	public Account(String balans1, String spar, String uttag, String balans,String lastDeposit, String lastWithDrawal){
		this.balans1=balans1;
		this.spar=spar;
		this.uttag=uttag;
		this.balans=balans;
		this.lastDeposit=lastDeposit;
		this.lastWithDrawal=lastWithDrawal;
		
	}

	public String getBalans1() {
		return balans1;
	}

	public void setBalans1(String balans1) {
		this.balans1 = balans1;
	}

	/**
	 * @return the lastDeposit
	 */
	private final String getLastDeposit() {
		return lastDeposit;
	}

	/**
	 * @param lastDeposit the lastDeposit to set
	 */
	private final void setLastDeposit(String lastDeposit) {
		this.lastDeposit = lastDeposit;
	}

	/**
	 * @return the lastWithDrawal
	 */
	private final String getLastWithDrawal() {
		return lastWithDrawal;
	}

	/**
	 * @param lastWithDrawal the lastWithDrawal to set
	 */
	private final void setLastWithDrawal(String lastWithDrawal) {
		this.lastWithDrawal = lastWithDrawal;
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