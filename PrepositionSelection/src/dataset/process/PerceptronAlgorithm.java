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
	private Double[][] weights;
	private final Double scale_factor = 0.35;
	private Double[] sum;
	
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
			weights = new Double[MainClass.prep.length][prepMap.size()];
			
			sum= new Double[this.prepMap.size()];
			
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
				this.weights[i][j]=((new Random()).nextDouble());
			}
		}
	}
	
	public void resetSum()
	{
		for(int i=0; i< sum.length; i++)
		{
			sum[i] = (double) 0;
		}
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
		
			for(int k=0; k<MainClass.prep.length; k++)
			{
				System.out.println("FOR: "+MainClass.prep[k]);
				this.randomizeWeights();
				int miscount=Integer.MAX_VALUE;
				
				while(miscount > MainClass.NOF*2*0.4)
				{
					try
					{
						System.err.println("CONDITION: "+miscount);
						count=0;
						polarity =1;
						poscount =0;
						negcount =0;	
						miscount =0;
						this.resetSum();
						
						posfile = new BufferedReader(new FileReader(new File("data"+FS+"window"+FS+"training_set"+FS+MainClass.prep[k]+".txt")));
						negfile = new BufferedReader(new FileReader(new File("data"+FS+"negative"+FS+"window"+FS+MainClass.prep[k]+".txt")));
						String line="";
						while(count <= MainClass.NOF*2)
						{
							//rand = (int) (System.currentTimeMillis()%2);
							rand = random.nextInt(2);
							if((rand == 0) && (poscount< MainClass.NOF))
							{
								line = posfile.readLine();
								polarity =1;
								poscount++;
								count++;
								//System.err.println("POSITIVE");
							}
							else
								if(negcount< MainClass.NOF)
								{
									line = negfile.readLine();
									polarity = -1;
									negcount++;
									count++;
									//System.err.println("NEGATIVE");
								}
								else
									break;					


							Integer[] vector = new Integer[this.prepMap.size()];
							for(int i=0; i< this.prepMap.size(); i++)
								vector[i]=0;

							String[] tokens= line.split("\\s+");
							for(String token: tokens)
							{
								// [0]: word [1]: part of speech  
								String[] wordsplit = token.split("/");
								vector[this.prepMap.get(wordsplit[1])] = 1;
							}
							miscount= adjustSum(k,vector, polarity, miscount);
							//System.out.println("MISCOUNT: "+miscount);
						}
						for(int i=0; i< sum.length; i++)
							weights[k][i] += sum[i]*this.scale_factor;
				}
				catch(Exception e)
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
	}
					this.displayWeight();
					System.out.println("Number of files read #POS: "+poscount+" #NEG: "+negcount);
					
				
}
	
	
	///returns the number of misplaced sentences
	private int adjustSum(int k, Integer[] vector, int polarity, int miscount) {
		// TODO Auto-generated method stub
		Double res = 0.0;
		int div = 0;
		
		//displayVector(vector);
		//displayWeight();
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
			for(int i=0; i<this.prepMap.size(); i++)
				sum[i]+= div*vector[i];
		
		return miscount;
	}


	public void displayVector(Integer[] vector)
	{
		System.out.print("[");
		for(int i=0; i<vector.length; i++)
		{
			System.out.print(" "+vector[i]);
		}
		System.out.println("]");
	}

	public void displayWeight()
	{
		for(int i=0; i< MainClass.prep.length; i++)
		{
			for(int j=0; j< this.prepMap.size(); j++)
				System.out.print(weights[i][j]+"\t");
			System.out.println();
		}
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PerceptronAlgorithm algorithm = new PerceptronAlgorithm();
		//algorithm.randomizeWeights();
		//algorithm.displayWeight();
		System.out.println("##################################################");
		algorithm.trainWeights();
	}

}

