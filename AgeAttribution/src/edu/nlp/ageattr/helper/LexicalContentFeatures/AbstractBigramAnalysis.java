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

public abstract class AbstractBigramAnalysis {
	
	protected String dataset = System.getenv("NLP")+"\\1500TrainTextFiles"; 
	protected String output = "output\\";
	 
	 protected HashMap<String, HashMap<String, Double>> ageMap;
	 protected HashMap<String, Double> mapTeens;
	 protected HashMap<String, Double> map20s;
	 protected HashMap<String, Double> map30s;
	 
	 protected Double wordTeen = 0.0;
	 protected Double word20s = 0.0;
	 protected Double word30s = 0.0;

	 public AbstractBigramAnalysis() {
		// TODO Auto-generated constructor stub
		 ageMap = new HashMap<String, HashMap<String,Double>>(10, (float)0.3);
		 mapTeens = new LinkedHashMap<String, Double>(10, (float)0.3);
		 map20s = new LinkedHashMap<String, Double>(10, (float)0.3);
		 map30s = new LinkedHashMap<String, Double>(10, (float)0.3);
		 
		 ageMap.put("teens", mapTeens);
		 ageMap.put("twenties", map20s);
		 ageMap.put("thirties", map30s);
		 System.out.println("AgeMap is initialized: "+this.ageMap.size());
	}
	 
	 public void process() {
		 System.out.println("Reading dataset from "+ this.dataset);
		 BufferedReader br = null;
		 File dir =  new File(dataset);
		 String line="";
		 if(dir.isDirectory()) {
			 File[] files = dir.listFiles();
			 for(int i=0; i<files.length; i++) {
				 try {
					br = new BufferedReader(new FileReader(files[i]));
					String ageclass = BigramAnalysis.getAuthorAge(files[i].getName());
					System.out.println(i+"# "+files[i].getName());
					while((line = br.readLine()) != null) {
						updateMap(ageclass, line);
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally {
					try {
						br.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				 
			 }
		 } else
			try {
				throw new Exception("Dataset path is not a directory: path: "+dataset);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 
	 }	 
	 

	 public void writeintoFile(){
		 
		 Iterator mitr = this.ageMap.entrySet().iterator();
		 BufferedWriter bw = null;
		 while(mitr.hasNext()){
			 try {
				 Map.Entry<String, HashMap<String, Double>> entry = (Entry<String, HashMap<String,Double>>)mitr.next();
				 bw= new BufferedWriter(new FileWriter(new File(output+entry.getKey()+".txt")));
				 Iterator eitr = entry.getValue().entrySet().iterator();
				 while(eitr.hasNext()) {
					 Map.Entry<String, Double> entry1 =(Entry<String, Double>)eitr.next();
					 if(this.getCountObj(entry.getKey()).equals(new Double(0.0)))
						 throw new Exception("Count zero for : "+entry.getKey());
					 bw.write(entry1.getKey()+" "+entry1.getValue()/this.getCountObj(entry.getKey()));
					 bw.newLine();
				 }
				 bw.close();
				 
				 //Reducing heap pressure by removing hashmap objects
				 mitr.remove();
				 
			 }catch(Exception e) {
				 e.printStackTrace();
			 }
		 }
			 
	 }
	 
	 public void sortMaps(int threshold) {
			// TODO Auto-generated method stub
			System.err.println("Sorting started");
			this.removeSmallCounts(threshold);
			//System.out.println("teens: "+mapTeens.size()+" 20s:"+map20s.size()+" 30s:"+map30s.size());
			Iterator it = this.ageMap.entrySet().iterator();
			while(it.hasNext()) {
				Map.Entry<String, LinkedHashMap<String, Double>> entry = (Entry<String, LinkedHashMap<String,Double>>) it.next();
				ageMap.put(entry.getKey(), MapUtil.sortHashMap(entry.getValue()));
				System.gc();
				System.out.println(entry.getKey()+" Map sorted!");
			}
			
			System.err.println("Sorting done");
		}
	 
	 /**
		 * Removes entries less than 75
		 * 
		 */
		private void removeSmallCounts(int threshold) {
			// TODO Auto-generated method stub
			Iterator it = this.ageMap.entrySet().iterator();
			while(it.hasNext()) {
				Map.Entry<String, LinkedHashMap<String, Double>> entry = (Entry<String, LinkedHashMap<String,Double>>) it.next();
				Double count = this.getCountObj(entry.getKey());
				Iterator itr = entry.getValue().entrySet().iterator();
				while(itr.hasNext()) {
					Map.Entry<String, Double> entry1 = (Entry<String, Double>)itr.next();
					if(entry1.getValue() < 75)
						itr.remove();
					else
						count+=entry1.getValue();
					
				}
				System.gc();
				this.setObjCount(entry.getKey(), count);
				System.out.println(entry.getKey()+" Map removed! count="+count);
			}
		}
	 
		public static String[] getAllBigrams(String[] words) {
			// TODO Auto-generated method stub
			
			List<String> res = new ArrayList<String>();
			for(int i=0,j=i+1; j<words.length;i++,j++) {
				res.add(words[i]+words[j]);
			}
			
			return (String[]) res.toArray(new String[0]);
		}

		
		

		private void setObjCount(String key, Double count) {
			// TODO Auto-generated method stub
			if(key.equals("twenties"))
			{
				this.word20s= count;
				return;
			}
			if(key.equals("thirties"))
			{
				this.word30s=count;
				return;
			}
			
			this.wordTeen=count;
			
		}

		private Double getCountObj(String key) {
			// TODO Auto-generated method stub
			
			Double res = this.wordTeen;
			if(key.equals("twenties"))
				return this.word20s;
			if(key.equals("thirties"))
				return this.word30s;
			return this.wordTeen;
		}

		public void normalizeMaps() {
			
			Iterator it = this.ageMap.entrySet().iterator();
			while(it.hasNext()) {
				Map.Entry<String, LinkedHashMap<String, Double>> entry = (Entry<String, LinkedHashMap<String,Double>>) it.next();
				
				Iterator itr = entry.getValue().entrySet().iterator();
				while(itr.hasNext()) {
					Map.Entry<String, Double> entry1 = (Entry<String, Double>)itr.next();
					
				}
				System.gc();
				
				System.out.println(entry.getKey()+" Map normalized!");
			}
		}
		
		
	 public void displayMap(Map<String, Double> map) {
		 map = this.map20s;
		Iterator it = map.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry<String, Double> entry = (Entry<String, Double>) it.next();
			System.out.println(entry.getKey()+": "+entry.getValue());
		}
	 }
	 
	 abstract protected void updateMap(String ageClass, String line);
	 
	 
	 public static String getAuthorAge(String filename) {
		 Integer age = Integer.parseInt(filename.split("\\.")[2]);
		 String res = "teens";
		 if(age>=23 && age<=27)
			 res = "twenties";
		 if(age>27)
			 res= "thirties";
		 
		 return res;
		 
	 }
	 
	 
	 public final void run(int threshold) {
		 	Long time = System.currentTimeMillis();
			this.process();
			this.sortMaps(threshold);
			this.normalizeMaps();
			this.writeintoFile();
			//analysis.displayMap(new HashMap<String, Double>());
			time = (System.currentTimeMillis() - time)/1000;
			System.out.println("Time taken: "+time/60+" mins and "+time%60+" sec");
	 }
	 
	 
	 
	 
}
