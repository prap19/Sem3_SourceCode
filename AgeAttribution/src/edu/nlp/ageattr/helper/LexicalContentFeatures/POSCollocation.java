package edu.nlp.ageattr.helper.LexicalContentFeatures;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class POSCollocation extends AbstractBigramAnalysis{

	private MaxentTagger tagger;
	 
	public POSCollocation() {
		// TODO Auto-generated constructor stub
		super();
		try {
			tagger = new MaxentTagger("rsrc/left3words-distsim-wsj-0-18.tagger");
			this.output = "output\\pos\\";
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * returns a string that contains only the part of speech of the word rather than the word itself
	 * @param str
	 * @return part of speech of the words
	 */
	
	public String stringToPOS(String str) {
	
		str = str.replaceAll("\\W+", " ");
		String[] taggedStr = tagger.tagString(str).split("\\s+");
		System.out.println("taggedstr: "+Arrays.deepToString(taggedStr)+" len:"+taggedStr.length);
		StringBuilder builder = new StringBuilder();
		for(String word: taggedStr) {
			try {
				builder.append(word.split("/")[1]+" ");
			}catch(ArrayIndexOutOfBoundsException e)
			{
				System.out.println("index going out of bound for tagged word: "+word+" len:"+word.length());
				System.exit(1);
			}
	}
		
		return builder.toString().trim();
	}
	
	@Override
	protected void updateMap(String ageClass, String line) {
		// TODO Auto-generated method stub
		
		if(this.ageMap.containsKey(ageClass)) {
			HashMap<String, Double> hashmap = this.ageMap.get(ageClass);
			if(line.replaceAll("\\W+", "").length() > 0) {
				String[] words = stringToPOS(line).split("\\s+");
				String[] bigrams = BigramAnalysis.getAllBigrams(words);
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
		else
			System.exit(1);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		POSCollocation collocation = new POSCollocation();
		collocation.run(0);
	//	System.out.println("I'll will do it.".replaceAll("\\W+", "X"));
	}

	

}
