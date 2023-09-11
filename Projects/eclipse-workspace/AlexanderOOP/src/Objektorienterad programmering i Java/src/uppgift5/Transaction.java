package uppgift5;

import java.io.Serializable;

public class Transaction implements Serializable
{
	private static final long serialVersionUID = 3071856011606283818L;
	
	private long   createdTimestamp;
	private double sum;
	private String message;

	public Transaction()
	{

	}

	public Transaction(double sum, long createdTimestamp, String message)
	{
		this.setSum(sum);
		this.setCreatedTimestamp(createdTimestamp);
		this.setMessage(message);
	}

	public long getCreatedTimestamp()
	{
		return createdTimestamp;
	}

	public void setCreatedTimestamp(long createdTimestamp)
	{
		this.createdTimestamp = createdTimestamp;
	}

	public double getSum()
	{
		return sum;
	}

	public void setSum(double sum)
	{
		this.sum = sum;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}
}
