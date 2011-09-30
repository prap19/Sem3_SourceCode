package create.dataset;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class POSTagging {

	private MaxentTagger tagger;
	
	public POSTagging()
	{
		try {
			this.tagger = new MaxentTagger("tagger"+MainClass.FS+"bidirectional-distsim-wsj-0-18.tagger") ;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void SampleTagging()
	{
		String str =  "as the highest body of elected officials in our country , we should be held to the highest ethical standards ";
		String taggedStr = tagger.tagString(str);
		System.out.println(taggedStr);
	}
	
	public void TagAll(String folderName)
	{
		String inputPath = "data"+MainClass.FS+"seperated";
		String outputPath = "data"+MainClass.FS+"tagged";
		String line;
		
		for(int i=0; i<MainClass.prep.length; i++)
		{
			String inputFile= inputPath+MainClass.FS+folderName+MainClass.FS+MainClass.prep[i]+".txt";
			String outputFile = outputPath+MainClass.FS+folderName+MainClass.FS+MainClass.prep[i]+".txt";
			BufferedReader br = null;
			BufferedWriter bw = null;
			
			if((new File(outputPath+MainClass.FS+folderName).exists())!= true)
			{
				(new File(outputPath+MainClass.FS+folderName)).mkdir();
			}
			try
			{
				
				br = new BufferedReader(new FileReader(new File(inputFile)));
				bw = new BufferedWriter(new FileWriter(new  File(outputFile)));
				long time = System.currentTimeMillis();
				while((line = br.readLine())!=null)
				{
					line= line.replaceAll("\\p{Punct}", "");
					bw.write(tagger.tagString(line));
					bw.newLine();
				}
				time = (System.currentTimeMillis() - time)/1000;
				System.out.println("File: "+ inputFile +" tagged in time= "+time/60+" mins "+time%60+"sec!");
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				try
				{
					br.close();
					bw.close();
				}catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		
		
	}
	
	
	public static void main(String args[])
	{
		POSTagging posTagging = new POSTagging();
		long totalTime = System.currentTimeMillis();	
		//posTagging.TagAll("development_set");
		//posTagging.TagAll("test_set");
		//posTagging.TagAll("training_set");
		posTagging.SampleTagging();
		totalTime = (System.currentTimeMillis()- totalTime)/1000;
		System.out.println("TOTAL TIME: "+totalTime/60+"mins "+totalTime%60+" secs");
	}
}
