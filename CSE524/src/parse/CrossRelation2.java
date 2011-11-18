package parse;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.xeustechnologies.googleapi.spelling.SpellChecker;
import org.xeustechnologies.googleapi.spelling.SpellCorrection;
import org.xeustechnologies.googleapi.spelling.SpellResponse;


/**
 * 
 * @author prasad
 *
 */
public class CrossRelation2 {

	// stores word count per image. Format: <imageName, <word, count>>
	private LinkedHashMap<String, LinkedHashMap<String, Integer>> wordCountMap = new LinkedHashMap<String, LinkedHashMap<String,Integer>>(); 
	
	// stores word vs index. index implies the feature vector position index
	private LinkedHashMap<String, Integer> wordIndexMap = new LinkedHashMap<String, Integer>();
	
	//  contains all the word tags form the list. <word, count>
	private LinkedHashMap<String, Integer> totalWord = new LinkedHashMap<String, Integer>();
	
	// Feature vector <imageName, [comparisonValue]>
	private LinkedHashMap<String, ArrayList<Float>> vectors = new LinkedHashMap<String, ArrayList<Float>>();
	
	// Feature vector <image,  <image, comparisonValue>>
	private LinkedHashMap<String, LinkedHashMap<String, Float>> compResultList = new LinkedHashMap<String, LinkedHashMap<String,Float>>();
	
	// <image <tags, count>>
	private LinkedHashMap<String, LinkedHashMap<String, Integer>> commonTagMap = new LinkedHashMap<String, LinkedHashMap<String,Integer>>();
	
	private static SpellChecker checker = new SpellChecker();
	private static SpellResponse response;
	private Integer index=0;
	private Integer MIN_IMAGE_TAGS=0;
	private Integer TOTAL_NUMBER_IMAGES=4785;
	private Integer TOTAL_NUMBER_OF_COMMON_TAGS =3;
	
	// change this to change the number of words to be considered for feature vector. It takes top VECTOR_SIZE form totalWords that is sorted according to count
	private Integer VECTOR_SIZE=150;
	
	// this value implies =  outputs all those images which have comparison value greater than this. 
	private Float THRESHOLD = (float)0.3;
	
	/**
	 * Switches off all the loggers
	 */
	public CrossRelation2() {
		// TODO Auto-generated constructor stub
		List<Logger> loggers = Collections.<Logger>list(LogManager.getCurrentLoggers());
		loggers.add(LogManager.getRootLogger());
		for ( Logger logger : loggers ) {
		    logger.setLevel(Level.OFF);
		}
		//System.out.println("LOGGING OFF");
		
	}
	
	/**
	 * Corrects the spelling of word
	 * 
	 * @param word
	 * @return 1st meaning 
	 */
	public static String correctSpell(String word)
	{
		String result = null;
		try {
		response = checker.check(word);
		if(response !=null)
		{
			SpellCorrection[] spelling = response.getCorrections();
			if(spelling != null)
			{
				result = spelling[0].getValue().split("\\t")[0];
			//	System.err.println("result");
			}
		}
		}
		catch(Exception e)
		{
			result = word;
			System.err.println(e.getMessage());
			//e.printStackTrace();
			//System.exit(0);
		}
		return result;
	}
	
	
	/**
	 *  Gets a cross product of one image vector with rest of image vectors and stores the comparison value
	 */
	public void correlate()
	{
		Iterator<Entry<String, ArrayList<Float>>> vectItr1 = this.vectors.entrySet().iterator();
		while(vectItr1.hasNext())
		{
			Map.Entry<String, ArrayList<Float>> entry = vectItr1.next();
			ArrayList<Float> arr1 = entry.getValue();
			Iterator<Entry<String, ArrayList<Float>>> restItr = this.vectors.entrySet().iterator();
			while(restItr.hasNext())
			{
				Map.Entry<String, ArrayList<Float>> entry2 = restItr.next();
				if(entry.getKey().equals(entry2.getKey()) !=true)
				{
					ArrayList<Float> arr2 = entry2.getValue();
					Float compval = this.compareArr(entry.getKey(), arr1, arr2);
					if(compval > Float.valueOf((float)THRESHOLD))
					{
						LinkedHashMap<String, Float> tempMap;
						if(this.compResultList.containsKey(entry.getKey()))
						{
							tempMap = this.compResultList.get(entry.getKey());
						}
						else 
						{
							tempMap = new LinkedHashMap<String, Float>();
							this.compResultList.put(entry.getKey(), tempMap);
						}
						tempMap.put(entry2.getKey(), compval);
					}
				}
			}
		}
		
	}
	
	private String formImageTag(String imageURL)
	{
		String res = "";
		res= "<img src=\"http://recognition.cs.stonybrook.edu\\~"+imageURL+"\"></img>";
		return res;
	}
	
	/**
	 * Generates HTML files of images that have comparison value greater than THRESHOLD
	 * @throws IOException
	 */
	public void createHTMLFiles() throws IOException
	{
		BufferedReader br = null;
		BufferedWriter bw = null;
		BufferedWriter indexWriter = null;
		int count =0;
		String htmlHead = "<html>\n<body>\n";
		String htmlFoot = "</body>\n</html>";
		try {
			br = new BufferedReader(new FileReader(new File("comparisonresultforhtml.txt")));
			indexWriter = new BufferedWriter(new FileWriter(new File("HTML\\index.html")));
			indexWriter.write(htmlHead+"\n");
			String line;
			while((line = br.readLine()) != null)
			{		
					bw = new BufferedWriter(new FileWriter(new File("HTML\\"+count+".html")));
					String[] linesplit = line.trim().split("\\t");
					String image = linesplit[0];
					String[] simages = linesplit[1].substring(0, linesplit[1].length()-1).split(";");
					
					String res = htmlHead+"\n<h2>Given Image</h2>"+this.formImageTag(image)+"\n"+"<br />\n";
					
					Iterator<Entry<String, Integer>> it = ParseMain.sortHashMap(this.wordCountMap.get(image)).entrySet().iterator();
					while(it.hasNext())
					{
						Map.Entry<String, Integer> entry = it.next();
						res += entry.getKey()+"("+entry.getValue()+")&nbsp;&nbsp;";
					}
					
					res+= "<hr /><h3>Similar Images</h3>";
					for(String simage: simages)
					{
						res+="(comparison value= "+this.compResultList.get(image).get(simage)+")<br />\n";
						res+= this.formImageTag(simage)+"<br /><br />";
						Iterator<Entry<String, Integer>> it1 = ParseMain.sortHashMap(this.wordCountMap.get(simage)).entrySet().iterator();
						res += "<div>";
						while(it1.hasNext())
						{
							Map.Entry<String, Integer> entry = it1.next();
							res += entry.getKey()+"("+entry.getValue()+")&nbsp;&nbsp;";
						}
						res += "</div><hr /"; 
					}
					indexWriter.write("\t<a href=\""+count+".html\" target=\"_blank\" >image-"+count+"</a><br />\n     ");
					indexWriter.newLine();
					indexWriter.newLine();
					res+= htmlFoot;
					bw.write(res);
					count++;
					bw.close();
			}
			indexWriter.write(htmlFoot);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				br.close();
				indexWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * Generates a text file that has image and list of image url that are similar. 
	 */
	public void createHTMLOutput()
	{
		System.out.println("Creating HTML output");
		Iterator<Entry<String, LinkedHashMap<String, Float>>> resItr = this.compResultList.entrySet().iterator();
		System.out.println("COMPRESSIZE: "+this.compResultList.size());
		BufferedWriter bw = null;
		int count=0;
		try {
			bw = new BufferedWriter(new FileWriter(new File("comparisonresultforhtml.txt")));
			while(resItr.hasNext())
			{
				Map.Entry<String, LinkedHashMap<String, Float>> entry = resItr.next();
				//System.out.println("Displaying Simarity:"+entry.getKey());
				
				bw.write(entry.getKey()+"\t");
				LinkedHashMap<String, Float> tempMap = entry.getValue();
				Iterator<Entry<String, Float>> tmpItr = tempMap.entrySet().iterator();
				while(tmpItr.hasNext())
				{
					Map.Entry<String, Float> valEntry = tmpItr.next();
					//System.out.println(valEntry.getKey()+" : "+valEntry.getValue());
					bw.write(valEntry.getKey()+";");
				}
				
				//bw.write("|");
				bw.newLine();
				count++;
			}
		} catch(Exception e)
		{
			e.printStackTrace();
		}
		finally {
			try {
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	private void displaySimilarity()
	{
		Iterator<Entry<String, LinkedHashMap<String, Float>>> resItr = this.compResultList.entrySet().iterator();
		System.out.println("COMPRESSIZE: "+this.compResultList.size());
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(new File("comparisonresult.txt")));
			while(resItr.hasNext())
			{
				Map.Entry<String, LinkedHashMap<String, Float>> entry = resItr.next();
				//System.out.println("Displaying Simarity:"+entry.getKey());
				
				bw.write("For: "+entry.getKey());
				bw.newLine();
				bw.write("----------------------------------------\n");
				LinkedHashMap<String, Float> tempMap = entry.getValue();
				Iterator<Entry<String, Float>> tmpItr = tempMap.entrySet().iterator();
				while(tmpItr.hasNext())
				{
					Map.Entry<String, Float> valEntry = tmpItr.next();
					//System.out.println(valEntry.getKey()+" : "+valEntry.getValue());
					bw.write(valEntry.getKey());
					bw.newLine();
				}
				bw.write("========================================\n");
			}
		} catch(Exception e)
		{
			e.printStackTrace();
		}
		finally {
			try {
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * Computes comparison of 2 image vectors
	 * @param arr1 
	 * @param arr2
	 * @return
	 */
	private Float compareArr(String image,ArrayList<Float> arr1, ArrayList<Float> arr2)
	{
		Float res=(float)0.0;
		for(int i=0; i< this.VECTOR_SIZE; i++)
		{
			res+= Math.min(arr1.get(i), arr2.get(i));
		}
	//	System.out.println("COMPVAL: "+res);
		return res;
	}

	public void buildIndexMap()
	{
		Iterator<Entry<String, Integer>> it = this.totalWord.entrySet().iterator();
		for(int i=0; i<this.VECTOR_SIZE; i++)
		{
			Map.Entry<String, Integer> entry = it.next();
			this.wordIndexMap.put(entry.getKey(), this.index++);
		}
		
	}
	
	/**
	 * Generates normalized  feature vector for every image.
	 * 
	 */
	public void buildVector()
	{
		//Iterator it = this.vectors.entrySet().iterator();
		Iterator<Entry<String, LinkedHashMap<String, Integer>>> wcMapIt = this.wordCountMap.entrySet().iterator();
		while(wcMapIt.hasNext())
		{
			Map.Entry<String, LinkedHashMap<String, Integer>> entry = (Map.Entry<String, LinkedHashMap<String,Integer>>) wcMapIt.next();
			//Map.Entry<String, ArrayList<Float>> entry1 = (Map.Entry<String, ArrayList<Float>>) it.next();
			LinkedHashMap<String , Integer> hashmap = entry.getValue();
			ArrayList<Float> arr = this.vectors.get(entry.getKey());
		//	System.out.println(this.vectors.containsKey(entry.getKey())+" : "+entry.getKey());
			for(int i=0; i<this.VECTOR_SIZE; i++)
				arr.add(Float.valueOf((float)0));
			
			Iterator<Entry<String, Integer>> it1 = hashmap.entrySet().iterator();
			int sum=0;
			while(it1.hasNext())
			{
				Map.Entry<String, Integer> entry2= (Map.Entry<String, Integer>) it1.next();
				if(this.wordIndexMap.containsKey(entry2.getKey()))
					arr.set(this.wordIndexMap.get(entry2.getKey()), (float)entry2.getValue());
				sum+= entry2.getValue();
			}
			this.normalize(arr, sum);
		}
	}
	
	private void normalize(ArrayList<Float> arr, int sum)
	{
		for(int i=0; i<arr.size(); i++)
		{	
			arr.set(i, arr.get(i)/sum);
		}
		
	}
	
	
	public void findCommonTags()
	{
		Iterator wcItr = this.wordCountMap.entrySet().iterator();
		
		while(wcItr.hasNext()) {
			Map.Entry<String, LinkedHashMap<String, Integer>> entry = (Entry)wcItr.next();		// key = given Image  Value= list of tags
			LinkedHashMap<String, Integer> wordMap = entry.getValue();							// Tags of given image
			LinkedHashMap<String, Integer> tempMap = new LinkedHashMap<String, Integer>();
			Iterator wmapItr = wordMap.entrySet().iterator();
			while(wmapItr.hasNext()) {
				Map.Entry<String, LinkedHashMap<String, Integer>> entry1 = (Entry)wmapItr.next();
				tempMap.put(entry1.getKey(), 0);
			}
			this.commonTagMap.put(entry.getKey(), tempMap);
			
			LinkedHashMap<String, Float> tempCompRes =  this.compResultList.get(entry.getKey());		// all simalar images of given image
			Iterator tcResItr = tempCompRes.entrySet().iterator();
			while(tcResItr.hasNext()) {
				Map.Entry<String, Float> entry2 = (Map.Entry<String, Float>) tcResItr.next();
				LinkedHashMap<String, Integer> tempSimTags = this.wordCountMap.get(entry2.getKey());	// tags of similar images of given image(entry)
				Iterator tSimItr = tempSimTags.entrySet().iterator();
				while(tSimItr.hasNext()) {
					Map.Entry<String, LinkedHashMap<String, Integer>> entry3 = (Entry)tSimItr.next();
					if(tempMap.containsKey(entry3.getKey())){
						tempMap.put(entry3.getKey(), tempMap.get(entry3.getKey()) + 1);
					}
				}
			}
			tempMap = ParseMain.sortHashMap(tempMap);
		}
			
	}
	
	public void writeCommonTags() {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(new File("commontags.txt")));
			Iterator itr = this.commonTagMap.entrySet().iterator();
			while(itr.hasNext()) {
				Map.Entry<String, LinkedHashMap<String, Integer>> entry = (Map.Entry<String, LinkedHashMap<String,Integer>>) itr.next();
				String line="";
				LinkedHashMap<String, Float> simImages = this.compResultList.get(entry.getKey());
				Iterator simItr = simImages.entrySet().iterator();
				while(simItr.hasNext()) {
					Map.Entry<String, LinkedHashMap<String, Integer>> entry2 = (Map.Entry<String, LinkedHashMap<String,Integer>>)simItr.next();
					LinkedHashMap<String, Integer> tags = entry.getValue();
					Iterator tagsItr = tags.entrySet().iterator();
					while(tagsItr.hasNext()) {
						Map.Entry<String, Integer> entry3 = (Map.Entry<String, Integer>) tagsItr.next();
						line= entry.getKey()+":"+entry2.getKey()+"#"+entry3.getKey()+" ";
					}
					
				}
				bw.write(line.trim());
				bw.newLine();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	 
	
	/**
	 *  reads input file that contains image versus tags. It is expected that tags are spell corrected to get best results.
	 */
	public void processFile()
	{
		System.out.println("PROCESSING BEGIN");
		BufferedReader br = null;
		Integer lineno = 0;
		try {
			br= new BufferedReader(new FileReader(new File("data\\input.txt")));
			String line;
			
			while(lineno<this.TOTAL_NUMBER_IMAGES)
			{
				line = br.readLine().toLowerCase();
				String[] tokens = line.split("\\t");   // 3= image_name 4= tags
				System.out.println("#"+lineno+": "+tokens[0]+" "+tokens[1]);
				String[] tags = tokens[1].split(";");
				//System.out.println(Arrays.deepToString(tags));
				
				this.vectors.put(tokens[0], new ArrayList<Float>());
				
				LinkedHashMap<String, Integer> tempMap;
				if(this.wordCountMap.containsKey(tokens[0])) {
					tempMap = this.wordCountMap.get(tokens[0]);
				}
				else {
					tempMap= new LinkedHashMap<String, Integer>();
					this.wordCountMap.put(tokens[0], tempMap);
				}
				
				
				for(int i=0; i<tags.length; i++)
				{
					/*
					String corrected = correctSpell(tags[i]) ;
					if(corrected != null)
					{
						tags[i] = corrected; 
					}
					*/
					if(tempMap.containsKey(tags[i])){
						tempMap.put(tags[i], tempMap.get(tags[i])+1);
					}
					else {
						tempMap.put(tags[i], 1);
					}
					
					if(this.totalWord.containsKey(tags[i])){
						this.totalWord.put(tags[i], this.totalWord.get(tags[i])+1);
					}
					else {
						this.totalWord.put(tags[i], 1);
					}
					
				}
				tempMap = ParseMain.sortHashMap(tempMap);
				
				lineno++;
				if(tempMap.size() < this.MIN_IMAGE_TAGS)
					this.MIN_IMAGE_TAGS = tempMap.size();
			}
			//System.out.println(this.totalWord.size());
			this.totalWord = ParseMain.sortHashMap(this.totalWord);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				System.err.println(this.MIN_IMAGE_TAGS);
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	void displayVectors()
	{
		Iterator<Entry<String, ArrayList<Float>>> it = this.vectors.entrySet().iterator();
		while(it.hasNext())
		{
			Map.Entry<String, ArrayList<Float>> entry = (Map.Entry<String, ArrayList<Float>>) it.next();
			ArrayList<Float> arr = entry.getValue();
			System.err.println("SIZE:"+arr.size()+" "+entry.getKey()+ ":"+Arrays.deepToString(arr.toArray()));
			
			
		}
	}
		
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		CrossRelation2 crossRelation2 = new CrossRelation2();
		crossRelation2.processFile();
		crossRelation2.buildIndexMap();
		crossRelation2.buildVector();
		//crossRelation2.displayVectors();
		crossRelation2.correlate();
		//crossRelation2.displaySimilarity();
		crossRelation2.createHTMLOutput();
		crossRelation2.createHTMLFiles();
		crossRelation2.findCommonTags();
	}

}
