package test.algorithm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import create.dataset.MainClass;

import dataset.process.BigramPerceptronAlgorithm;

public class TestBigramPerceptron {

	private String FS = MainClass.FS;
	private BigramPerceptronAlgorithm algorithm;
	private Double[][] weights;
	private HashMap<String, Integer> prepMap;
	private Integer misplaced;
	public static Integer TOTALFILE = 14000;
	
	public TestBigramPerceptron()
	{
		algorithm = new BigramPerceptronAlgorithm();
		algorithm.trainWeights();
		this.weights = algorithm.getWeights();
		this.prepMap = algorithm.getPrepMap();
		this.misplaced = 0;
	}
	
	public void TestDataSet()
	{
		String inputFile= "data"+FS+"window"+FS+"non-tagged"+FS+"test_set";
		BufferedReader br = null;
		BufferedWriter bw = null;
		int argMax;
		int n=0;
		try
		{
			this.misplaced =0;
			for(int i=0; i< MainClass.prep.length; i++)
			{
				int count = this.TOTALFILE;
				
				br = new BufferedReader(new FileReader(new File(inputFile+FS+MainClass.prep[i]+".txt")));
				bw = new BufferedWriter(new FileWriter(new File(inputFile+FS+"error"+FS+"error"+"_"+MainClass.prep[i]+".txt")));
				String line;
				Double[] vector = null;
				while(((line = br.readLine()) != null) && (count > 0))
				{
					count--;
					if(line.isEmpty() !=true)
					{
						n++;
						String[] tokens = this.algorithm.getNewToken(line.split("\\s+"));
						if(this.algorithm.isEmptyToken(tokens) != true)
						{	vector = this.algorithm.getFeatureVector(tokens);
						}
						else
							continue;
					
					}
					
					argMax = this.dotProduct(vector);
					if(argMax != i)
					{
						System.out.println("Error found for "+MainClass.prep[i] );
						bw.write(line+"  ------- PREDICTED: "+MainClass.prep[argMax]);
						bw.newLine();
						this.misplaced++;
					}
				}
				
			}
			System.out.println("Efficiency: "+(double)(1 - (double)(this.misplaced)/n));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try {
				br.close();
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
	
	public int dotProduct(Double[] vector)
	{
		int argMax=0;
		Double max = (double)0;
		Double [] dotprod = new Double[vector.length];  
		for(int i=0 ; i<vector.length; i++)
			dotprod[i] = (double)0;
		
		for(int k=0; k<MainClass.prep.length; k++)
		{
			for(int j=0; j< vector.length; j++)
			{
				dotprod[k] += this.weights[k][j] * vector[j];
			}
			if(dotprod[k] > max)
			{
				max = dotprod[k];
				argMax = k;
			}
		}
		
		return argMax;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		TestBigramPerceptron perceptron = new TestBigramPerceptron();
		perceptron.TestDataSet();
		System.out.println("FINAL WEIGHTS");
	}

}
