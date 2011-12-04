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
	static final File TeensFileWordFreq = new File(System.getenv("NLP")+"//ConcatenatedFiles//OrderedLowerTeensFileTrainOut.txt");
	static final File TwentiesFileWordFreq = new File(System.getenv("NLP")+"//ConcatenatedFiles//OrderedLowerTwentiesFileTrainOut.txt");
	static final File ThiriesFileWordFreq = new File(System.getenv("NLP")+"//ConcatenatedFiles//OrderedLowerThirteesFileTrainOut.txt");
	
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
//	public void create(){
//		TopWordMap = new HashMap<String, String>();
//		createTop50FrequentUniqueWordsFile(TeensFileWordFreq, TeensFile50WordFreq,TopWordMap,TwentiesFileWordFreq,ThiriesFileWordFreq,"teens");
//		createTop50FrequentUniqueWordsFile(TwentiesFileWordFreq, TwentiesFile50WordFreq,TopWordMap,TeensFileWordFreq,ThiriesFileWordFreq,"twenties");
//		createTop50FrequentUniqueWordsFile(ThiriesFileWordFreq, ThiriesFile50WordFreq,TopWordMap,TwentiesFileWordFreq,TeensFileWordFreq,"thirtees");
//	}
//	
//	/**
//	 * creates a output file containing the top 50 frequently occcuring words for a input class and updates the global word map
//	 * @param InputFile
//	 * @param OutputFile
//	 * @param topWordMap
//	 * @param ClassBFile
//	 * @param ClassCFile
//	 * @param string 
//	 */
//	private void createTop50FrequentUniqueWordsFile(File InputFile, File OutputFile, HashMap<String,String> topWordMap, File ClassBFile, File ClassCFile, String classLabel ){
//		HashSet<String> ClassB = CreateTop50WordsSet(ClassBFile);
//		HashSet<String> ClassC = CreateTop50WordsSet(ClassCFile);
//		BufferedReader input = null;
//		BufferedWriter output = null;
//		int count=0;
//		try {
//			input = new BufferedReader(new FileReader(InputFile));
//			output = new BufferedWriter(new FileWriter(OutputFile));
//			String line ="";
//			while((line = input.readLine()) != null && count!=50){
//				String words[] = line.split("\\s");
//				if(!(ClassB.contains(words[0]) && ClassC.contains(words[0]))){
//					output.write(line);
//					output.newLine();
//					topWordMap.put(words[0], classLabel);
//					count++;
//				}
//			}
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}finally{
//			
//			try {
//				input.close();
//				output.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		
//		
//	}
//	private HashSet<String> CreateTop50WordsSet(File InputFile) {
//		int count =0;
//		BufferedReader input = null;
//		HashSet<String> WordSet = new HashSet<String>();
//		try {
//			input = new BufferedReader(new FileReader(InputFile));
//			String line ="";
//			while((line = input.readLine()) != null && count!=50){
//				count++;
//				String words[] = line.split("\\s");
//				WordSet.add(words[0]);
//				
//			}
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}finally{
//			try {
//				input.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		
//		return WordSet;
//		
//	}
	
	public void create(){
		TopWordMap = new HashMap<String, String>();
		
		LinkedHashMap<String , Integer> teenMap = new LinkedHashMap<String, Integer>();
		LinkedHashMap<String , Integer> twentiesMap = new LinkedHashMap<String, Integer>();
		LinkedHashMap<String , Integer> thirteesMap = new LinkedHashMap<String, Integer>();
		
		teenMap = createTop150Map(TeensFileWordFreq);
		twentiesMap = createTop150Map(TwentiesFileWordFreq);
		thirteesMap = createTop150Map(ThiriesFileWordFreq);
		
		updateWordMap(TopWordMap,teenMap,twentiesMap,thirteesMap,"teens","twenties","thirties");
		updateWordMap(TopWordMap,twentiesMap,teenMap,thirteesMap,"twenties","teens","thirties");
		updateWordMap(TopWordMap,thirteesMap,twentiesMap,teenMap,"thirties","twenties","teens");
		
		
	}


	private void updateWordMap(HashMap<String, String> topWordMap,
			LinkedHashMap<String, Integer> AMap,
			LinkedHashMap<String, Integer> BMap,
			LinkedHashMap<String, Integer> CMap, String aClassName,String bClassName,String cClassName) {
			Iterator iterator = AMap.entrySet().iterator();  
			while (iterator.hasNext() && ! isCount50(aClassName)) {
				Map.Entry entry = (Entry) iterator.next();
				iterator.remove();
				String key = entry.getKey().toString();
				int value = (Integer) entry.getValue();
				//AMap.remove(key);
				String classLabel =  getClassLabelForWord(value, key, BMap, CMap,bClassName,cClassName,aClassName);
				updateClassCounts(classLabel);
				topWordMap.put(key, classLabel);
				if(BMap.containsKey(key))
				BMap.remove(key);
				if(CMap.containsKey(key))
				CMap.remove(key);
				
			}
		
	}


	private boolean isCount50(String className) {
		if(className.equalsIgnoreCase("teens")){
			if(teenCount==50)
				return true;
		}else if(className.equalsIgnoreCase("twenties")){
			if(twentiesCount==50)
				return true;
		}else if(className.equalsIgnoreCase("thirties")){
			if(thirtiesCount==50)
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


	private String getClassLabelForWord(int value, String key,
			LinkedHashMap<String, Integer> bMap,
			LinkedHashMap<String, Integer> cMap, String bClassName, String cClassName, String aClassName) {
		String classLabel="";
		int bValue = -1,cValue =-1;
		if(bMap.containsKey(key)){
			 bValue = bMap.get(key);
		}
		if(cMap.containsKey(key)){
			 cValue = cMap.get(key);
		}
		if(value> bValue && value>cValue) return aClassName;
		else if(bValue> value && bValue > cValue) return bClassName;
		else return cClassName;
		
	}


	private LinkedHashMap<String, Integer> createTop150Map(
			File inputFile) {
		LinkedHashMap< String, Integer> map = new LinkedHashMap<String, Integer>();
		int count =0;
		BufferedReader input = null;
		try {
			input = new BufferedReader(new FileReader(inputFile));
			String line ="";
			while((line = input.readLine()) != null && count!=150){
				
				String words[] = line.split("\\s");
				if(StopWord.isStopWord(words[0]) == 0){
					count++;
					map.put(words[0], Integer.parseInt(words[1]));
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
