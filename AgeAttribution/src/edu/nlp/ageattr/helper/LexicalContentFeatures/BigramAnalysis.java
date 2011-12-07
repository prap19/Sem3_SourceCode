package edu.nlp.ageattr.helper.LexicalContentFeatures;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.nlp.ageattr.util.MapUtil;

public class BigramAnalysis extends AbstractBigramAnalysis{

	 	 
	 public BigramAnalysis() {
		// TODO Auto-generated constructor stub
		 super();
		 this.output= "output\\bigram\\";
	 }
	 
	 	 
	 protected void updateMap(String ageclass, String line) {
		// TODO Auto-generated method stub
		HashMap<String, Double> hashmap = this.ageMap.get(ageclass);
		String[] words = line.toLowerCase().replaceAll("[:.,*!?\"\\;'{}()`~@#$%^&|-]", " ").split("\\s+");
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
	 
	
	 
	public static void main(String[] args) {
		BigramAnalysis analysis = new BigramAnalysis();
		analysis.run(75);
	}

	
	 
}
