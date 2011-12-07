package edu.nlp.ageattr.helper.LexicalContentFeatures;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import edu.nlp.ageattr.helper.StopWord;



public class TopWords {
	public StopWord stopWord;
	public static HashMap<String , String> TopWordMap;
	public int teenCount = 0;
	public int twentiesCount = 0;
	public int thirtiesCount = 0;
	public float range = 10;
	static final File TeensFileWordFreqNormalised = new File("C://Data//AgePredictionDataset//ConcatenatedFiles//1000NormalisedTeensWordFreuency.txt");
	static final File TwentiesFileWordFreqNormalised = new File("C://Data//AgePredictionDataset//ConcatenatedFiles//1000NormalisedTwentiesWordFreuency.txt");
	static final File ThiriesFileWordFreqNormalised = new File("C://Data//AgePredictionDataset//ConcatenatedFiles//1000NormalisedThirteesWordFreuency.txt");
	
	
	static final File Top150WordFreq = new File("top150\\unigrams\\top150words.txt");
	
	static final File TeensFile50WordFreq = new File(System.getenv("NLP")+"//ConcatenatedFiles//50OrderedTeensFileTrainOut.txt");
	static final File TwentiesFile50WordFreq = new File(System.getenv("NLP")+"//ConcatenatedFiles//50OrderedTwentiesFileTrainOut.txt");
	static final File ThiriesFile50WordFreq = new File(System.getenv("NLP")+"//ConcatenatedFiles//50OrderedThirteesFileTrainOut.txt");

	public TopWords(){
		stopWord = new StopWord();
		stopWord.StopWordCreate();
	}
	/**
	 * creates a map of the top 50 most frequently occuring words from each age group 
	 */
	public void create(){
		TopWordMap = new HashMap<String, String>();
		
		LinkedHashMap<String , Float> teenMap = new LinkedHashMap<String, Float>();
		LinkedHashMap<String , Float> twentiesMap = new LinkedHashMap<String, Float>();
		LinkedHashMap<String , Float> thirteesMap = new LinkedHashMap<String, Float>();
		
		teenMap = createTop500Map(TeensFileWordFreqNormalised);
		twentiesMap = createTop500Map(TwentiesFileWordFreqNormalised);
		thirteesMap = createTop500Map(ThiriesFileWordFreqNormalised);
		
		BufferedWriter bufferedWriter = null;
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(Top150WordFreq));
			updateWordMap(TopWordMap,teenMap,twentiesMap,thirteesMap,"teens","twenties","thirties",bufferedWriter);
			updateWordMap(TopWordMap,twentiesMap,teenMap,thirteesMap,"twenties","teens","thirties",bufferedWriter);
			updateWordMap(TopWordMap,thirteesMap,twentiesMap,teenMap,"thirties","twenties","teens",bufferedWriter);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				bufferedWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		System.out.println("ther");
	}


	private void updateWordMap(HashMap<String, String> topWordMap,
			LinkedHashMap<String, Float> AMap,
			LinkedHashMap<String, Float> BMap,
			LinkedHashMap<String, Float> CMap, String aClassName,String bClassName,String cClassName, BufferedWriter bufferedWriter) {
			Iterator iterator = AMap.entrySet().iterator();  
			while (iterator.hasNext() ) {
				Map.Entry entry = (Entry) iterator.next();
				iterator.remove();
				String key = entry.getKey().toString();
				float value = (Float) entry.getValue();
				//AMap.remove(key);
				String classLabel =  getClassLabelForWord(value, key, BMap, CMap,bClassName,cClassName,aClassName);
				if(!(classLabel.equals("wordNotInRange"))){
					if(!isCount50(classLabel)){
						updateClassCounts(classLabel);	
						topWordMap.put(key, classLabel);
						try {
							bufferedWriter.write(classLabel+" "+key);
							bufferedWriter.newLine();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				}
				/**
				 * even if the word is not in range remove it from all the maps
				 */
				if(classLabel.equals("wordNotInRange")){
					if(BMap.containsKey(key)){
						BMap.remove(key);
					}
					if(CMap.containsKey(key)){
						CMap.remove(key);
					}
				}
				/**
				 * if the word is included in the unique word list remove it from all the maps
				 */
				if(BMap.containsKey(key) && topWordMap.containsKey(key))
				BMap.remove(key);
				if(CMap.containsKey(key) && topWordMap.containsKey(key))
				CMap.remove(key);
				
			}
		
	}


	private boolean canUpdateMap(String classLabel) {
		
		return false;
	}
	private boolean isCount50(String className) {
		if(className.equalsIgnoreCase("teens")){
			if(teenCount>=50)
				return true;
		}else if(className.equalsIgnoreCase("twenties")){
			if(twentiesCount>=50)
				return true;
		}else if(className.equalsIgnoreCase("thirties")){
			if(thirtiesCount>=50)
				return true;
		}
		return false;
	}


	private void updateClassCounts(String classLabel) {
		if(classLabel.equalsIgnoreCase("teens")){
			teenCount++;
		}else if(classLabel.equalsIgnoreCase("twenties")){
			twentiesCount++;
		}else{
			thirtiesCount++;
		}
	}


	private String getClassLabelForWord(float value, String key,
			LinkedHashMap<String, Float> bMap,
			LinkedHashMap<String, Float> cMap, String bClassName, String cClassName, String aClassName) {
		String classLabel="";
		float bValue = -1,cValue =-1;
		if(bMap.containsKey(key) && !isCount50(bClassName)){
			 bValue = bMap.get(key);
		}
		if(cMap.containsKey(key) && !isCount50(cClassName)){
			 cValue = cMap.get(key);
		}
		
		if(!isWithinRange(value,bValue) && !isWithinRange(value,cValue) && !isWithinRange(cValue,bValue)){
			if(value> bValue && value>cValue ) return aClassName;
			else if(bValue> value && bValue > cValue) return bClassName;
			else return cClassName;
		}
		else
			return "wordNotInRange";
	}


	private boolean isWithinRange(float value, float bValue) {
		float val = Math.abs(value - bValue);
		if(value ==-1 || bValue == -1)
		return false;
		if(val*10000 > range)	
			return false;
		return true;
		
	}
	private LinkedHashMap<String, Float> createTop500Map(
			File inputFile) {
		LinkedHashMap< String, Float> map = new LinkedHashMap<String, Float>();
		int count =0;
		BufferedReader input = null;
		try {
			input = new BufferedReader(new FileReader(inputFile));
			String line ="";
			while((line = input.readLine()) != null && count!=1500){
			
				String words[] = line.split("\\s");
				if(StopWord.isStopWord(words[0]) == 0){
					count++;
					map.put(words[0], Float.parseFloat(words[1]));
				}
				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				input.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
		return map;
	}

	
}
