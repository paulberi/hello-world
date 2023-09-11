package uppgift5;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;



public class Account extends Kunder implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String balans1;
	private String spar;
	private String uttag;
	private String balans;
	private SimpleDateFormat date;
	
	Kunder Kunds;
	
	
	
	public Account() {
	}
	
	
	public Account(String balans1, String spar, String uttag, String balans, SimpleDateFormat date, String kontoNr){
		this.balans1=balans1;
		this.spar=spar;
		this.uttag=uttag;
		this.balans=balans;
		this.date=date;
		this.kontoNr=kontoNr;
		
	}
	
	/**
	 * @return the kontoN
	 */
	public String getKontoNr() {
		return getKontoNr();
	}

	/**
	 * @param kontoN the kontoN to set
	 */
	public void setKontoN(String kontoNr) {
		this.kontoNr = kontoNr;
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
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String thedate=format.format(new Date());
		return thedate;
	}

	public void setDate(SimpleDateFormat date) {
		this.date = date;
	}

}
