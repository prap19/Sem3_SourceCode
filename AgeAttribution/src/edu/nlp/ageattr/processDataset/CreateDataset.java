package edu.nlp.ageattr.processDataset;
/**
 * @author piyush
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.Buffer;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import edu.nlp.ageattr.helper.PosTaggging;

/**
 * parses the xml data files and creates the dataset objects
 * @author piyush
 *
 */
public class CreateDataset {
PosTaggging posTaggging; 
public CreateDataset(){
	try {
		posTaggging = new PosTaggging();
	} catch (IOException e) {
		e.printStackTrace();
	} catch (ClassNotFoundException e) {
		e.printStackTrace();
	}
}
	public ArrayList<PersonBlogs> createTrainingData(File folder) {
		ArrayList<PersonBlogs> personBlogs = new ArrayList<PersonBlogs>();
		File files[];
		//File a  = new File("C:\\Users\\piyush\\workspace\\AgePrediction\\rsrc\\CleanXml\\a.xml");
		files = folder.listFiles();
		int count =0;
	    for (final File fileEntry : files) {
	        if (fileEntry.isDirectory())
	        {
	            createTrainingData(fileEntry);
	        } else {
	        	String fileName = CreateDataset.proceesFileName(fileEntry); 
	        	
	            //System.out.println(fileEntry.getAbsolutePath());
	            PersonBlogs pBlogs = CreateDataset.CreatePersonBlog(fileEntry.getAbsoluteFile(),fileName);
	      //  	PersonBlogs pBlogs = CreatePersonBlog(a,fileName);
	        	personBlogs.add(pBlogs);
	        	count++;
	            System.out.println("The count is "+count);
	        }    
		}
	    System.out.println("The number of files are "+files.length);
	    return personBlogs;
	}
/**
 * returns the filename which contains information like the age, gender and id of the blogger	
 * @param fileEntry
 * @return
 */
public static String proceesFileName(File fileEntry) {
	String a = fileEntry.toString();
	String[] fnames;
	fnames = a.split("\\\\");
	//System.out.println("hey");
		return fnames[4];
	}
/**
 * parses one xml file and creates on personBlog object
 * @param absoluteFile
 * @param fileNameInfo 
 * @return
 */
	public static PersonBlogs CreatePersonBlog(File absoluteFile, String fileNameInfo) {
		PersonBlogs pBlogs = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			//Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();
			//System.out.println("the file is "+absoluteFile);
			//parse using builder to get DOM representation of the XML file
		//	cleanFile(absoluteFile);
			
			InputStream inputStream= new FileInputStream(absoluteFile);
			Reader reader = new InputStreamReader(inputStream, "UTF-8");
			
			
			InputSource is = new InputSource(reader);
			
			Document dom = db.parse(is);

			
		//	Document dom = db.parse(absoluteFile);
			
			
			
			
			pBlogs = parseDocument(dom,fileNameInfo);
			Element docEle = dom.getDocumentElement();
			NodeList n = docEle.getElementsByTagName("post");
			
			
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}finally{
			
		}
		return pBlogs;
	}
	
	public void cleanFile(File absoluteFile) throws FileNotFoundException{
		BufferedReader bufferedReader=null;
		bufferedReader = new BufferedReader(new FileReader(absoluteFile));
		BufferedWriter bufferedWriter = null;
		 
		String line;
		
		try {
			bufferedWriter = new BufferedWriter(new FileWriter("C:\\Users\\piyush\\workspace\\AgePrediction\\rsrc\\CleanXml\\a.xml"));
			while((line = bufferedReader.readLine())!=null){
				String newline  = CreateDataset.forXML(line);
				bufferedWriter.write(newline);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				bufferedWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static String forXML(String aText){
	    final StringBuilder result = new StringBuilder();
	    final StringCharacterIterator iterator = new StringCharacterIterator(aText);
	    char character =  iterator.current();
	    while (character != CharacterIterator.DONE ){
	  /*    if (character == '<') {
	        result.append("&lt;");
	      }
	      else if (character == '>') {
	        result.append("&gt;");
	      }
	      else if (character == '\"') {
	        result.append("&quot;");
	      }
	      else if (character == '\'') {
	        result.append("&#039;");
	      }*/
	      if (character == '&') {
	         result.append("&amp;");
	      }
	      else {
	        //the char is not a special one
	        //add it to the result as is
	        result.append(character);
	      }
	      character = iterator.next();
	    }
	    return result.toString();
	  }
	private static PersonBlogs parseDocument(Document dom, String fileNameInfo){
		//get the root element
		PersonBlogs pBlogs = null;
		Element docEle = dom.getDocumentElement();
		ArrayList<String> post  = new ArrayList<String>();
		//get a nodelist of elements
		NodeList nl = docEle.getElementsByTagName("post");
		if(nl != null && nl.getLength() > 0) {
			for(int i = 0 ; i < nl.getLength();i++) {

				//get the post element
				Element el = (Element)nl.item(i);
				String e =  el.getTextContent();
				String k = e.replaceAll("[\\n\\t]", "");
				String l = k.replaceAll("\\s\\s+", "");
				//add it to list
				post.add(l);
				
			}
		}
		pBlogs = CreatePersonBlogObject(post,fileNameInfo);
		return pBlogs;
	}
	/**
	 * given a a list of posts and other info of the blogger create the blogger object
	 * @param post
	 * @param fileNameInfo(of the form id.gender.age)
	 * @return
	 */
	private static PersonBlogs CreatePersonBlogObject(ArrayList<String> post,
			String fileNameInfo) {
		PersonBlogs personBlogs = null;
		int gender=0;
		String bloggerInfo[];
		bloggerInfo = fileNameInfo.split("\\.");
		if(bloggerInfo[1].equalsIgnoreCase("male")){
			gender = 1;
		}
		personBlogs = new PersonBlogs(Integer.parseInt(bloggerInfo[0]), Integer.parseInt(bloggerInfo[2]),gender , post);
		return personBlogs;
	}
	public void createCleanXMLFiles(File folder, String cleanXmlFolder) throws FileNotFoundException {
		File files[];
		File a  = new File("C:\\Users\\piyush\\workspace\\AgePrediction\\rsrc\\CleanXml\\a.xml");
		files = folder.listFiles();
		int count =0;
	     
		for (final File fileEntry : files) {
		        if (fileEntry.isDirectory())
		        {
		            createTrainingData(fileEntry);
		        } else {
		        	count++;
		        	System.out.println("count is "+count);
		        	String fileName = proceesFileName(fileEntry);
		        	String cleanXMLFileName = cleanXmlFolder;
		        	cleanXMLFileName += fileName;
		        	createCleanXMLFile(fileEntry,cleanXMLFileName);
		        	
		        	cleanXMLFileName="";
		        }
		 }
	}
	
	/**
	 * creates a clean xml file with the name cleanXMLFileName
	 * @param fileEntry
	 * @param cleanXMLFileName
	 * @throws FileNotFoundException 
	 */
	private void createCleanXMLFile(File fileEntry, String cleanXMLFileName) throws FileNotFoundException {
		BufferedReader bufferedReader=null;
		bufferedReader = new BufferedReader(new FileReader(fileEntry));
		BufferedWriter bufferedWriter = null;
		 
		String line;
		System.out.println("creating file "+cleanXMLFileName);
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(cleanXMLFileName));
			while((line = bufferedReader.readLine())!=null){
				String newline  = CreateDataset.forXML(line);
				bufferedWriter.write(newline);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				bufferedWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	public void createTextDataFiles(File xmldataset, File datatextfolder) {
		File files[];
		//File a  = new File("C:\\Users\\piyush\\workspace\\AgePrediction\\rsrc\\CleanXml\\a.xml");
		files = xmldataset.listFiles();
		int count =0;
	    for (final File fileEntry : files) {
	        	
	        	String fileName = CreateDataset.proceesFileName(fileEntry);
	        	System.out.println("the file is "+ fileName);
	        	PersonBlogs pBlogs = CreateDataset.CreatePersonBlog(fileEntry.getAbsoluteFile(),fileName);
	            if(pBlogs!=null){
	            	count++;
	            	String textFileName = CreateDataset.createTextFileName(fileName);
		            
	            	createBlogsTextFile(datatextfolder,pBlogs,textFileName);
	            	System.out.println("the numbrer of text files created are "+count);
	            }
	      }
	}
	private static String createTextFileName(String fileName) {
		String newFileName="";
		String a[] = fileName.split("\\.");
		for(int i=0;i<a.length-1;i++){
			newFileName+=a[i]+".";
		}
			
		return newFileName;
	}
	private void createBlogsTextFile(File datatextfolder, PersonBlogs pBlogs,
			String fileName) {
		BufferedWriter bufferedWriter = null;
		String fname = datatextfolder.getAbsolutePath()+"\\"+fileName+"txt"; 
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(fname));
			ArrayList<String> posts = pBlogs.getPosts();
			for(int i=0;i<posts.size();i++){
				bufferedWriter.write(posts.get(i));
				bufferedWriter.newLine();
			}
		} catch (IOException e) {
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
	/**
	 * creates a pos tagged dataset
	 * @param datatextfolder
	 * @param postaggeddataset
	 */
	public void createPosTaggedFiles(File datatextfolder, File postaggeddataset) {
		File files[];
		files = datatextfolder.listFiles();
		int count =0;
	    for (final File fileEntry : files) {
	 	count++;
	    String OutputfileName = CreateDataset.proceesFileName(fileEntry);
    	OutputfileName = postaggeddataset.getAbsolutePath()+"\\"+OutputfileName;
    	String InputFileName = fileEntry.getAbsolutePath();
	 	try {
			posTaggging.TagFile(InputFileName, OutputfileName);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	    System.out.println("the file count is "+count);
	    }
	}
	public void createConcatenatedFiles(File datatextfolder,
			File singleteensfile, File singlethiriesfile,
			File singletwentiesfile) {
		File files[];
		BufferedWriter teensOut = null,twentiesOut =null,thirteesOut=null;
		try {
			teensOut = new BufferedWriter(new FileWriter(singleteensfile));
			 twentiesOut = new BufferedWriter(new FileWriter(singletwentiesfile));
			 thirteesOut = new BufferedWriter(new FileWriter(singlethiriesfile));
			
			files = datatextfolder.listFiles();
			int count =0;
		    for (final File fileEntry : files) {
			 	count++;
			 	System.out.println("the count is "+count);
			 	String OutputfileName = CreateDataset.proceesFileName(fileEntry);
		    	if(getAuthorAge(OutputfileName).equalsIgnoreCase("teens")){
		    		writeToFile(teensOut, fileEntry);
		    	}else if(getAuthorAge(OutputfileName).equalsIgnoreCase("twenties")){
		    		writeToFile(twentiesOut, fileEntry);
		    	}else{
		    		writeToFile(thirteesOut, fileEntry);
		    	}
		    }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				teensOut.close();
				twentiesOut.close();
				thirteesOut.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	private void writeToFile(BufferedWriter out, File fileEntry) throws IOException {
		BufferedReader bufferedReader = null;
		try {
		bufferedReader = new BufferedReader(new FileReader(fileEntry));
		String line="";
		while((line = bufferedReader.readLine())!=null){
			out.write(line);
			out.newLine();
		}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}finally{
			bufferedReader.close();
		}
		
		
	}
	private String getAuthorAge(String outputfileName) {
		String[] fname = outputfileName.split("\\.");
		
		if(Integer.parseInt(fname[2])>=13 && Integer.parseInt(fname[2])<=17){
			return "teens";
		}
		else if(Integer.parseInt(fname[2])>=23 && Integer.parseInt(fname[2])<=27){
			return "twenties";
		}
		else{
			return "thirtees";
		}
	}

	public static String getBloggerAge(String outputfileName) {
		String[] fname = outputfileName.split("\\.");
		
		if(Integer.parseInt(fname[2])>=13 && Integer.parseInt(fname[2])<=17){
			return "10s";
		}
		else if(Integer.parseInt(fname[2])>=23 && Integer.parseInt(fname[2])<=27){
			return "20s";
		}
		else{
			return "30s";
		}
	}
	public void createEqualyDistributedTrainingDocs(File inputFolder,
			File outputFolder, int noofdocsperageclass) {
		File files[];
		int noOfTeenFiles = 0,noOfTwentieFile = 0,noOfThirteesFile = 0;
		BufferedWriter bufferedWriter = null;
		files = inputFolder.listFiles();
		int count =0;
	    for (final File fileEntry : files) {
		 	count++;
		 	System.out.println("the count is "+count);
		 	String OutputfileName = CreateDataset.proceesFileName(fileEntry);
		 	//System.out.println("hey");
		 	if(getAuthorAge(OutputfileName).equalsIgnoreCase("teens") && noOfTeenFiles<noofdocsperageclass){
		 		noOfTeenFiles++;
		 		createTextFile(fileEntry,outputFolder,OutputfileName);
		 	}else if(getAuthorAge(OutputfileName).equalsIgnoreCase("twenties") && noOfTwentieFile<noofdocsperageclass){
	    		noOfTwentieFile++;
	    		createTextFile(fileEntry,outputFolder,OutputfileName);
	    	}else{
	    		if(noOfThirteesFile<noofdocsperageclass){
	    			noOfThirteesFile++;
		    		createTextFile(fileEntry,outputFolder,OutputfileName);
	    		}
	    		
	    	}

		 	
	    }
		
		
	}
	private void createTextFile(File fileEntry, File outputFolder, String outputfileName) {
		BufferedReader bufferedReader = null;
		BufferedWriter bufferedWriter = null;
		
		try {
			bufferedReader = new BufferedReader(new FileReader(fileEntry));
			bufferedWriter = new BufferedWriter(new FileWriter(outputFolder+"\\"+outputfileName));
			String line ="";
			while((line  = bufferedReader.readLine())!=null){
				bufferedWriter.write(line);
				bufferedWriter.newLine();
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
				bufferedWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
	}
	public void createNormalisedWordFreqFile(File teensfilewordfreq,
		File teensfilewordfreqnormalised) {
		BufferedReader bufferedReader = null;
		BufferedWriter bufferedWriter = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(teensfilewordfreq));
			bufferedWriter = new BufferedWriter(new FileWriter(teensfilewordfreqnormalised));
			String line;
			while((line = bufferedReader.readLine())!=null){
				String[] a = line.split("\\s+");
				double freq = (double)Integer.parseInt(a[1])/10000;
				bufferedWriter.write(a[0]+" "+freq);
				bufferedWriter.newLine();
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
				bufferedWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}
	public void createWordFrequencyNormalizedFile(File inputFile,
			File ouputFile) {
		BufferedReader bufferedReader = null,bufReader = null;
		BufferedWriter bufferedWriter = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(inputFile));
			String line;
			int countOfWords=0;
			while((line = bufferedReader.readLine())!=null){
				String[] a = line.split("\\s+");
				countOfWords+= Integer.parseInt(a[1]);	
			}
			bufferedReader.close();
			bufReader = new BufferedReader(new FileReader(inputFile));
			bufferedWriter = new BufferedWriter(new FileWriter(ouputFile));
			while((line = bufReader.readLine())!=null){
				String[] a = line.split("\\s+");
				double freq = (double)Integer.parseInt(a[1])/countOfWords;
				bufferedWriter.write(a[0]+" "+freq);
				bufferedWriter.newLine();	
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
				bufferedWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	}
	
	
}
