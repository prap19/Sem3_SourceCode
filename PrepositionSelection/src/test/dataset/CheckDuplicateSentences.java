package test.dataset;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import create.dataset.MainClass;

public class CheckDuplicateSentences {

	private static String FS = MainClass.FS;
	public static String[] prep = MainClass.prep;
	private static ArrayList<String>[] arr = new ArrayList[prep.length];
	public HashMap<FileMap, Integer> inMap = new HashMap<FileMap, Integer>();
	public HashMap<FileMap, Integer> onMap = new HashMap<FileMap, Integer>();
	public HashMap<FileMap, Integer> ofMap = new HashMap<FileMap, Integer>();
	
	public HashMap<String, Integer> sentences = new HashMap<String, Integer>();
	
	void ReadAndWriteDataSet(String folderName) throws IOException
	{
		
		 String path = "data"+FS+"data_stage_one"+FS+folderName;  // Folder path 
		 
		  String filename, line;
		  File folder = new File(path);
		  File[] listOfFiles = folder.listFiles();
		  BufferedReader br = null;
		  for(int i=0; i < listOfFiles.length; i++)
		  { 
			 // System.out.println(listOfFiles[i].getName());
			  filename = path+FS+listOfFiles[i].getName();
			  try
			  {
				  br= new BufferedReader(new FileReader(new File(filename)));
				  while((line = br.readLine())!= null)
				  {
					for(int j=0; j<3; j++)
					{
						StringTokenizer st = new StringTokenizer(line);
						while(st.hasMoreTokens())
							if(st.nextToken().equalsIgnoreCase(prep[j]))
							{	
								switch(j)
								{
								case 0:
									this.addToHashMap(inMap, filename, line);
									break;
								case 1:
									this.addToHashMap(onMap, filename, line);
									break;

								case 2:
									this.addToHashMap(ofMap, filename, line);
									break;

								}
								break;
							}
					}
				  }
				  
			  }
			  catch(Exception e)
			  {
				  System.err.println("exception: "+e.getMessage() );
				  e.printStackTrace();
				  System.exit(0);
			  }
			  finally
			  {
				  br.close();
			  }
		  }
	}
	
	private void addToHashMap(HashMap<FileMap, Integer> map, String fileName, String sentence)
	{
		FileMap fileMap = new FileMap(fileName, sentence.toLowerCase());
		if(map.containsKey(fileMap))
		{
			map.put(fileMap, map.get(fileMap)+1);
		}
		else
		{
			map.put(fileMap, 1);
		}
	}
	
	public void printNumberOfDuplicates(String prep, HashMap map)
	{
		int count=0;
		  System.out.println("Map size: "+map.size());
		for( Iterator entries = map.entrySet().iterator(); entries.hasNext();){

			   Entry entry = (Entry) entries.next();

			  System.out.println(entry.getKey() + "/" + entry.getValue());
			 
			   Integer val = (Integer)entry.getValue();
			   if(val == null)
				   System.out.println("VAL IS NULL");
			  if(val > 1)
			  {
				  count++;
			  }

			}
		System.out.println("Repeated Entries for '"+prep+"' : "+count);
		System.out.println("Total File: "+map.size());
	}
	
	
	public void checkUniqueSentences()
	{
		String path="data"+this.FS+"seperated"+this.FS+"training_set";
		
		for(int i=0; i<prep.length; i++)
		{
			if(sentences.isEmpty()!=true)
				sentences.clear();
			String filename= path+this.FS+prep[i]+".txt";
			BufferedReader br = null;
			int count=0;
			try
			{
				String line = null;
				br = new BufferedReader(new FileReader(new File(path+this.FS+prep[i]+".txt")));
				while((line= br.readLine())!=null)
				{
					if(this.sentences.containsKey(line.toLowerCase()))
					{
						this.sentences.put(line.toLowerCase(), this.sentences.get(line.toLowerCase()+1));
						System.out.println(line);
						count++;
					}
					else
						this.sentences.put(line.toLowerCase(), new Integer(1));
				}
				printNumberOfDuplicates(prep[i], sentences);
				System.out.println("MAP SIZE: "+sentences.size());
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
	}
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		CheckDuplicateSentences CDS = new CheckDuplicateSentences();
		CDS.ReadAndWriteDataSet("test_set");
	//	CDS.printNumberOfDuplicates(prep[0], CDS.inMap);
	//	CDS.printNumberOfDuplicates(prep[1], CDS.onMap);
	//	CDS.printNumberOfDuplicates(prep[2], CDS.ofMap);
		
		CDS.checkUniqueSentences();
	}

}
