package edu.nlp.ageattr.helper.LexicalStylisticFeatures;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

public class Puctuations {

	public static HashSet<String> punctuationSet;
	public static void create() throws IOException
	{
		String line=null;
		punctuationSet =  new HashSet<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File("rsrc"+File.separator+"Punctuations.txt")));
			while((line= br.readLine()) != null)
			{
				punctuationSet.add(line);
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Punctuations.java: create() Exception: "+e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	public static boolean isPunctuation(String word)
	{
		if(punctuationSet.contains(word.trim().toLowerCase()))
			return true;
		else
			return false;
	}
	
	
}
