package hangman;



public class AsciiHangman {
	
	private static final String[] man = {"          |\n" +
	                               "          |\n" +
			                       "          |\n" +
	                               "          |\n",
	                               
			                       "  O       |\n"+
					               "          |\n"+
					               "          |\n"+
			                       "          |\n",
			                       
			                       "  O       |\n" +
			                       "  |       |\n" +
					               "          |\n"+
			                       "          |\n",
			                       
			                       "  O       |\n" +
			                       " /|       |\n" +
					               "          |\n"+
			                       "          |\n",
			                       
			                       "  O       |\n" +
			                       " /|\\      |\n" +
					               "          |\n"+
			                       "          |\n",
			                       
			                       "  O       |\n" +
			                       " /|\\      |\n" +
					               " /        |\n"+
			                       "          |\n",
			                       
			                       "  O       |\n" +
			                       " /|\\      |\n" +
					               " / \\      |\n"+
			                       "          |\n" };
			                    		 
	
	
	private static final String noose = "  + - - - +\n" + 
	                              "  |       |\n";
	
	
	private static final String ground = "          |  \n"+ 
			                      "= = = = = = =\n";

	public static final int MAX_GUESSES = man.length - 1;
	
	public static String getManRepresentation(int numWrongGuesses)
	{
		if (numWrongGuesses > MAX_GUESSES)
			throw new IllegalArgumentException("you have more wrong guesses than what is allowed");
		StringBuilder sb = new StringBuilder();
		sb.append(noose);
		sb.append(man[numWrongGuesses]);
		sb.append(ground);
		return sb.toString();
	}

}
