package dataset.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import test.algorithm.TestPerceptron;

import create.dataset.MainClass;

public class BigramCount {

	private String FS = MainClass.FS;
	private HashMap<String, Integer> unigram;
	private	HashMap<String, Integer> bigram;
	public static Stemmer stemmer;
	
	public BigramCount() {
		// TODO Auto-generated constructor stub
		this.unigram = new HashMap<String, Integer>();
		this.bigram = new HashMap<String, Integer>();
	}
	
	public void populateUnigram()
	{
		BufferedReader br= null;
		try
		{
			String path="data"+FS+"seperated"+FS+"training_set";
			for(int k=0; k<MainClass.prep.length; k++)
			{
				String file= path+FS+MainClass.prep[k]+".txt";
				br = new BufferedReader(new FileReader(new File(file)));
				String line;
				while((line = br.readLine()) != null) 
				{
					String[] tokens = line.split("\\s+");
					for(String token: tokens)
					{
						if(this.unigram.containsKey(BaseLineUnigram.getStemWord(token)))
						{
							this.unigram.put(BaseLineUnigram.getStemWord(token), this.unigram.get(BaseLineUnigram.getStemWord(token))+1);
						}
						else
							this.unigram.put(BaseLineUnigram.getStemWord(token), 1);
					}
				}
			}
		//	this.display(unigram);
			
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
	
	public void populateBigram()
	{
		BufferedReader br= null;
		try
		{
			String path="data"+FS+"seperated"+FS+"training_set";
			for(int k=0; k<MainClass.prep.length; k++)
			{
				String file= path+FS+MainClass.prep[k]+".txt";
				br = new BufferedReader(new FileReader(new File(file)));
				String line;
				while((line = br.readLine()) != null) 
				{
					String[] tokens = line.split("\\s+");
					for(int i=0; (i+1)<tokens.length; i++)
					{
							String word = tokens[i]+tokens[i+1];
							if(this.bigram.containsKey(BaseLineUnigram.getStemWord(word)))
							{
								this.bigram.put(BaseLineUnigram.getStemWord(word), this.bigram.get(BaseLineUnigram.getStemWord(word))+1);
							}
							else
								this.bigram.put(BaseLineUnigram.getStemWord(word), 1);
									
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
	//	System.out.println("SIZE"+bigram.size());
	//	this.display(this.bigram);
	}

	
	public HashMap<String, Integer> getUnigram() {
		return unigram;
	}

	public void setUnigram(HashMap<String, Integer> unigram) {
		this.unigram = unigram;
	}

	public HashMap<String, Integer> getBigram() {
		return bigram;
	}

	public void setBigram(HashMap<String, Integer> bigram) {
		this.bigram = bigram;
	}

	public void display(HashMap<String, Integer> map)
	{
		System.out.println("COUNT TABLE");
		for(Iterator entries = map.entrySet().iterator(); entries.hasNext();)
		{
			Entry  entry = (Entry) entries.next();
			System.out.println(entry.getKey()+": "+" "+entry.getValue());
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		BigramCount bigramCount = new BigramCount();
		bigramCount.populateUnigram();
		
		bigramCount.populateBigram();
		
	}

}
