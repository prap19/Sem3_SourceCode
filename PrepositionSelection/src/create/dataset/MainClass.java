package create.dataset;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class MainClass {

	private static String FS= File.separator;
	private static String[] prep = {"in", "of", "on"};
	private static ArrayList<String>[] arr = new ArrayList[prep.length];
	
	//private static ArrayList<String> in = new ArrayList<String>();;
	//private static ArrayList<String> of = new ArrayList<String>();;
	//private static ArrayList<String> on = new ArrayList<String>();;
	
	static void ReadDataSet() throws IOException
	{
		
		 String path = "data"+FS+"data_stage_one"+FS+"training_set";  // Folder path 
		 
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
					for(int j=0; j<3; j++)
					{
						StringTokenizer st = new StringTokenizer(line);
						while(st.hasMoreTokens())
							if(st.nextToken().equalsIgnoreCase(prep[j]))
							{	//System.out.println(line);
							
								if(arr[j] == null)
									arr[j] = new ArrayList<String>();
								System.err.println("MATCHED: " + prep[j]+": "+line);
								System.out.println("SIZE["+j+": "+arr[j].size());
								arr[j].add(line.toLowerCase());
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
	
	static void WriteSeperated(ArrayList<String> arr)
	{
		
	}
	
	
	static void AddToOf(String line)
	{
		
	}
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		ReadDataSet();
	
		  System.out.println("in:"+arr[0].size()+" on:"+arr[1].size()+" of:"+arr[2].size()+" ");
		System.out.println("DONE");
	}

}
