package edu.nlp.ageattr.helper;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;

import java.io.FileWriter;
import java.io.IOException;

import edu.nlp.ageattr.processDataset.CreateDataset;

/**
 * Scripts for helping generate the senetence complexity of the the sentences in the blogs
 * @author piyush
 *
 */
public class SentenceComplexityHelperScripts {

	public void createCollinsParserBatFile(File InputFolder, String CollinsParserBatFile, String OutputFolderPath){
	BufferedWriter bufferedWriter = null;
	
	File files[];
	//File a  = new File("C:\\Users\\piyush\\workspace\\AgePrediction\\rsrc\\CleanXml\\a.xml");
	files = InputFolder.listFiles();
	int count =0;
    for (final File fileEntry : files) {
        String fileName = CreateDataset.proceesFileName(fileEntry); 
        String outputFileName = OutputFolderPath+=fileName;
        String inputFileName ="";
        String command ="gunzip -c models/model2/events.gz | code/parser "+"C:/"+" models/model2/grammar 10000 1 1 1 1 > "+outputFileName; 		
	
	try {
			bufferedWriter = new BufferedWriter(new FileWriter(CollinsParserBatFile));
		
		
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	}catch (IOException e) {
		e.printStackTrace();
	}finally{
		if(bufferedWriter!=null){
			try {
				bufferedWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	}
    }
}