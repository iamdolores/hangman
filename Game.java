package hangman;

import java.util.Scanner;

public class Game {

	private static String NEW_PLAYER_SYMBOL = "*";
	private static String EXIT_GAME_SYMBOL = "#";
	private static String PREVIOUS_MENU_SYMBOL = "<<";

	private enum GameState
	{
		PLAYER_KNOWN, NEW_PLAYER, EXIT, START, PLAY_ROUND
	}	
	
	private GameState gameState = GameState.START;
	private WordGenerator wordGenerator = new WordGenerator();
	private LeaderBoard leaderBoard = new LeaderBoard();
	
	private Player currentPlayer = null;
	private Word currentWord = null;
	private Scanner reader;
	
	
	public void play()
	{
		reader = new Scanner(System.in);
		printWelcomeMessage();
		
		while (this.gameState != GameState.EXIT)
		{
			processPlayerInput(this.leaderBoard.getPlayers());
			while (this.gameState == GameState.NEW_PLAYER)
			{
				processNewPlayerInput();

			}
			if (this.gameState == GameState.PLAYER_KNOWN && currentPlayer != null)
			{
				processWordDifficultyInput();				
				if (this.gameState == GameState.PLAY_ROUND && currentPlayer != null && currentWord != null)
				{
					Round round = new Round(currentPlayer, currentWord);
					round.play(leaderBoard);
					this.gameState = GameState.START;					
				}
			}
		}
		
		printGoodByeMessage();
		reader.close();
	}

	private void processNewPlayerInput()
	{
		String input = "";
		do {
			printNewPlayerPrompt();
			input = reader.nextLine().trim();
		} while (!isNewPlayerInputValid(input));

	}
	
	private static void printNewPlayerPrompt()
	{
		String newPlayerMessage = "Enter new player name";
		String prevMenuPrompt = "or " + PREVIOUS_MENU_SYMBOL + " to return to previous menu: ";
		System.out.println(newPlayerMessage);
		System.out.println(prevMenuPrompt);
	}
	
	private boolean isNewPlayerInputValid(String input)
	{
		Player[] players = this.leaderBoard.getPlayers();
		if (input.equals(PREVIOUS_MENU_SYMBOL))
		{
			this.gameState = GameState.START;
			return true;
		}
		
		boolean isValid = true;
		if (input.length() < Player.MIN_NAME_LENGTH || input.length() > Player.MAX_NAME_LENGTH)
		{
			System.out.println("Name must be between " + Player.MIN_NAME_LENGTH + 
					" and " + Player.MAX_NAME_LENGTH + " letters long.");
			isValid = false;
		}
		if (isValid && !input.matches("[a-zA-Z]+"))
		{
			System.out.println("Name must contain only letters");
			isValid = false;
		}
		if (isValid)
		{
			for (Player player : players)
			{
				if (player.getName().equals(input))
				{
					System.out.println("Name must be unique.");
					isValid = false;
					break;
				}
			}
		}
		if (isValid)
		{
			currentPlayer = new Player(input);
			gameState = GameState.PLAYER_KNOWN;
			return true;
		}

		if (!isValid) System.out.println("Invalid input.  Try again.");
		return isValid;
	}

	private static void printWelcomeMessage()
	{
		String message = "Welcome!  Let's play Hangman!!\n";
		System.out.println(message);
	}
	
	private static void printGoodByeMessage()
	{
		String message = "Good bye\n\n";
		System.out.println(message);
	}
	
	private static void printLeaderBoard(Player[] players)
	{
		int namePaddingParam = Player.MAX_NAME_LENGTH + 5;
		String formatHeader = "%1$-"+ namePaddingParam + "s\t%2$s\t%3$s\t%4$s\n\n"; 
		String header = String.format(formatHeader, "NAME", "WINS", "LOSES", "POINTS");
		
		StringBuilder sb = new StringBuilder();
		sb.append(header);
		String formatEntry = "(%1$d) " + "%2$-" + namePaddingParam + "s\t%3$d\t%4$d\t%5$d\n"; 
		int count = 1;
		for (Player player : players)
		{
			String entry = String.format(formatEntry, count++ , player.getName(), player.getWins(),
					player.getLoses(), player.getPoints());
			sb.append(entry);			
		}
		sb.append("\n");
		System.out.println(sb.toString());
	}
	
	private static void printNewRoundPrompt(Player[] players)
	{
		String previousPlayerPrompt = "Enter the corresponding number from the list above to play as previous player";
		String newPlayerPrompt = "or " + NEW_PLAYER_SYMBOL + " to enter a new player";
		String exitGamePrompt = "or " + EXIT_GAME_SYMBOL + " to exit game:";
		
		if (players.length > 0)
		{
			printLeaderBoard(players);
			System.out.println(previousPlayerPrompt);			
		}
		System.out.println(newPlayerPrompt);
		System.out.println(exitGamePrompt);
	}
	
	private void processPlayerInput(Player[] players)
	{

		String input = "";
		do {
			printNewRoundPrompt(players);
			input = reader.nextLine().trim();
		} while (!isNewRoundInputValid(players, input));
	}
	
	private boolean isNewRoundInputValid(Player[] players, String input)
	{
		boolean isValid = false;
		if (input.equals(EXIT_GAME_SYMBOL))
		{
			this.gameState = GameState.EXIT;
			return true;
		}
		if (input.equals(NEW_PLAYER_SYMBOL)) 
		{
			this.gameState = GameState.NEW_PLAYER;
			return true;
		}
		try 
		{
			int number = Integer.parseInt(input);
			if (number >= 1 || number <= players.length) 
			{
				this.gameState = GameState.PLAYER_KNOWN;
				this.currentPlayer = players[number - 1];
				return true;
			}
		} catch (Exception e) 
		{
			isValid = false;
		}
		if (!isValid) System.out.println("Invalid input.  Try again.");
		return isValid;
	}
	
	private void processWordDifficultyInput()
	{

		String input = "";
		do {
			printWordDifficultyPrompt();
			input = reader.nextLine().trim();
		} while (!isWordDifficultyInputValid(input));
	}
	
	private static void printWordDifficultyPrompt()
	{
		int MIN_LEVEL = 1;
		int MAX_LEVEL = WordGenerator.NUM_DIFFICULTY_LEVELS;
		String wordDifficultyOption = "Enter a word difficulty level between " + MIN_LEVEL + " and " + MAX_LEVEL;
		String prevOption = "or " + PREVIOUS_MENU_SYMBOL + " to go back to the beginning:";
		
		System.out.println(wordDifficultyOption);
		System.out.println(prevOption);
	}
	
	private boolean isWordDifficultyInputValid(String input)
	{
		if (input.equals(PREVIOUS_MENU_SYMBOL))
		{
			this.gameState = GameState.START;
			return true;
		}
		boolean isValid = false;
		try 
		{
			int difficulty = Integer.parseInt(input);
			if (difficulty >= 1 || difficulty <= WordGenerator.NUM_DIFFICULTY_LEVELS) 
			{
				this.gameState = GameState.PLAY_ROUND;
				this.currentWord = wordGenerator.getWord(difficulty);

				return true;
			}
		} catch (Exception e) 
		{
			isValid = false;
		}
		if (!isValid) System.out.println("Invalid input.  Try again.");
		return isValid;
		
	}
}
