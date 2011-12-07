package edu.nlp.ageattr.examples;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import edu.nlp.ageattr.PopulateFile;
import edu.nlp.ageattr.SVNInterface;
import edu.nlp.ageattr.helper.LexicalContentFeatures.TopWords;
import edu.nlp.ageattr.processDataset.CreateDataset;

public class BagOfWords implements SVNInterface{

	TopWords topWords;
	
	@Override
	public int getNumberOfAttributes() {
		topWords = new TopWords();
		topWords.create();
		return 150;
	}

	@Override
	public void addAttributes(FastVector fastVector,HashMap<String,Integer> featureList) {
		Iterator iterator = TopWords.TopWordMap.keySet().iterator();
		int featureCount = featureList.size();
		while (iterator.hasNext()) {  
			String key = iterator.next().toString();
			Attribute attribute = new Attribute(key);
			fastVector.addElement(attribute);
			featureList.put(key, featureCount++);
		}
		System.out.println("done");
		
		
	}

	@Override
	public void addVector(FastVector fastVector, Instance trainInstance,
			boolean train, PopulateFile populateFile,HashMap<String,Integer> featureList) {
		HashMap<String, Float> WordFrequencyMap = new HashMap<String, Float>();
		initializeMap(WordFrequencyMap);
		File blogger;
		int countOfWords = 0;
		
		if(train == true){
			blogger = populateFile.getFileMap(RSRC_TRAIN_TXT);
		}
		else{
			blogger = populateFile.getFileMap(RSRC_TEST_TXT);
		}
		BufferedReader bufferedReader = null;
			try {
				bufferedReader = new BufferedReader(new FileReader(blogger));
				String line="";
				countOfWords =0;
				while((line = bufferedReader.readLine())!=null){
					//line = line.replaceAll("\\W+", "");
					line = line.replaceAll("[/./?/!/:/;/-/—/(/)/[/]/’/“/”///,/`/']", "");
					String s[] = line.split("\\s+");
					countOfWords+= s.length;
					for(int i=0;i<s.length;i++){
						if(topWords.TopWordMap.containsKey(s[i].toLowerCase())){
						updateWordFreqMap(WordFrequencyMap,s[i].toLowerCase());
						}
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
					bufferedReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			String fileName = CreateDataset.proceesFileName(blogger);
			Iterator iterator = WordFrequencyMap.keySet().iterator();
			while (iterator.hasNext()) {  
				String key = iterator.next().toString();
				float val = WordFrequencyMap.get(key);
				val = val/countOfWords;
				WordFrequencyMap.put(key, val);
				
			}
			/*
			 * populate the feature vector
			 */
			iterator = TopWords.TopWordMap.keySet().iterator();
			while (iterator.hasNext()) {  
				String key = iterator.next().toString();
				trainInstance.setValue((Attribute)fastVector.elementAt(featureList.get(key)), WordFrequencyMap.get(key));   
			}
			 trainInstance.setValue((Attribute)fastVector.elementAt(fastVector.size()-1), CreateDataset.getBloggerAge(fileName));
			
		
	}

	private void updateWordFreqMap(HashMap<String, Float> wordFrequencyMap,
			String key) {
		String lowerKey = key.toLowerCase();
		if(wordFrequencyMap.containsKey(lowerKey)){
			float val = wordFrequencyMap.get(lowerKey);
			wordFrequencyMap.put(lowerKey, val+1);
		}else{
			wordFrequencyMap.put(lowerKey, (float)1.0);
		}
		
	}

	private void initializeMap(HashMap<String, Float> wordFrequencyMap) {
		Iterator iterator = TopWords.TopWordMap.keySet().iterator();
		
		while (iterator.hasNext()) {  
			String key = iterator.next().toString();
			wordFrequencyMap.put(key.toLowerCase(), (float)0.0);
		}
		
	}

	@Override
	public File getDataFile(String FolderName) {
		// TODO Auto-generated method stub
		return null;
	}

}
