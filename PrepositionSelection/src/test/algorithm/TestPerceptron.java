package test.algorithm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import create.dataset.MainClass;

import dataset.process.PerceptronAlgorithm;

public class TestPerceptron {

	private String FS = MainClass.FS;
	private PerceptronAlgorithm algorithm;
	private Double[][] weights;
	private HashMap<String, Integer> prepMap;
	private Integer misplaced;
	public static Integer TOTALFILE = 6700;
	public Integer[] error; 
	public Integer[] prepTotal;
	
	public TestPerceptron()
	{
		algorithm = new PerceptronAlgorithm();
		algorithm.trainWeights();
		this.weights = algorithm.getWeights();
		this.prepMap = algorithm.getPrepMap();
		this.misplaced = 0;
		error= new Integer[MainClass.prep.length];
		prepTotal = new Integer[MainClass.prep.length];
		
	}
	
	public void TestDataSet()
	{
		String inputFile= "data"+FS+"window"+FS+"test_set";
		BufferedReader br = null;
		BufferedWriter bw = null;
		int argMax;
		int n=0, totalMisplaced=0;
		
		try
		{
			this.misplaced =0;
			for(int i=0; i< MainClass.prep.length; i++)
			{
				error[i]=0;
				prepTotal[i]=0;
				int count = this.TOTALFILE;
				
				br = new BufferedReader(new FileReader(new File(inputFile+FS+MainClass.prep[i]+".txt")));
				bw = new BufferedWriter(new FileWriter(new File(inputFile+FS+"error"+FS+"error"+"_"+MainClass.prep[i]+".txt")));
				String line;
				while(((line = br.readLine()) != null) && (count > 0))
				{
					count--;
					n++;
					prepTotal[i]++;
					Integer[] vector = this.algorithm.getFeatureVector(line);
					argMax = this.dotProduct(vector);
					if(argMax != i)
					{
						//System.out.println("Error found for "+MainClass.prep[i] );
						bw.write(line+"  ------- PREDICTED: "+MainClass.prep[argMax]);
						bw.newLine();
						this.misplaced++;
						totalMisplaced++;
						error[i]++;
					}
				}
				
			
			}
			for(int i=0; i<MainClass.prep.length; i++)
				System.out.println("EFFICIENCY FOR: "+MainClass.prep[i]+"("+error[i]+"):"+(double)(1 - (double)error[i]/prepTotal[i] ));
			System.out.println("OVERALL Efficiency: "+(double)(1 - (double)(totalMisplaced)/(n*MainClass.prep.length)));
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(0);
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
	
	public int dotProduct(Integer[] vector)
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

		TestPerceptron perceptron = new TestPerceptron();
		perceptron.TestDataSet();
		System.out.println("FINAL WEIGHTS");
	}

}
