package uppgift5;

import java.io.Serializable;
import java.util.ArrayList;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;


public class Account implements Serializable
{
	private static final long serialVersionUID = 1976574805403607235L;

	LongProperty           accountNumber    = new SimpleLongProperty();
	DoubleProperty         balance          = new SimpleDoubleProperty();
	LongProperty           createdTimestamp = new SimpleLongProperty();
	ArrayList<Transaction> transactions     = new ArrayList<Transaction>();
	ArrayList<CardData>    cards            = new ArrayList<CardData>();

	public Account()
	{
	}

	public Account(long accountNumber, CardData card)
	{
		createdTimestamp.setValue(ATM.getTimestamp());
		this.accountNumber.setValue(accountNumber);
		cards.add(card);
	}

	public long getAccountNumber()
	{
		return accountNumber.longValue();
	}

	public void setAccountNumber(long accountNumber)
	{
		this.accountNumber.setValue(accountNumber);
	}

	public double getBalance()
	{
		return balance.doubleValue();
	}

	public void setBalance(double balance)
	{
		this.balance.setValue(balance);
	}

	public long getCreatedTimestamp()
	{
		return createdTimestamp.longValue();
	}

	public void setCreatedTimestamp(long createdTimestamp)
	{
		this.createdTimestamp.setValue(createdTimestamp);
	}

	public ArrayList<CardData> getCards()
	{
		return cards;
	}

	public void setCards(ArrayList<CardData> cards)
	{
		this.cards = cards;
	}

	public void addCard(CardData card)
	{
		cards.add(card);
	}

	public ArrayList<Transaction> getTransactions()
	{
		return transactions;
	}

	public void setTransactions(ArrayList<Transaction> transactions)
	{
		this.transactions = transactions;
	}

	public void addTransaction(Transaction transaction)
	{
		transactions.add(transaction);
	}

	public boolean withdraw(int sum)
	{
		if (getBalance() < sum)
			return false;
		else
		{
			transactions.add(new Transaction(sum, ATM.getTimestamp(), "Bankomat"));
			setBalance(getBalance() - sum);
			return true;
		}

	}

	public void deposit(int sum)
	{
		setBalance(getBalance() + sum);
		transactions.add(new Transaction(sum, ATM.getTimestamp(), "Insättning"));
	}
}
