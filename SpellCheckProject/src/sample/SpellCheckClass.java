package sample;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.xeustechnologies.googleapi.spelling.SpellChecker;
import org.xeustechnologies.googleapi.spelling.SpellCorrection;
import org.xeustechnologies.googleapi.spelling.SpellResponse;

public class SpellCheckClass {

	
	private static SpellChecker checker = new SpellChecker();
	private static SpellResponse response;
	
	public SpellCheckClass() {
		// TODO Auto-generated constructor stub
		List<Logger> loggers = Collections.<Logger>list(LogManager.getCurrentLoggers());
		loggers.add(LogManager.getRootLogger());
		for ( Logger logger : loggers ) {
		    logger.setLevel(Level.OFF);
		}
		//System.out.println("LOGGING OFF");
		
	}
	
	public static String correctSpell(String word)
	{
		String result = null;
		try {
		response = checker.check(word);
		if(response !=null)
		{
			SpellCorrection[] spelling = response.getCorrections();
			if(spelling != null)
			{
				result = spelling[0].getValue().split("\\t")[0];
			//	System.err.println("result");
			}
		}
		}
		catch(Exception e)
		{
			result = word;
			System.err.println(e.getMessage());
			//e.printStackTrace();
			//System.exit(0);
		}
		return result;
	}

	void run()
	{
		BufferedReader br = null;
		BufferedWriter bw = null;
		try
		{
			br = new BufferedReader(new FileReader(new File("out.txt")));
			bw = new BufferedWriter(new FileWriter(new File("result.txt")));
			String line;
			while((line = br.readLine()) != null)
			{
				String[] tokens = line.split("\\t");   // 3= image_name 4= tags
				System.out.println(tokens[3]+" "+tokens[4]);
				String[] tags = tokens[4].split(";");
				for(int i=0; i<tags.length; i++)
				{
					String corrected = correctSpell(tags[i]) ;
					if(corrected != null)
					{
						tags[i] = corrected; 
					}
				}
				bw.write(tokens[3]+"\t"+tags[0]+";"+tags[1]+";"+tags[2]);
				bw.newLine();
				
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
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SpellCheckClass checkClass = new SpellCheckClass();
		checkClass.run();
	}

}
