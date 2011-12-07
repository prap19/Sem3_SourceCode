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

public class BigramFeature implements SVNInterface {

	public final static String input = System.getenv("NLP")+"\\top150\\bigram\\top150.txt"; 
	public HashMap<String, String> hashmap = new HashMap<String, String>();
	public HashMap<String, Integer> wordMap = new HashMap<String, Integer>();
	
	public BigramFeature() {
		// TODO Auto-generated constructor stub
		BufferedReader br = null;
		String line;
		 
		try {
			br= new BufferedReader(new FileReader(new File(input)));
			while((line=br.readLine())!=null) {
				String[] words = line.split("\\s+");
				this.hashmap.put(words[0], words[1]);
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
		
		
	}

	@Override
	public File getDataFile(String FolderName) {
		// TODO Auto-generated method stub
		return null;
	}

}
