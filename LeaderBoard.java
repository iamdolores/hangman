package hangman;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class LeaderBoard {
	/**
	 * 
	 * Keep track of MAX_RECORDS.
	 * Read and write records to a file so that they persist.
	 * If new player and already at MAX_RECORDS, replace player who has oldest 'last' date.
	 * 
	 */
	private static final String playerFileName = "players.txt";
	private static final int MAX_RECORDS = 10;
	private ArrayList <Player> players = new ArrayList<Player>();
	
	public LeaderBoard()
	{
		deserializeScores();
	}
	
	public void updateBoard(Player player)
	{
		if (!players.contains(player))
		{
			players.add(player);
			if (players.size() > MAX_RECORDS)
				players.remove(MAX_RECORDS);			
		}
		Collections.sort(players, Comparator.comparing((Player p) -> p.getPoints()).reversed());
		serializeScores();
	}
	
	public Player[] getPlayers()
	{
		Player[] arr = new Player[players.size()];
		return (Player[]) players.toArray(arr);
	}
	

    private void serializeScores() {

        try {
			// write object to file
			FileOutputStream fileStream = new FileOutputStream(playerFileName);
			ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
			objectStream.writeObject(this.players);
			objectStream.close();

		} catch (Exception e) 
        {
			System.out.println(e);
			e.printStackTrace();
		}
    }

    /**
     * Get whole list of scores from file
     */
	private void deserializeScores() {

        try {
			// read object from file
			FileInputStream fileStream = new FileInputStream(playerFileName);
			ObjectInputStream objectStream = new ObjectInputStream(fileStream);
			@SuppressWarnings("unchecked")
			ArrayList<Player> readObject = (ArrayList<Player>) objectStream.readObject();
			this.players = readObject;
			objectStream.close();

		} catch (Exception e) 
        {
			//System.out.println("Problem deserializing players from file " + playerFileName + e);
			this.players = new ArrayList<Player>();
		}
    }

}
