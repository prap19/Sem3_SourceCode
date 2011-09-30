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
		String str =  "since my last letter  dated july 12  the congress has cleared and the president has signed the following acts that affect budget authority  outlays  or revenues for fiscal year 2005  the surface transportation extension act of 2005  part iii  public law 10935    lt  p  gt   amp  nbsp   amp  nbsp   amp  nbsp  the surface transportation extension act of 2005  part iv  public law 10937    lt  p  gt   amp  nbsp   amp  nbsp   amp  nbsp  an act approving the renewal of import restrictions contained in the burmese freedom and democracy act of 2005  public law 10939    lt  p  gt   amp  nbsp   amp  nbsp   amp  nbsp  the surface transportation extension act of 2005  part v  public law 10940    lt  p  gt   amp  nbsp   amp  nbsp   amp  nbsp  the interior appropriations act  2006  public law 10954    lt  p  gt   amp  nbsp   amp  nbsp   amp  nbsp  the energy policy act of 2005  public law 10958    lt  p  gt   amp  nbsp   amp  nbsp   amp  nbsp  the safe  accountable  flexible  efficient transportation equity act  a legacy for users  public law 10959   and  lt  p  gt   amp  nbsp   amp  nbsp   amp  nbsp  the emergency supplemental appropriations act to meet immediate needs arising from the consequences of hurricane katrina  2005  public law 10961   ";
		str = str.replaceAll("[^\\w&&[^\\s]]", "");
	    str = tagger.tagString(str);
		System.out.println(str);
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
