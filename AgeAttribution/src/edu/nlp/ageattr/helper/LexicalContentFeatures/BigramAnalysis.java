package edu.nlp.ageattr.helper.LexicalContentFeatures;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.nlp.ageattr.helper.StopWord;
import edu.nlp.ageattr.util.MapUtil;

public class BigramAnalysis extends AbstractBigramAnalysis{

	private StopWord stopWord = new StopWord();
	 	 
	 public BigramAnalysis() {
		// TODO Auto-generated constructor stub
		 super();
		 StopWord.StopWordCreate();
		 this.output= "output\\bigram\\";
	 }
	 
	 	 
	 protected void updateMap(String ageclass, String line) {
		// TODO Auto-generated method stub
		HashMap<String, Double> hashmap = this.ageMap.get(ageclass);
		String[] words = line.toLowerCase().replaceAll("[:.,*!?\"\\;{}()`~@#$%^&|=/]", " ").trim().split("\\s+");
		words = removeStopwords(words);
		if(words.length > 0) {
			String[] bigrams = BigramAnalysis.getAllBigrams(words);
			//System.out.println("Bigrams: "+Arrays.deepToString(bigrams));
			for(int i=0; i<bigrams.length; i++) {
				if(hashmap.containsKey(bigrams[i])) {
					hashmap.put(bigrams[i], hashmap.get(bigrams[i])+1);
				}
				else {	
					hashmap.put(bigrams[i], 1.0);
				}
			}
		}
	}
	 
	
	 
	private String[] removeStopwords(String[] words) {
		// TODO Auto-generated method stub
		
		List<String> res = new ArrayList<String>();
		
		for(int i=0; i<words.length; i++) {
			if(StopWord.isStopWord(words[i]) != 1 && words[i].length()>1) 
				res.add(words[i]);
		//	else
			//	System.err.println("STOP WORD FOUND!: "+words[i]);
		}
		
		//System.out.println(Arrays.deepToString((String[])res.toArray(new String[0])));
		/*
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		return 	(String[])res.toArray(new String[0]);
	}


	public static void main(String[] args) {
		BigramAnalysis analysis = new BigramAnalysis();
		analysis.run(75);
	}

	
	 
}
