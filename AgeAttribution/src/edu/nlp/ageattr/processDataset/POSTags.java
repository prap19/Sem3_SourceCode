package edu.nlp.ageattr.processDataset;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

import edu.nlp.ageattr.util.POSUtil;

public class POSTags {

	private static final String traininput = System.getenv("NLP")+File.separator+"1500TrainTextFiles";
	private static final String trainoutput = System.getenv("NLP")+File.separator+"1500POSTrainTextFiles";
	
	private static final String testinput = System.getenv("NLP")+File.separator+"1500TestTextFiles";
	private static final String testoutput = System.getenv("NLP")+File.separator+"1500POSTestTextFiles";
	
	public POSTags() {
		// TODO Auto-generated constructor stub
		
	}
			
	public void create() {
		File dir = new File(testinput);
		File[] files =  dir.listFiles();
		BufferedReader br = null;
		BufferedWriter bw = null;
		String line;
		System.out.println("Creating begun:"+testinput);
		for(int i=0; i<files.length; i++) {
			try {
			//	System.out.println(files[i].getAbsolutePath());
				br = new BufferedReader(new FileReader(new File(files[i].getAbsolutePath())));
				//System.out.println(br.read());
				File out = new File(testoutput+File.separator+files[i].getName());
				bw = new BufferedWriter(new FileWriter(out));
				System.out.println(i+"# "+files[i].getName()+"--> "+out.getName());
				while((line = br.readLine()) != null) {
					bw.write(POSUtil.getAllPOS(line.replaceAll("[^\\p{L}\\p{N}]", " ").trim()));
					bw.newLine();
				}
				
				br.close();
				bw.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
		POSTags posTags = new POSTags();
		posTags.create();
	}

}
