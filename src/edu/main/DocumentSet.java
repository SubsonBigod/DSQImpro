package edu.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONObject;
import edu.utils.CollectionUtil;

/**
 * @author Subson Bigod
 * @email  subsontding@gmail.com
**/
public class DocumentSet {
	
	private int docNum = 0;
	private int clusterNum = 0;
	private List<String> clusters;
	private int dictionarySize = 0;
	private HashMap<String, Integer> dictionaryToFreMap =
			new HashMap<String, Integer>();
	private HashMap<String, ArrayList<Document>> clusterToDocListMap = 
			new HashMap<String, ArrayList<Document>>();
	
	public List<String> getClusters() {
		return clusters;
	}
	public HashMap<String, Integer> getDictionaryToFreMap() {
		return dictionaryToFreMap;
	}
	public int getDocNum() {
		return docNum;
	}
	public int getClusterNum() {
		return clusterNum;
	}
	public int getDictionarySize() {
		return dictionarySize;
	}

	public HashMap<String, ArrayList<Document>> getClusterToDocListMap() {
		return clusterToDocListMap;
	}
	
	public DocumentSet(
			List<String> dataSet, 
			int lessNum, 
			List<String> LFWords) 
					throws Exception {
		HashMap<String, Integer> dicFre = new HashMap<String, Integer>();
		for (String line : dataSet) {
			JSONObject obj = new JSONObject(line);
			String text = obj.getString("text");
			new Document(text, dicFre);
		}
		List<Map.Entry<String, Integer>> list = 
				new ArrayList<Map.Entry<String, Integer>>(dicFre.entrySet());
		for (Entry<String, Integer> entry : list) {
			if (entry.getValue() < lessNum) 
				LFWords.add(entry.getKey());
		}
	}

	public DocumentSet(
			String dataDir, 
			HashMap<String, 
			HashMap<String, Integer>> clusterToWordToFreMapMap) 
					throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(dataDir));
		String line;
		
		long startTime = System.currentTimeMillis();
		
		while((line=br.readLine()) != null){
			docNum++;
			JSONObject obj = new JSONObject(line);
			String text = obj.getString("text");
			String cluster = obj.getString("cluster");
			Document document = new Document(
					text, 
					cluster, 
					clusterToWordToFreMapMap);
			ArrayList<Document> docs = clusterToDocListMap.get(cluster);
			if (docs==null) docs = new ArrayList<Document>();
			docs.add(document);
			clusterToDocListMap.put(cluster, docs);
		}
		br.close();
		
		long endTime = System.currentTimeMillis();
		System.out.println("Read file takes "+ 
		(endTime-startTime)/1000.0 + " seconds.");
		
		clusterNum = clusterToDocListMap.size();
		clusters = CollectionUtil.keysToList(clusterToDocListMap);
		for (String cluster : clusters) {
			HashMap<String, Integer> wordToFreMap = 
					clusterToWordToFreMapMap.get(cluster);
			List<String> words = CollectionUtil.keysToList(wordToFreMap);
			for (String word : words) {
				if (dictionaryToFreMap.containsKey(word)) {
					int fre = dictionaryToFreMap.get(word) + 
							wordToFreMap.get(word);
					dictionaryToFreMap.put(word, fre);
				} else {
					dictionaryToFreMap.put(word, wordToFreMap.get(word));
				}
			}
		}
		dictionarySize = dictionaryToFreMap.size();
	}

}
