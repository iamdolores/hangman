package hangman;

import java.util.Scanner;

public class Round {
	
	private static final String QUIT_SYMBOL = "#";
	private State state = State.PLAY;
	private int numGuessesRemaining = AsciiHangman.MAX_GUESSES;
	private Player player;
	private Word word;
	private Scanner reader;

	private enum State
	{
		WIN, LOSE, QUIT, PLAY
	}	
	
	public Round(Player player, Word word)
	{
		this.player = player;
		this.word = word;
	}
	
	public State play(LeaderBoard leaderBoard)
	{
		reader = new Scanner(System.in);

		printStartMessage(this.player);
		
		while (state == State.PLAY)
		{
			processUserGuessInput();
		}
		
		if (this.state == State.QUIT)
			printQuitMessage();
		
		if (this.state == State.WIN)
		{
			printWinMessage();
			this.player.won(calculateRoundPoints());
			
		}
		if (this.state == State.LOSE)
		{
			printLoseMessage();
			this.player.lost();
		}
		
		if (this.state == State.LOSE || this.state == State.WIN)
		{
			leaderBoard.updateBoard(this.player);
		}

		return this.state;
	}

	public State getState()
	{
		return this.state;
	}

	private void printQuitMessage()
	{
		printNewLines(10);
		System.out.println(String.format("%n%nSorry to see you quit this round %1$s.%n", this.player.getName()));
	}
	
	private void printNewLines(int num)
	{
		for (int i = 0; i < num; i++)
			System.out.println();		
	}
	
	private void printWinMessage()
	{
		printNewLines(10);
		System.out.println(String.format("%n%nCongratulations %1$s!!  You won this round!%n", this.player.getName()));
		printEndGameSummary();
	}
	
	private void printLoseMessage()
	{
		printNewLines(10);
		System.out.println(String.format("%n%nSorry %1$s! You lost this round.%n", this.player.getName()));
		printEndGameSummary();
	}

	private void printEndGameSummary()
	{
		int letterGuesses = word.getNumberOfLettersGuessed();
		int wordGuesses = word.getNumberOfWordsGuessed();
		int totalGuesses = letterGuesses + wordGuesses;
		
		String letterInfo = "Letters guessed (" + letterGuesses + "): " + word.getLettersGuessed().toUpperCase() + ".";
		String wordInfo = "Words guessed (" + wordGuesses + "): " + word.getWordsGuessed().toUpperCase() + ".";
		String totalGuessesInfo = "You made " + totalGuesses + " guesses total.";
		String secretWordInfo = "The secret word was '" + word.getSecretWord().toUpperCase() + "'.\n\n";

		
		String hangmanInfo = AsciiHangman.getManRepresentation(AsciiHangman.MAX_GUESSES - numGuessesRemaining);
		System.out.println(hangmanInfo);
		if (letterGuesses > 0)
			System.out.println(letterInfo);
		if (wordGuesses > 0)
			System.out.println(wordInfo);
		if (letterGuesses > 0 && wordGuesses > 0) 
			System.out.println(totalGuessesInfo);
		System.out.println(secretWordInfo);
	}
	
	private void processUserGuessInput()
	{
		String input = "";
		// Scanner reader = new Scanner(System.in);
		do {
			printUserGuessPrompt(this.word, this.numGuessesRemaining);
			input = reader.nextLine().trim();
		} while (!isUserGuessInputValid(input));
		// reader.close();
	}

	private static void printUserGuessPrompt(Word word, int numGuessesRemaining)
	{
		int letterGuesses = word.getNumberOfLettersGuessed();
		int wordGuesses = word.getNumberOfWordsGuessed();
		String letterInfo = "Letters guessed (" + letterGuesses + "): " + word.getLettersGuessed().toUpperCase();
		String wordInfo = "Words guessed (" + wordGuesses + "): " + word.getWordsGuessed().toUpperCase();
		String userWordInfo = "Word state: " + word.getPlayerWord().toUpperCase();
		String hangmanInfo = AsciiHangman.getManRepresentation(AsciiHangman.MAX_GUESSES - numGuessesRemaining);
		String numGuessesInfo = "Guesses remaining: " + numGuessesRemaining;
		String guessPrompt = "Guess a letter or word";
		String quitPrompt = "or enter " + QUIT_SYMBOL + " to quit this round:";
		
		System.out.println();
		System.out.println();
		System.out.println(letterInfo);
		System.out.println(wordInfo);
		System.out.println(numGuessesInfo);
		System.out.println();
		System.out.println(userWordInfo);
		System.out.println();
		System.out.println(hangmanInfo);
		System.out.println(guessPrompt);
		System.out.println(quitPrompt);
	}

	/**
	 * Check if guess is valid.
	 * - input must be alphabetic or QUIT_SYMBOL
	 * - input needs to be a new guess
	 * @param input
	 * @return
	 */
	private boolean isUserGuessInputValid(String input)
	{
		if (input.isEmpty())
		{
			System.out.println("the empty string is invalid input");
			return false;
		}
		if (input.equals(QUIT_SYMBOL)) {
			this.state = State.QUIT;
			return true;
		}
		if (!input.matches("[a-zA-Z]+")) {
			System.out.println("non-alphabetic characters are invalid");
			return false;
		}
		if (input.length() == 1)
		{
			if (this.word.hasGuessed(input.charAt(0)))
			{
				System.out.println("the letter '" + input + "' has been guessed before");
				return false;				
			} else 
			{
				if (!this.word.guessLetter(input.charAt(0)))
					this.numGuessesRemaining--;
				if (this.word.getIsCompleted())
					state = State.WIN;
				else if (numGuessesRemaining == 0)
					state = State.LOSE;
				return true;
			}
		}
		if (this.word.hasGuessed(input))
		{
			System.out.println("the word '" + input + "' has been guessed before");
			return false;
		} else 
		{
			if (!this.word.guessWord(input))
				this.numGuessesRemaining--;
			if (this.word.getIsCompleted())
				state = State.WIN;
			else if (numGuessesRemaining == 0)
				state = State.LOSE;
			return true;
		}
	}

	private void printStartMessage(Player player)
	{
		printNewLines(5);
		String message = player.getName() + " let's play!";
		System.out.println(message);
	}
	
	private int calculateRoundPoints()
	{
		// (MAX_GUESSES - WRONG_GUESSES) * WORD_DIFFICULTY
		return numGuessesRemaining * word.getDifficulty();
	}

}
