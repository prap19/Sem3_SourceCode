package parse;

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
import java.util.Map;
import java.util.Map.Entry;

import org.xeustechnologies.googleapi.spelling.SpellChecker;
import org.xeustechnologies.googleapi.spelling.SpellCorrection;
import org.xeustechnologies.googleapi.spelling.SpellResponse;

public class ParseMain {

	private LinkedHashMap<String, Integer> totalWordMap = new LinkedHashMap<String, Integer>();
	private LinkedHashMap<String, LinkedHashMap<String, Integer>> wordcount = new LinkedHashMap<String, LinkedHashMap<String,Integer>>();
	private ArrayList<String> sortedCount = new ArrayList<String>();
	private SpellChecker checker = new SpellChecker();
	private SpellResponse response;
	
	
	public void ReadFile() throws IOException
	{
		checker.setOverHttps(true);
		BufferedReader br= null;
		try
		{
			br= new BufferedReader(new FileReader(new File("data/info.txt")));
			String lines;
			while((lines= br.readLine()) != null)
			{
				//System.out.println(lines);
				
				String[] line= lines.trim().toLowerCase().split(",");
				String[] tags= line[1].split(";");
				//System.out.println(line[0]);
				LinkedHashMap<String, Integer> hashmap;
				
		//		if(line[0].equals("prprabhu/train_images/000010.jpg"))
		//			System.err.println(tags[0]+","+tags[1]+","+tags[2]);
				
				
				if(wordcount.containsKey(line[0]))
				{
					hashmap = wordcount.get(line[0]);
				}
				else
				{
					hashmap = new LinkedHashMap<String, Integer>();
					wordcount.put(line[0], hashmap);
				}
				
				for(int i=0; i<tags.length; i++)
				{
					if(hashmap.containsKey(tags[0]))
					{
						hashmap.put(tags[0].toLowerCase(), hashmap.get(tags[0])+1);
					}
					else
					{
						hashmap.put(tags[0].toLowerCase(), 1);
					}
					
					if(totalWordMap.containsKey(tags[0]))
						totalWordMap.put(tags[0].toLowerCase(), totalWordMap.get(tags[0])+1);
					else
						totalWordMap.put(tags[0].toLowerCase(), 1);
				}
			
			}
			
			//totalWordMap.clear();
			totalWordMap = this.sortHashMap(totalWordMap);
			System.out.println("TotalWordMapSize: "+totalWordMap.size());
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			br.close();
		}
		
	}
	
	public static LinkedHashMap<String,Integer> sortHashMap(Map<String,Integer> map)
	{
		Map<String,Integer> tempMap=new HashMap<String,Integer>(map);
		LinkedHashMap<String,Integer> sortedOutputMap = new LinkedHashMap<String, Integer>();
		
		for(int i=0;i<map.size();i++){
		    Map.Entry<String,Integer> maxEntry=null;
		    Integer maxValue=-1;
		    for(Map.Entry<String,Integer> entry:tempMap.entrySet()){
		        if(entry.getValue()>maxValue){
		            maxValue=entry.getValue();
		            maxEntry=entry;
		        }
		    }
		    tempMap.remove(maxEntry.getKey());
		    sortedOutputMap.put(maxEntry.getKey(),maxEntry.getValue());
		}
		return sortedOutputMap;
	}
	
	public void WriteFile() throws IOException
	{
		BufferedWriter common = null, results=null;
		try {
		common = new BufferedWriter(new FileWriter(new File("commonwords")));
		results = new BufferedWriter(new FileWriter(new File("results")));
		Iterator it = wordcount.entrySet().iterator();
		while(it.hasNext())
		{
		//	System.out.println("hello");
			Map.Entry<String, HashMap<String, Integer>> entry = (Entry<String, HashMap<String, Integer>>) it.next();
			HashMap<String, Integer> hashmap = entry.getValue();
			hashmap = this.sortHashMap(hashmap);
			Iterator it1 = hashmap.entrySet().iterator();
			
			common.write(entry.getKey());
			common.write("\n----------------------------------");
			common.newLine();
			while(it1.hasNext())
			{
				Map.Entry<String , Integer> entry1= (Entry<String, Integer>) it1.next();
				//System.out.println(entry1.getKey());
				String result= entry1.getKey()+"("+entry1.getValue()+")";
				
				response = checker.check(entry1.getKey());
				try
				{
					Boolean flag= false;
					if(response != null)
					{	
						String spellings=result+": { ";
						for(SpellCorrection sc: response.getCorrections())
						{
							spellings+= sc.getValue();
							flag= true;
						}
						spellings+=" }\n";
						if(flag)
						{
								result= spellings;
								common.write(result);
						}
					}
					
				
				}catch (Exception e) {
					// TODO: handle exception
					common.write(result+"\n");
					continue;
				}
				
				
			}
			
			common.newLine();
			common.write("___________________________________________\n");
			
		}
		
		it = this.totalWordMap.entrySet().iterator();
		int count=0;
		while(it.hasNext())
		{
			Map.Entry<String, Integer> entry = (Entry) it.next();
			results.write(entry.getKey()+"("+entry.getValue()+")\n");
			try
			{
			response = checker.check(entry.getKey());
			String spellings="";
			if(response != null)
				for(SpellCorrection spells: response.getCorrections())
					spellings+= spells.getValue()+" ";
			if(spellings.length() > 1)
			{
				results.write("-->  {"+spellings+" }");
				count++;
			}
			}
			catch(Exception ne)
			{
				continue;
			}
			results.newLine();
		}
		
		results.write("\nTotalWords: "+totalWordMap.size()+"\n");
		results.write("Words Misspelled: "+count);
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			common.close();
			results.close();
		}

	}
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
	//	SpellChecker checker = new SpellChecker();
	//	SpellResponse response = checker.check("transport");
	/*	
		for(SpellCorrection correction: response.getCorrections())
		{
			System.out.println(correction.getValue());
		}
	*/
		ParseMain parseMain = new ParseMain();
		parseMain.ReadFile();
		parseMain.WriteFile();
	}

}
