package edu.main;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Subson Bigod
 * @email  subsontding@gmail.com
**/
public class Document {
	
	private Map<String, Integer> docMessage = 
			new HashMap<String, Integer>();
	private String cluster;
	private int docLen = 0;
	
	public Map<String, Integer> getDocMessage() {
		return docMessage;
	}
	public String getCluster() {
		return cluster;
	}
	public int getDocLen() {
		return docLen;
	}
	
	public Document(String text, 
			HashMap<String, Integer> dicFre) {
		for (String str : text.split("\\s+")) {
			if (str.equals("")) continue;
			if (dicFre.containsKey(str)) {
				dicFre.put(str, dicFre.get(str)+1);
			} else {
				dicFre.put(str, 1);
			}
		}
	}

	public Document(
			String text, 
			String cluster, 
			HashMap<String, 
			HashMap<String, Integer>> clusterToWordToFreMapMap) {
		this.cluster = cluster;
		for (String str : text.split("\\s+")) {
			if (str.equals("")) continue;
			docLen++;
			if (docMessage.containsKey(str)) {
				docMessage.put(str, docMessage.get(str)+1);
			} else {
				docMessage.put(str, 1);
			}
			
			HashMap<String, Integer> wordToFreMap;
			if (clusterToWordToFreMapMap.containsKey(cluster)) {
				wordToFreMap = clusterToWordToFreMapMap.get(cluster);
				if (wordToFreMap.containsKey(str)) {
					wordToFreMap.put(str, wordToFreMap.get(str)+1);
				} else {
					wordToFreMap.put(str, 1);
				}
			} else {
				wordToFreMap = new HashMap<String, Integer>();
				wordToFreMap.put(str, 1);
			}
			clusterToWordToFreMapMap.put(cluster, wordToFreMap);
		}
	}

}
