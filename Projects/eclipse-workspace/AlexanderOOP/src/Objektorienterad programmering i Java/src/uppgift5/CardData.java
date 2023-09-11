package uppgift5;

import java.io.Serializable;

public class CardData implements Serializable
{
	private static final long serialVersionUID = -303030953419105623L;
	
	private long number;
	private int  pinCode;
	
	public CardData()
	{
		
	}
	
	public CardData(long number, int pinCode)
	{
		this.setNumber(number);
		this.setPinCode(pinCode);
	}

	public long getNumber()
	{
		return number;
	}

	public void setNumber(long number)
	{
		this.number = number;
	}

	public int getPinCode()
	{
		return pinCode;
	}

	public void setPinCode(int pinCode)
	{
		this.pinCode = pinCode;
	}
}
