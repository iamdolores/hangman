package hangman;

import java.util.ArrayList;
import java.util.Arrays;


public class Word {
	private String secretWord;
	private char[] playerWord;
	private boolean[] guessedLetters = new boolean[26];
	private ArrayList<String> guessedWords = new ArrayList<String>();
	private boolean isCompleted = false;
	private int difficulty;
	
	public Word (String word, int difficulty)
	{
		this.secretWord = word;
		this.setDifficulty(difficulty);
		playerWord = new char[word.length()];
		Arrays.fill(this.playerWord, '_');
		Arrays.fill(this.guessedLetters, false);
	}
	
	/**
	 * Check to see if letter has been guessed before.
	 * @param c letter to be checked
	 * @return true if letter has not been guessed, therefore it is a valid guess; otherwise return false
	 */
	public boolean hasGuessed(char c)
	{
		if (!Character.isLetter(c))
			throw new IllegalArgumentException("character must be a letter: " + c);
		Character.toLowerCase(c);
		return this.guessedLetters[c - 'a'];
	}
	
	/**
	 * Check to see if word has been guessed before
	 * @param word word to be checked
	 * @return true if letter has not been guessed, therefore it is a valid guess; otherwise return false
	 */
	public boolean hasGuessed(String word)
	{
		if (!word.matches("[a-zA-Z]+"))
			throw new IllegalArgumentException("word containing characters that aren't letters are not valid: "+ word);
		word.toLowerCase();
		return guessedWords.contains(word);
	}
	
	/**
	 * Check if letter guess matches a letter in the secret word
	 * @param c
	 * @return true if letter guess matches a letter in secret word and has not been guessed before; otherwise return false
	 */
	public boolean guessLetter(char c)
	{
		if (hasGuessed(c)) throw new IllegalArgumentException("must make new guess.");
		Character.toLowerCase(c);
		this.guessedLetters[c - 'a'] = true;
		boolean found = this.secretWord.contains(Character.toString(c));
		if (found)
		{
			for (int i = 0; i < this.secretWord.length(); i++)
			{
				if (this.secretWord.charAt(i) == c)
				{
					playerWord[i] = c;
				}
			}
		}
		if (this.secretWord.equals(new String(this.playerWord))) this.isCompleted = true;
		return found;
	}
	
	/**
	 * Check if word guess matches the secret word.
	 * 
	 * @param word
	 * @return true if word guess matches secret word; otherwise return false
	 */
	public boolean guessWord(String word)
	{
		if (hasGuessed(word)) throw new IllegalArgumentException("must make new guess.");
		word.toLowerCase();
		this.guessedWords.add(word);
		boolean found = this.secretWord.equals(word);
		if (found) {
			playerWord = word.toCharArray();
			this.isCompleted = true;
		}
		return found;
	}
	public boolean getIsCompleted()
	{
		return isCompleted;
	}
	public String getPlayerWord()
	{
		StringBuilder sb = new StringBuilder();
		for (char c : playerWord)
		{
			sb.append(c);
			sb.append(' ');
		}
		return sb.toString();
	}
	
	public String getSecretWord()
	{
		return this.secretWord;
	}
	
	public int getNumberOfLettersGuessed()
	{
		int num = 0;
		for (boolean val: guessedLetters)
		{
			if (val) num++;
		}
		return num;
	}
	
	public int getNumberOfWordsGuessed()
	{
		return guessedWords.size();
	}
	
	public String getLettersGuessed()
	{
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < guessedLetters.length; i++)
		{
			if (guessedLetters[i])
			{
				sb.append((char)('a' + i));
				sb.append(' ');
			}
		}
		return sb.toString().trim();
	}
	
	public String getWordsGuessed()
	{
		StringBuilder sb = new StringBuilder();
		for (String word : guessedWords)
		{
			sb.append(word);
			sb.append(' ');
		}
		return sb.toString().trim();
	}

	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}
}
