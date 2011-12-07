package edu.nlp.ageattr.interfaces;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import edu.nlp.ageattr.PopulateFile;
import edu.nlp.ageattr.SVNInterface;
import edu.nlp.ageattr.helper.LexicalStylisticFeatures.Emoticons;
import edu.nlp.ageattr.processDataset.CreateDataset;


/**
 * @author akshaysnatu
 * Class to create a Emoticon feature
 */
public class EmoticonFeature implements SVNInterface {

	Emoticons emoticons;
	
	public EmoticonFeature() {
		// TODO Auto-generated constructor stub
		emoticons = new Emoticons();
	}
	
	
	
	@Override
	public int getNumberOfAttributes() {
		// TODO Auto-generated method stub
		try {
			Emoticons.EmotiMapCreate();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 1;
		
	}

	@Override
	public void addAttributes(FastVector fastVector,
			HashMap<String, Integer> featureList) {
		// TODO Auto-generated method stub
		int featureCount = featureList.size();
		Attribute attribute = new Attribute("emoticons");
		fastVector.addElement(attribute);
		featureList.put("emoticons", featureCount++);

	}

	@Override
	public void addVector(FastVector fastVector, Instance trainInstance,
			boolean train, PopulateFile populateFile,
			HashMap<String, Integer> featureList) {
		// TODO Auto-generated method stub
		File blogger;
		int countOfWords = 0,noOfEmoticons = 0;
		
		if(train == true){
			blogger = populateFile.getFileMap(RSRC_TRAIN_TXT);
		}
		else{
			blogger = populateFile.getFileMap(RSRC_TEST_TXT);
		}
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(blogger));
			String line="";
			countOfWords =0;
			while((line = bufferedReader.readLine())!=null){
				String s[] = line.split("\\s+");
				countOfWords+= s.length;
				for(int i=0;i<s.length;i++){
					if(Emoticons.isEmoticon(s[i].toLowerCase()))
						noOfEmoticons++;
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				bufferedReader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String fileName = CreateDataset.proceesFileName(blogger);
		float val = (float) noOfEmoticons/countOfWords;
		trainInstance.setValue((Attribute)fastVector.elementAt(featureList.get("emoticons")), val);
		trainInstance.setValue((Attribute)fastVector.elementAt(fastVector.size()-1), CreateDataset.getBloggerAge(fileName));
	}

	@Override
	public File getDataFile(String FolderName) {
		// TODO Auto-generated method stub
		return null;
	}

}
