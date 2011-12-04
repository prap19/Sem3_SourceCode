package edu.nlp.ageattr.helper.LexicalStylisticFeatures;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

public class Emoticons {
	
	private final static String emoticons = "rsrc"+File.separatorChar+"emoticonsDictionary.txt";
	public static HashSet<String> emotiSet;
	
	public static void EmotiMapCreate() throws IOException
	{
		//Generate hashmap for emoticons
		emotiSet = new HashSet<String>();
		String[] words;
    	try {
	    	String temp;
			BufferedReader br = new BufferedReader(new FileReader(emoticons));
			while((temp = br.readLine())!= null) {
				words = temp.split("\\s+");
				emotiSet.add(words[0]);
			}
			br.close();
    	}
    	catch(FileNotFoundException e)
    	{
    		System.out.println(e.getMessage());
    	}
	}
	
	public static Boolean isEmoticon(String s) {
		
		// TODO Auto-generated method stub
		Boolean a =false;
		String m;
		m=removeChar(s, '.');
		m=removeChar(m, ',');
		//System.out.println("String is "+m);
		if(emotiSet.contains(m))
			return true;
		
		return a;
	}
	

	public static String removeChar(String s, char c) {

		   String r = "";

		   for (int i = 0; i < s.length(); i ++) {
		      if (s.charAt(i) != c) r += s.charAt(i);
		   }

		   return r;
		}

}
