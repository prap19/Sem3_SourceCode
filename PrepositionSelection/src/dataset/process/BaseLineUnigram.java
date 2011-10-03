package dataset.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import create.dataset.MainClass;

public class BaseLineUnigram {

	private String FS = MainClass.FS;
	private HashMap<String, HashMap<String, Integer>> unigram;
	private HashMap<String, Integer> prepMap;
	private static Stemmer stemmer;
	
	
	public HashMap<String, HashMap<String, Integer>> getUnigram() {
		return unigram;
	}

	public HashMap<String, Integer> getPrepMap() {
		return prepMap;
	}
	
	public BaseLineUnigram()
	{
		unigram = new HashMap<String, HashMap<String,Integer>>();
		prepMap = new HashMap<String, Integer>();
		stemmer = new Stemmer();
	
		for(int k=0; k< MainClass.prep.length; k++)
		{
			prepMap.put(MainClass.prep[k], k);
		}
		/*
		System.out.println("PREPMAP");
		for(Iterator  it = prepMap.entrySet().iterator(); it.hasNext();)
		   {
			   Entry<String, Integer> prepEntry = (Entry<String, Integer>) it.next();
			   System.out.print(prepEntry.getKey()+": "+prepEntry.getValue()+"  ");
		   }
		   */
	}
	
	public static String getStemWord(String word)
	{
		for(int i=0; i<word.length(); i++)
		{
			stemmer.add(word.charAt(i));
		}
		stemmer.stem();
	
		return stemmer.toString();
	}
	
	public void evaluateUnigramCount()
	{
		String inputPath="data"+FS+"seperated"+FS+"training_set";
		BufferedReader br = null;
		String line;
		int linecount=0;
		try
		{
			for(int k=0; k< MainClass.prep.length; k++)
			{
				br= new BufferedReader(new FileReader(new File(inputPath+FS+MainClass.prep[k]+".txt")));
				while((line=br.readLine())!=null)
				{
					linecount++;
					line = line.replaceAll("\\p{Punct}|\\d","");
					String[] tokens = line.split("\\s+");
					for(int i=0; i<tokens.length; i++)
					{
						if(this.prepMap.containsKey(tokens[i]))
						{
							
							if(i-1 >= 0)
							{
								if(this.unigram.containsKey(getStemWord(tokens[i-1])))
								{
									HashMap<String, Integer> temp= this.unigram.get(getStemWord(tokens[i-1]));
									temp.put(tokens[i], (Integer)temp.get(tokens[i])+1);
									
								}	
								else
								{
									HashMap<String, Integer> countMap = new HashMap<String, Integer>();
									for(int j=0; j< MainClass.prep.length; j++)
									{
										countMap.put(MainClass.prep[j], 0);
										//System.out.println("CountMap: "+MainClass.prep[k]+": "+countMap.containsKey(MainClass.prep[k]));
									}
									//System.out.println("COUNTMAP SIZE"+countMap.size());
									this.unigram.put(getStemWord(tokens[i-1]), countMap);
									System.out.println("Inserted: "+getStemWord(tokens[i-1]) );
								}
							}
						}
					}
				}
				
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	public void printUnigramMap()
	{
		for( Iterator entries = this.unigram.entrySet().iterator(); entries.hasNext();){

			   Entry entry = (Entry) entries.next();
			   HashMap<String, Integer> temp = (HashMap<String, Integer>)entry.getValue();
			  // System.out.println("TEMP SIZE: "+temp.size());
			  // System.err.println("Word: " + entry.getKey() +"\t :");
			   for(Iterator  it = temp.entrySet().iterator(); it.hasNext();)
			   {
				   Entry<String, Integer> prepEntry = (Entry<String, Integer>) it.next();
				 //  System.out.print(prepEntry.getKey()+": "+prepEntry.getValue()+"  ");
			   }
			   System.out.println();
			}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		BaseLineUnigram unigram = new BaseLineUnigram();
		unigram.evaluateUnigramCount();
		unigram.printUnigramMap();
		
	}

}
