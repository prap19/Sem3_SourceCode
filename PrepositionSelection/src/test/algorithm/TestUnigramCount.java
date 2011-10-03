package test.algorithm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;

import create.dataset.MainClass;
import dataset.process.BaseLineUnigram;
import edu.stanford.nlp.ling.CoreAnnotations.StemAnnotation;

public class TestUnigramCount {
	
	private String FS = MainClass.FS;
	private HashMap<String, HashMap<String, Integer>> unigram;
	private HashMap<String, Integer> prepMap;
	private BaseLineUnigram baseLineUnigram;
	
	public TestUnigramCount()
	{
		baseLineUnigram = new BaseLineUnigram();
		baseLineUnigram.evaluateUnigramCount();
		this.unigram = this.baseLineUnigram.getUnigram();
		this.prepMap = this.baseLineUnigram.getPrepMap();
	}
	
	public void TestDataset()
	{
		String inputFile= "data"+FS+"window"+FS+"test_set";
		BufferedReader br = null;
		String line;
		int misplaced =0;
		int totalread= 0;
		for(int i=0; i< MainClass.prep.length; i++)
		{
			int count = 0;
			try
			{
				br = new BufferedReader(new FileReader(new File(inputFile+FS+MainClass.prep[i]+".txt")));
				while(((line = br.readLine()) != null) && (count < TestPerceptron.TOTALFILE))
				{
					count--;
					totalread++;
					String[] tokens = line.split("\\s+");
					for(int j=0; j<tokens.length; j++)
					{
						String[] wordsplit = tokens[j].split("/");
						if(this.prepMap.containsKey(wordsplit[0]))
						{
							if(j-1 >= 0)
							{
								String word = BaseLineUnigram.getStemWord(tokens[j-1].split("/")[0]);
								if(this.unigram.containsKey(word))
									System.out.println("Word: "+word+" found.");
								String argMax = this.getMax(this.unigram.get(word));
								String temp = tokens[j].split("/")[0];
								
								if(argMax.equals(temp)!= true)
								{
									misplaced++;
								}
							}
						}
					}
					
				}
				
			}
			catch(Exception e)
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
		System.out.println("Error: "+misplaced);
		System.out.println("Efficiency of Unigram Baseline: "+(double)(1-(double)misplaced/(TestPerceptron.TOTALFILE*MainClass.prep.length)));
	}
	
	public String getMax(HashMap<String, Integer> temp)
	{
		Integer max=-1;
		String argMax=new String();
		int index=0;
			
		if(temp != null)
		{
			for(Iterator it = temp.entrySet().iterator(); it.hasNext();)
			{
				Entry<String, Integer> entry = (Entry)it.next();
				if(entry.getValue() > max)
				{
					max = entry.getValue();
					argMax = entry.getKey();
				}
			}
			index--;
		}
		
		/*
		
		if((index > 1))
		{
			Random random= new Random(this.prepMap.size());
			index= random.nextInt(this.prepMap.size())%prepMap.size();
		}
		*/
		if(argMax == null)
			return MainClass.prep[index];
		
		return argMax;
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		TestUnigramCount unigramCount = new TestUnigramCount();
		unigramCount.TestDataset();
	}

}
