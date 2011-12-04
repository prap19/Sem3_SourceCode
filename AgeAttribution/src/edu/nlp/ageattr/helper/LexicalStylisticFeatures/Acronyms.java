package edu.nlp.ageattr.helper.LexicalStylisticFeatures;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

public class Acronyms {

	public static HashSet<String> AcronymSet;
	public static void create() throws IOException
	{
		String line=null;
		AcronymSet =  new HashSet<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File("rsrc"+File.separator+"AcronymsDictionary.txt")));
			while((line= br.readLine()) != null)
			{
				String[] wordscore = line.split("\\s+");
				AcronymSet.add(wordscore[0]);
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Acronyms.java: create() Exception: "+e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	public static boolean isAcronym(String word)
	{
		if(AcronymSet.contains(word.trim().toLowerCase()))
			return true;
		else
			return false;
	}
	
	
}
