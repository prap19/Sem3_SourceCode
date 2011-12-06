package edu.nlp.ageattr.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import weka.core.pmml.FieldMetaInfo.Value;

public class MapUtil {

	public static LinkedHashMap<String, Double> sortHashMap(Map<String,Double> map)
	{
		//Map<String,Integer> tempMap=new LinkedHashMap<String,Integer>(map);
		LinkedHashMap<String,Double> sortedOutputMap = new LinkedHashMap<String, Double>();
		
		for(int i=0;i<map.size();i++){
		    Entry<String, Double> maxEntry=null;
		    Double maxValue=-1.0;
		    for(Map.Entry<String,Double> entry:map.entrySet()){
		        if(entry.getValue()>maxValue){
		            maxValue=entry.getValue();
		            maxEntry=entry;
		        }
		    }
		    map.remove(maxEntry.getKey());
		    sortedOutputMap.put(maxEntry.getKey(),maxEntry.getValue());
		}
		return sortedOutputMap;
	}
	
	
}
