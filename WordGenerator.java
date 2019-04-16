package hangman;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;


public class WordGenerator {
    private static final String baseURL = new String("http://app.linkedin-reach.io/words");
    private static final String startCounterFileName = "startCounts.txt";
    public static final int NUM_DIFFICULTY_LEVELS = 10;
    public static final int DIFFICULTY_DEFAULT = 5;
    
    private int start[] = new int[NUM_DIFFICULTY_LEVELS];    
    private LinkedList<LinkedList<String>> words = new LinkedList<LinkedList<String>> ();
    private int BUFFER_SIZE = 5;
    
    public WordGenerator()
    {
    	for (int i = 0; i < start.length; i++)
    		words.add(new LinkedList<String>());
    	Arrays.fill(start,0);
    	getStartCountersFromFile();
    }
    
    public Word getWord()
    {
    	return getWord(DIFFICULTY_DEFAULT);
    }
    
    public Word getWord(int difficulty)
    {
    	if (words.get(difficulty-1).size() == 0) refreshWords(difficulty);
    	updateStartCounter(difficulty);
    	return new Word (words.get(difficulty-1).removeFirst(), difficulty);
    }
    
    private void getStartCountersFromFile()
    {   
    	try 
    	{
            BufferedReader reader = new BufferedReader(new FileReader(startCounterFileName));
            for (int i = 0; i < start.length; i++)
            {
            	start[i] = getStartFileValue(reader.readLine());
            }
            reader.close();
    	} catch (Exception e)
    	{
    		for(int i = 0; i < start.length; i++)
    			start[i] = 0;
    		try 
    		{
    			FileWriter w = new FileWriter(startCounterFileName);
                for (int i = 0; i < start.length; i++)
                	w.write(Integer.toString(start[i]));
                w.close();
    		} catch (Exception e1)
    		{
    			System.out.println(e1);
    		}
    	}
    }
    
    private void updateStartCounter(int difficulty)
    {
    	start[difficulty-1]++;
    	updateFileStartCounter(difficulty);
    }
    
    private void resetStartCounter(int difficulty)
    {
    	start[difficulty-1] = 0;
    	updateFileStartCounter(difficulty);
    }
    
    private void updateFileStartCounter(int difficulty)
    {
    	try
    	{
    		
    		File tmp = File.createTempFile(startCounterFileName, ".tmp");
    		BufferedWriter writer = new BufferedWriter (new FileWriter(tmp));
    		BufferedReader reader = new BufferedReader (new FileReader(startCounterFileName));
		
    		// copy over to tmp file
    		for (int i = 1; i < difficulty; i++)
    			writer.write(String.format("%d%n", getStartFileValue(reader.readLine())));
		
    		// discard line 
    		reader.readLine();
		
    		// write new value to tmp file
    		writer.write(String.format("%s%n", Integer.toString(start[difficulty-1])));

    		for (int i = difficulty + 1; i <= NUM_DIFFICULTY_LEVELS; i++)
    			writer.write(String.format("%d%n", getStartFileValue(reader.readLine())));
		
    		reader.close();
    		writer.close();
    		tmp.renameTo(new File(startCounterFileName));
    	} catch (Exception e)
    	{
    		System.out.println("Problem writing start counters for word REST API to file " + e);
    	}
    }
    
    private int getStartFileValue(String line)
    {
    	try 
    	{
    		return Integer.parseInt(line);
    	} catch (Exception e)
    	{
    		return 0;
    	}
    	
    }
    private void refreshWords(int difficulty)
    {
    	getWordsFromAPI(difficulty);
    	if (words.get(difficulty - 1).size() == 0)
    	{
    		resetStartCounter(difficulty);
        	getWordsFromAPI(difficulty);
    	}
    }
    
    private void getWordsFromAPI(int difficulty)
    {
        try {

            URL url = new URL(baseURL + "?difficulty=" + difficulty + "&count=" + BUFFER_SIZE + "&start=" + start[difficulty-1]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() != 200) 
            {
                throw new RuntimeException("Failed : HTTP Error code : "
                        + conn.getResponseCode());
            }
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(in);
            String output;
            while ((output = br.readLine()) != null) 
            {
                words.get(difficulty-1).add(output.trim());
            }
            conn.disconnect();
        } catch (Exception e) 
        {
            System.out.println("Problem getting words from REST API: " + e);
        }
    }
}

