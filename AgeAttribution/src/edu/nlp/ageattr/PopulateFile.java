package edu.nlp.ageattr;

import java.io.File;
import java.util.HashMap;

public class PopulateFile {
private HashMap<String,File> FileMap;
public static final String RSRC_TRAIN_TXT = System.getenv("NLP")+File.separator+"1500TrainTextFiles";
public static final String RSRC_TEST_TXT = System.getenv("NLP")+File.separator+"1500TrainTextFiles";

/*public static final String RSRC_TRAIN_POSDATA ="rsrc"+File.separator+"TrainPOSDataset";
public static final String RSRC_TRAIN_SCDATA ="rsrc"+File.separator+"TrainSCDataset";*/

/**
 * @return the fileMap
 */
public File getFileMap(String folderName) {
	return this.FileMap.get(folderName);
}

/**
 * @param fileMap the fileMap to set
 */
public void setFileMap(int fileIndex) {
	HashMap<String, File> fileMap = new HashMap<String, File>();

	File TextFolderTrain = new File(RSRC_TRAIN_TXT);
    File[] TextFilesTrain = TextFolderTrain.listFiles();
    fileMap.put("RSRC_TRAIN_TXT", TextFilesTrain[fileIndex]);

    File TextFolderTest = new File(RSRC_TEST_TXT);
    File[] TextFilesTest = TextFolderTest.listFiles();
    fileMap.put("RSRC_TEST_TXT", TextFilesTest[fileIndex]);
	
	
/*	File POSFolder = new File(RSRC_TRAIN_POSDATA);
	File[] POSFiles = POSFolder.listFiles();
	fileMap.put("RSRC_TRAIN_POSDATA", POSFiles[fileIndex]);
	
	File SentenceComplexityFolder = new File(RSRC_TRAIN_SCDATA);
	File[] SCFiles = SentenceComplexityFolder.listFiles();
	fileMap.put("RSRC_TRAIN_SCDATA", SCFiles[fileIndex]);*/

	this.FileMap = fileMap;
}


}
