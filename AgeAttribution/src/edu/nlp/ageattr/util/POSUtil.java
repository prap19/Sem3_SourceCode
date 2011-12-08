package edu.nlp.ageattr.util;

import java.io.IOException;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class POSUtil {
	
	private static MaxentTagger tagger = null;
	private POSUtil() {
		// TODO Auto-generated constructor stub
		if(tagger == null)
			try {
				tagger = new MaxentTagger("rsrc/left3words-distsim-wsj-0-18.tagger");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public static String getTaggedString(String str) {
		
		if(tagger == null) {
			new POSUtil(); 
		}
		String res=null;
		try {
			res = tagger.tagString(str);
		}catch(Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		return res;
	}
	
	public static String getAllPOS(String str) {
		
		String[] taggedW = POSUtil.getTaggedString(str).split("\\s+");
		StringBuilder res = new StringBuilder();
		int i=0;
		
		for(String word: taggedW) {
			try {
				if(word.length()>1)
					res.append(word.split("/")[1]+" ");
			}catch(ArrayIndexOutOfBoundsException e)
			{
				System.err.println("Exception: "+word+" len:"+word.length());
				e.printStackTrace();
			}
		}
		
		return res.toString().trim();
	}
	
	public static void main(String[] args) {
		System.out.println(POSUtil.getAllPOS("hey this works!!!!"));
	}
}


