package dataset.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import create.dataset.MainClass;

public class PerceptronAlgorithm {

	private HashMap<String, Integer> prepMap;
	private String FS = MainClass.FS;
	private Integer[][] weights;
	
	public PerceptronAlgorithm() {
		// TODO Auto-generated constructor stub
		this.prepMap = new HashMap<String, Integer>();
		int count=0;
		BufferedReader br=null;
		
		try
		{
			String line;
			br = new BufferedReader(new FileReader(new File("partsofspeech.txt")));
			while((line = br.readLine()) != null)
			{
				String[] wordsplit = line.split("\\s+");
				this.prepMap.put(wordsplit[0], count++);
			}
			weights = new Integer[MainClass.prep.length][prepMap.size()];
			
			/*Integer[] vector = new Integer[prepMap.size()];
			for( Iterator entries = prepMap.entrySet().iterator(); entries.hasNext();){

				   Entry entry = (Entry) entries.next();
				   vector[(Integer)entry.getValue()]=1;
				   
				//  System.out.println(entry.getKey() + "/" + entry.getValue());
			}
			display(vector);
*/			System.out.println("HASHMAP SIZE: "+this.prepMap.size());
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
	
	public void randomizeWeights()
	{
		for(int i=0; i<MainClass.prep.length; i++)
		{
			for(int j=0; j<this.prepMap.size(); j++)
			{
				this.weights[i][j]=(new Random()).nextInt(10);
			}
		}
	}
	
	public void trainWeights()
	{
		BufferedReader br= null;
		try
		{
			for(int k=0; k<this.prepMap.size(); k++)
			{
				br= new BufferedReader(new FileReader(new File("data"+FS+"window"+FS+"training_set"+FS+MainClass.prep[k]+".txt")));
				String line="";
				while((line=br.readLine()) != null)
				{
					Integer[] vector = new Integer[this.prepMap.size()];
					for(int i=0; i< this.prepMap.size(); i++)
						vector[i]=0;
					String[] tokens= line.split("\\s+");
					//System.out.println(line);
					for(String token: tokens)
					{
						// [0]: word [1]: part of speech  
						String[] wordsplit = token.split("/");
						vector[this.prepMap.get(wordsplit[1])] = 1;
					}
					adjustWeights(k,vector);
					//display(vector);
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
	
	private void adjustWeights(int k, Integer[] vector) {
		// TODO Auto-generated method stub
		
		
		
	}

	public void display(Integer[] vector)
	{
		System.out.print("[");
		for(int i=0; i<vector.length; i++)
		{
			System.out.print(" "+vector[i]);
		}
		System.out.println("]");
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PerceptronAlgorithm algorithm = new PerceptronAlgorithm();
		algorithm.randomizeWeights();
		algorithm.trainWeights();
	}

}
