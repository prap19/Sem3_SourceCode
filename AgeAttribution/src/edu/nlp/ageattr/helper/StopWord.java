package edu.nlp.ageattr.helper;

import java.util.*;
import java.io.*;

public class StopWord {
	String stopWordList[];
	static FileInputStream wordListFile;
	static HashMap<String, Double> wordList;
	
	public static final String wordListURI = "rsrc"+File.separatorChar+"NewStopWordList.txt";
	public static void StopWordCreate()
	{
		System.out.println(wordListURI);
		String line;
		try {
			wordList= new HashMap<String, Double>();
			wordListFile = new FileInputStream(wordListURI);
			DataInputStream in = new DataInputStream(wordListFile);
			BufferedReader br = new BufferedReader(new InputStreamReader(wordListFile));
			while((line = br.readLine())!= null)
			{
				wordList.put(line, new Double(1.0));
			}
			in.close();
		}
		catch(Exception e)
		{
			System.err.println("StopWord; StopWordCreate() Exception: " + e.getMessage());
		}
	}
	
	public static int isStopWord(String word)
	{
		if(wordList.containsKey(word.trim().toLowerCase()))
			return 1;
		else
			return 0;
		
	/*	
		if(wordList.get(word.trim().toLowerCase()) == null)
			return 0;
		if((Double)wordList.get(word.trim().toLowerCase()) == 1.0)
			return 1;
		return 0;
	*/
	}

	public String[] removeStopWords(String[] result) {
		// TODO Auto-generated method stub
			List<String> stringList = new ArrayList<String>();
			 
			for(String string : result) {
			   if(string != null && string.length() > 0 && (this.isStopWord(string)!=1)) {
			      stringList.add(string);
			   }
			}
			return (String[]) stringList.toArray(new String[stringList.size()]);
		}

		
}
