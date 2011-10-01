package create.dataset;

import java.awt.Window;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import dataset.process.Windows;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class NegativeSample {

	private String FS = MainClass.FS;
	private String[] prep = MainClass.prep;
	private HashMap<String, Integer> prepMap;
	
	public NegativeSample()
	{
		prepMap = new HashMap<String, Integer>();
		for(int i=0; i<prep.length; i++)
		{
			prepMap.put(prep[i], 0);
		}
	}
	
	public String randomizeSentence(String line)
	{
		String result="";
		String[] res = line.split("\\s+");
		for(int k=0; k < res.length; k++)
		{
			//System.out.println(res[k]);
			if(this.prepMap.containsKey(res[k]))
			{
				Random random = new Random();
				int x;
				do
				{
					x= random.nextInt(prep.length);
				}while((prep[x]).equalsIgnoreCase(res[k]));
				
			//	System.err.println("X :="+x);
				res[k]= prep[x];
			}
			result+= res[k]+" ";
		}
		
		return result.trim();
	}
	
	public void getNextPreposition()
	{
		
	}
	
	public void createNegativeDataset() throws IOException, ClassNotFoundException
	{
		BufferedReader br= null;
		BufferedWriter tag = null;
		BufferedWriter win = null;
		String inputpath  = "data"+FS+"seperated"+FS+"training_set" ;
		String taggedOutput = "data"+FS+"negative"+FS+"tagged";
		String windowOutput = "data"+FS+"negative"+FS+"window";
		MaxentTagger tagger = new MaxentTagger("tagger"+MainClass.FS+"left3words-wsj-0-18.tagger");
		
		for(int i=0; i<this.prep.length; i++)
		{
			try
			{
				br = new BufferedReader(new FileReader(new File(inputpath+FS+prep[i]+".txt")));
				tag = new BufferedWriter(new FileWriter(new File(taggedOutput+FS+prep[i]+".txt")));
				win = new BufferedWriter(new FileWriter(new File(windowOutput+FS+prep[i]+".txt")));
				String line;
				while((line=br.readLine())!=null)
				{
					System.out.println("[ORG] "+line);
					line = this.randomizeSentence(line.toLowerCase().replaceAll("\\p{Punct}|\\d", ""));
					System.out.println("[RANDOM] "+line);
					line = tagger.tagString(line);
					tag.write(line);
					tag.newLine();
					
					ArrayList<ArrayList<String>> arr = Windows.createWindows(prep[i]+"/IN", line);
					//System.out.println("#WINDOWS: "+arr.size());
					for(ArrayList<String> windows: arr)
					{
						String str="";
						for(int j=0; j<windows.size(); j++)
						{
							str += windows.get(j)+" "; 
						}
						System.out.println(str);
						win.write(str.trim());
						win.newLine();
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
					tag.close();
					win.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		NegativeSample sample = new NegativeSample();
		try {
			sample.createNegativeDataset();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
