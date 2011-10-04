package dataset.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import create.dataset.MainClass;

public class BigramPerceptronAlgorithm {

	private String FS = MainClass.FS;
	private HashMap<String, Integer> unigram;
	private	HashMap<String, Integer> bigram;
	private HashMap<String, Integer> prepMap;
	
	private Double[][] weights;
	private Integer VECTOR_LENGTH = 21;
	private static Integer index=0;
	private Double[] sum;
	private static Double scale_factor = PerceptronAlgorithm.scale_factor;
	
	public BigramPerceptronAlgorithm()
	{
		BigramCount bigramCount = new BigramCount();
		bigramCount.populateUnigram();
		bigramCount.populateBigram();
		this.unigram = bigramCount.getUnigram();
		this.bigram = bigramCount.getBigram();
		this.weights = new Double[MainClass.prep.length][this.VECTOR_LENGTH];
		this.prepMap= new HashMap<String, Integer>();
		for(int i=0; i<MainClass.prep.length; i++)
		{
			this.prepMap.put(MainClass.prep[i], 0);
		}
		
		sum= new Double[this.VECTOR_LENGTH];
		Random random = new Random();
		for(int i=0; i<MainClass.prep.length; i++)
		{
			for(int j=0; j<this.VECTOR_LENGTH; j++)
			{
				this.weights[i][j] = random.nextDouble();
			//	this.weights[i][j] = 0.0;
			}
		}
		
	
	}
	
	
	public String[] getNewToken(String[] tokens)
	{
		String[] newTokens = new String[this.VECTOR_LENGTH/MainClass.prep.length];
	
		int prepIndex=0;
		int winLen = (this.VECTOR_LENGTH/MainClass.prep.length) /2;
		for(String token: tokens)
		{
			if(this.prepMap.containsKey(token))
			{
				break;
			}
			prepIndex++;
		}
		//1st half
		int j=prepIndex-winLen;
		//System.out.println("TOKEN SIZE"+tokens.length);
		for(int i=0; i<this.VECTOR_LENGTH/MainClass.prep.length; i++)
		{
			if((j<0) || (j>=tokens.length))
				newTokens[i]= "-";
			else
				newTokens[i]=BaseLineUnigram.getStemWord(tokens[j++]);
		}
		
	//	System.out.println("NEWTOKENS SIZE: "+newTokens.length);
		
		
		return newTokens;
	}
	
	public Double getProbability(String[] tokens, int i, int j)
	{
		Double res = (double) 1;
		for(int index=i; i+1<=j; i++)
		{
			String word=tokens[i]+tokens[i+1];
			if((this.bigram.containsKey(word)) && (this.unigram.containsKey(tokens[i+1])))
			{
					res*= (double)((double)this.bigram.get(word)/this.unigram.get(tokens[i+1]));
			}
			else
			{
				res = 0.0;
				break;
			}
		}
		return res;
	}
	
	
	public Double[] getFeatureVector(String[] tokens)
	{
		Double[] vector = new Double[this.VECTOR_LENGTH];
		Integer tokenIndex;
		Integer winLength = MainClass.prep.length;
		
		for(int i=0; i<MainClass.prep.length; i++)
		{
			tokens[(this.VECTOR_LENGTH/MainClass.prep.length)/2] = MainClass.prep[i];
			vector[7*i+0] = getProbability(tokens, 0, 3);
			vector[7*i+1] = getProbability(tokens, 1, 3);
			vector[7*i+2] = getProbability(tokens, 2, 3);
			vector[7*i+3] = 1.0;
			vector[7*i+4] = getProbability(tokens, 3, 4);
			vector[7*i+5] = getProbability(tokens, 3, 5);
			vector[7*i+6] = getProbability(tokens, 3, 6);
		}
		return vector;
	}
	public void trainWeights()
	{
		BufferedReader posfile= null;
		BufferedReader negfile= null;
		BufferedReader br= null;
		int rand, count=0;
		Random random = new Random(3);
		int polarity =1;   // 1 = positive file and -1 = negative file
		int poscount =0, negcount =0; 
	//	this.randomizeWeights();
		
		for(int k=0; k<MainClass.prep.length; k++)
		{
			System.out.println("FOR: "+MainClass.prep[k]);
			System.out.println("############################################################################");
			//this.randomizeWeights();
			
			int iteration =1;
			int miscount=Integer.MAX_VALUE;
			System.out.println("Initial weight");
			this.displayWeight();
			while(miscount > MainClass.NOF*2*0.35)
			{
				try
				{
					System.err.println("Iteration:"+iteration+"  Error: "+miscount);
					iteration++;
					count=0;
					polarity =1;
					poscount =0;
					negcount =0;	
					miscount =0;
					this.resetSum();
					
					posfile = new BufferedReader(new FileReader(new File("data"+FS+"window"+FS+"non-tagged"+FS+"training_set"+FS+MainClass.prep[k]+".txt")));
					negfile = new BufferedReader(new FileReader(new File("data"+FS+"negative"+FS+"window"+FS+MainClass.prep[k]+".txt")));
					
					String line="";
					
					while(count <= MainClass.NOF*2)
					{
						//rand = (int) (System.currentTimeMillis()%2);
						rand = random.nextInt(2);
						
						if(((rand == 0) && (poscount< MainClass.NOF)) || (negcount >= MainClass.NOF))
						{
							line = posfile.readLine();
							polarity =1;
							poscount++;
							count++;
							//System.err.println("POSITIVE");
						}
						else
							if((negcount< MainClass.NOF) || (poscount >= MainClass.NOF))
							{
								line = negfile.readLine();
								polarity = -1;
								negcount++;
								count++;
								//System.err.println("NEGATIVE");
							}
							else
								break;					
						
						if(line == null)
							break;

						if(line.isEmpty() !=true)
						{
							String[] tokens = this.getNewToken(line.split("\\s+"));
							if(this.isEmptyToken(tokens) != true)
							{	Double[] vector = this.getFeatureVector(tokens);
								miscount = this.adjustSum(k, vector, polarity, miscount);
							}
							else
								continue;
						
						}
						//System.out.println("MISCOUNT: "+miscount);
					}
					for(int i=0; i< sum.length; i++)
						weights[k][i] += sum[i]*this.scale_factor;
					
				}catch(Exception e)
				{
					e.printStackTrace();
				}
				finally
				{
					try {
						posfile.close();
						negfile.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			for(int i=0; i< sum.length; i++)
				weights[k][i] += sum[i]*this.scale_factor;
		}
		
		/*
		
		try
		{
			String path= "data"+FS+"window"+FS+"non-tagged"+FS+"training_set";
			for(int k=0; k<MainClass.prep.length; k++)
			{
				String file= path+FS+MainClass.prep[k]+".txt";
				br= new BufferedReader(new FileReader(new File(file)));
				String line;
				while((line = br.readLine()) != null)
				{
					if(line.isEmpty() !=true)
					{
						String[] tokens = this.getNewToken(line.split("\\s+"));
						if(this.isEmptyToken(tokens) != true)
						{	Double[] vector = this.getFeatureVector(tokens);
							//this.displayVector(vector);
							this.adjustSum(k, vector, polarity, miscount);
							//System.err.println("TOKENS: "+Arrays.deepToString(tokens));
						}
						else
							continue;
					
					}
				}
				for(int i=0; i< sum.length; i++)
					weights[k][i] += sum[i]*this.scale_factor;
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
		*/
		
		this.displayWeight();
		System.out.println("Number of windows read #POS: "+poscount+" #NEG: "+negcount);
	}
	
	public String getFS() {
		return FS;
	}


	public void setFS(String fS) {
		FS = fS;
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


	public HashMap<String, Integer> getPrepMap() {
		return prepMap;
	}


	public void setPrepMap(HashMap<String, Integer> prepMap) {
		this.prepMap = prepMap;
	}


	public Double[][] getWeights() {
		return weights;
	}


	public void setWeights(Double[][] weights) {
		this.weights = weights;
	}


	public Integer getVECTOR_LENGTH() {
		return VECTOR_LENGTH;
	}


	public void setVECTOR_LENGTH(Integer vECTOR_LENGTH) {
		VECTOR_LENGTH = vECTOR_LENGTH;
	}


	public boolean isEmptyToken(String[] tokens) {
		// TODO Auto-generated method stub
		int count=0;
		for(int i=0; i<tokens.length; i++)
		{
			if(tokens[i].equals("-"))
				count++;
		}
		if(count == tokens.length)
			return true;
		else
			return false;
	}

	private int adjustSum(int k, Double[] vector, int polarity, int miscount) {
		// TODO Auto-generated method stub
		Double res = 0.0;
		int div = 0;
	
		for(int i=0; i<vector.length; i++)
		{
			res += this.weights[k][i]*vector[i];
		//	System.out.println("MUL: "+this.weights[k][i]*vector[i]);
		}
		
		if(polarity>0) //positive file
			if(res < 0)
			{
				div =1;
				miscount++;
			}
		if(polarity<0)
			if(res>0)
			{
				div= -1;
				miscount++;
			}
		
		if(div!=0)
			for(int i=0; i< vector.length; i++)
				sum[i]+= div*vector[i];
		
		return miscount;
	}

	public void resetSum()
	{
		for(int i=0; i< sum.length; i++)
		{
			sum[i] = (double) 0;
		}
	}
	
	public void displayVector(Object[] vector)
	{
		System.out.print("[");
		for(int i=0; i<vector.length; i++)
		{
			System.out.print(vector[i]+" ");
		}
		System.out.println("]");
	}
	
	public void displayWeight()
	{
		for(int i=0; i< MainClass.prep.length; i++)
		{
			for(int j=0; j< this.VECTOR_LENGTH; j++)
				System.out.print(weights[i][j]+"\t");
			System.out.println();
		}
		System.out.println("--------------------------------------------");
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		BigramPerceptronAlgorithm algorithm = new BigramPerceptronAlgorithm();
	//	String str= "of elected officials in";
				//	 0   1      2    3   4    5       6        7  8    9     10			
	//	algorithm.getNewToken(str.split("\\s+"));
		algorithm.trainWeights();
	}

}
