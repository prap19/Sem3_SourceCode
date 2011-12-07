package edu.nlp.ageattr.interfaces;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import edu.nlp.ageattr.PopulateFile;
import edu.nlp.ageattr.SVNInterface;
import edu.nlp.ageattr.helper.LexicalContentFeatures.BigramAnalysis;
import edu.nlp.ageattr.processDataset.CreateDataset;

public class BigramFeature implements SVNInterface {

	public final static String input = "top150\\bigram\\top150words.txt"; 
	private HashMap<String, String> hashmap = new HashMap<String, String>();
	private HashMap<String, Double> wordMap = new HashMap<String, Double>();
	private Double count=0.0;
	
	public BigramFeature() {
		// TODO Auto-generated constructor stub
		BufferedReader br = null;
		String line;
		 
		try {
			br= new BufferedReader(new FileReader(new File(input)));
			while((line=br.readLine())!=null) {
				String[] words = line.split("\\s+");
				this.hashmap.put(words[0], words[1]);
				this.wordMap.put(words[1], 0.0);
				words=null;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public int getNumberOfAttributes() {
		// TODO Auto-generated method stub
		return this.hashmap.size();
	}

	@Override
	public void addAttributes(FastVector fastVector,
			HashMap<String, Integer> featureList) {
		// TODO Auto-generated method stub
		Integer index = featureList.size();
		Iterator it = this.hashmap.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry<String, String> entry = (Entry<String, String>) it.next();
			fastVector.addElement(new Attribute(entry.getKey()));
			featureList.put(entry.getKey(), index++);
		}
		
	}

	@Override
	public void addVector(FastVector fastVector, Instance trainInstance,
			boolean train, PopulateFile populateFile,
			HashMap<String, Integer> featureList) {
		// TODO Auto-generated method stub
		File blogger;
		if(train==true){
			blogger = populateFile.getFileMap(RSRC_TRAIN_TXT);
		}
		else {
			blogger = populateFile.getFileMap(RSRC_TEST_TXT);
		}
		
		BufferedReader br = null;
		String line;
		try {
			br= new BufferedReader(new FileReader(blogger));
			while((line=br.readLine()) != null) {
				line= line.toLowerCase().replaceAll("[/./?/!/:/;/-/—/(/)/[/]/’/“/”///,/`/']", "");
				if(line.length() >0) {
					String s[] = line.split("\\s+");
					String[] bigrams= BigramAnalysis.getAllBigrams(s);
					this.count+= bigrams.length;
					for(int i=0; i< bigrams.length; i++){
						if(this.wordMap.containsKey(bigrams[i])) {
							this.wordMap.put(bigrams[i], this.wordMap.get(bigrams[i])+1);
						}
					}
				}
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		this.normalizeMap();
		
		/*
		 * populate the feature vector
		 */
		Iterator iterator = this.wordMap.keySet().iterator();
		while (iterator.hasNext()) {  
			String key = iterator.next().toString();
			trainInstance.setValue((Attribute)fastVector.elementAt(featureList.get(key)), wordMap.get(key));   
		}
		 trainInstance.setValue((Attribute)fastVector.elementAt(fastVector.size()-1), CreateDataset.getBloggerAge(CreateDataset.proceesFileName(blogger)));
		
	}

	
	private void normalizeMap() {
		
		Iterator it = this.wordMap.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry<String, Double> entry = (Entry<String, Double>)it.next();
			this.wordMap.put(entry.getKey(), entry.getValue()/this.count);
		}
	}
	
	@Override
	public File getDataFile(String FolderName) {
		// TODO Auto-generated method stub
		return null;
	}

}
