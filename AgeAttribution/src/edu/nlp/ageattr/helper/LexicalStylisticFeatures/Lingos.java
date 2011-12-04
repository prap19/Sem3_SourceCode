package edu.nlp.ageattr.helper.LexicalStylisticFeatures;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

public class Lingos {

	public static HashSet<String> LingoSet;
	public static void create() throws IOException
	{
		String line=null;
		LingoSet =  new HashSet<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File("rsrc"+File.separator+"LingosDictionary.txt")));
			while((line= br.readLine()) != null)
			{
				String[] wordscore = line.split("\\s+");
				LingoSet.add(wordscore[0]);
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Lingos.java: create() Exception: "+e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	public static boolean isLingo(String word)
	{
		if(LingoSet.contains(word.trim().toLowerCase()))
			return true;
		else
			return false;
	}
	
	
}
