package hangman;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

public class Player implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int MAX_NAME_LENGTH = 20;
	public static final int MIN_NAME_LENGTH = 1;
	
	private String name;
	private int wins;
	private int loses;
	private int points;
	private Date last;
	
	public Player (String name)
	{
		this.name = name;
		this.wins = 0;
		this.loses = 0;
		this.points = 0;
		this.last = new Date();
	}
	
	public Player (String name, int wins, int loses, int points, String date)
	{
		this.name = name;
		this.wins = wins;
		this.loses = loses;
		this.points = points;
	    DateFormat df = DateFormat.getDateInstance();
	    try 
	    {
	    	this.last = df.parse(date);
	    } 
	    catch(ParseException e) 
	    {
	        System.out.println("Unable to parse " + date);
	    }	
	}
	
	public void won(int points)
	{
		this.wins++;
		this.points += points;
		this.last = new Date();
	}
	
	public void lost()
	{
		this.loses++;
		this.last = new Date();
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public int getWins()
	{
		return this.wins;
	}
	
	public int getLoses()
	{
		return this.loses;
	}
	
	public int getPoints()
	{
		return this.points;
	}

	public Date getLast() 
	{
		return last;
	}
}
