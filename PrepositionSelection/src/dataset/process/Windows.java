package dataset.process;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import create.dataset.MainClass;

public class Windows {

	private String FS= MainClass.FS;
	private String[] prep = MainClass.prep;
	private int WINDOW_SIZE=5;
	
	public Windows()
	{
		
		//Set window size to odd if it is even
		if(this.WINDOW_SIZE%2==0)
			this.WINDOW_SIZE++;
	}
	
	public void readTaggedAndCreateWindow(String folderName)
	{
		BufferedReader br= null;
		BufferedWriter bw = null;
		String inputPath= "data"+FS+"tagged"+FS+folderName;
		String outputPath = "data"+FS+"window"+FS+folderName;
		try
		{
			if((new File(outputPath).exists())!= true)
			{
				(new File(outputPath)).mkdir();
			}
			
			for(int i=0; i<prep.length; i++)
			{
				String line= null;
				String inputfile = inputPath+FS+prep[i]+".txt";
				String outputfile = outputPath+FS+prep[i]+".txt";
				br= new BufferedReader(new FileReader(new File(inputfile)));
				bw = new BufferedWriter(new FileWriter(new File(outputfile)));
				while((line = br.readLine()) != null)
				{
					// '/IN' is part of speech identifier by the tagger
					ArrayList<ArrayList<String>> windows = this.createWindows(prep[i]+"/IN", line); 
					if(windows != null)
					{
						for(ArrayList<String> window: windows )
						{
							String windowString="";
							for(int k=0; k<window.size(); k++)
								windowString += window.get(k)+" ";
							bw.write(windowString);
							bw.newLine();
							System.out.println(windowString + "SIZE= "+window.size());
						}
					//	bw.newLine();
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
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
			
					
	}
	
	public ArrayList<ArrayList<String>> createWindows(String prep, String line)
	{
		int rindex=0;
		ArrayList<ArrayList<String>> resultArr = new ArrayList<ArrayList<String>>();
		
		//System.out.println("CREATE WINDOW: "+line);
		if(line.length()>0)
		{
			String[] tokens = line.split("\\s+");
			for(int i=0; i < tokens.length; i++)
			{
				ArrayList<String> result = new ArrayList<String>(tokens.length);
				rindex=0;
				if(tokens[i].equalsIgnoreCase(prep))
				{
					for(int j= i-this.WINDOW_SIZE/2; j <= i+this.WINDOW_SIZE/2; j++)
					{
						if((j>=0) && (j<tokens.length))
						{
							result.add(rindex++, tokens[j]);
						}
					}
				}
				if(result.isEmpty() != true)
					{
						resultArr.add(result);
						//System.err.println("WINDOW: "+ result.toString()+" SIZE: "+result.size());
						if(result.size()> this.WINDOW_SIZE)
							System.err.println("[ERROR] WINDOW: "+ result.toString()+" SIZE: "+result.size());
					}
			}
			
		}
		
		return resultArr;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
			Windows windows = new Windows();
			windows.readTaggedAndCreateWindow("training_set");
			windows.readTaggedAndCreateWindow("test_set");
			windows.readTaggedAndCreateWindow("development_set");
	}

}
