package create.dataset;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class MainClass {

	public static String FS= File.separator;
	public static String[] prep = {"in", "of", "on"};
	public static int NOF=6700;
	private static ArrayList<String>[] arr = new ArrayList[prep.length];
	
	
	//private static ArrayList<String> in = new ArrayList<String>();;
	//private static ArrayList<String> of = new ArrayList<String>();;
	//private static ArrayList<String> on = new ArrayList<String>();;
	
	static void ReadAndWriteDataSet(String folderName) throws IOException
	{
		
		  String path = "data"+FS+"data_stage_one"+FS+folderName;  // Folder path 
		 
		  String filename, line;
		  File folder = new File(path);
		  File[] listOfFiles = folder.listFiles();
		  BufferedReader br = null;
		  for(int i=0; i < listOfFiles.length; i++)
		  { 
			  System.out.println(listOfFiles[i].getName());
			  filename = path+FS+listOfFiles[i].getName();
			  try
			  {
				  br= new BufferedReader(new FileReader(new File(filename)));
				  while((line = br.readLine())!= null)
				  {
					for(int j=0; j<prep.length; j++)
					{
						StringTokenizer st = new StringTokenizer(line);
						while(st.hasMoreTokens())
							if(st.nextToken().equalsIgnoreCase(prep[j]))
							{	//System.out.println(line);
							
								if(arr[j] == null)
									arr[j] = new ArrayList<String>();
								
								arr[j].add(line.toLowerCase().replaceAll("\\p{Punct}","").trim().replaceAll("\\s+", " "));
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
		  
		  // Writes the entire arraylist(preposition wise seperated files)
		  
		  WriteSeperated(folderName);
		  
		  for(int i=0; i<prep.length; i++)
		  {
			  arr[i].clear();
		  }
		  
	}
	
	static void WriteSeperated(String folderName) throws IOException
	{
		String path= "data"+FS+"seperated"+FS+folderName;
		BufferedWriter bw= null, metabw= null;
		String metastr = "";
		//Writing Meta files
		try
		{
			metabw = new BufferedWriter(new FileWriter(new File(path+FS+"meta.info")));
			for(int i=0; i<prep.length; i++)
			{
				metastr+=prep[i]+"="+arr[i].size()+"\n";
			}
			metabw.write(metastr);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			metabw.close();
		}
		for(int i=0; i<prep.length; i++)
		{
			String filename = path+FS+prep[i]+".txt";
			try
			{
				bw = new BufferedWriter(new FileWriter(new File(filename)));
				for(int j=0; j<arr[i].size(); j++)
				{
					bw.write(arr[i].get(j));
					bw.newLine();
				}
			}
			catch(Exception e)
			{
				System.err.println("Exception: WriteSeperated() : "+e.getMessage());
				e.printStackTrace();
			}
			finally
			{
				try {
					bw.close();
					metabw.close();
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
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
		
			ReadAndWriteDataSet("training_set");
			ReadAndWriteDataSet("test_set");
			ReadAndWriteDataSet("development_set");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
		
		
		
		  System.out.println("in:"+arr[0].size()+" on:"+arr[1].size()+" of:"+arr[2].size()+" ");
		System.out.println("DONE");
	}

}
