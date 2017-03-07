package edu.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.utils.CollectionUtil;
import edu.utils.FileUtil;

/**
 * @author Subson Bigod
 * @email subsontding@gmail.com
 **/
public class FeatureObtain {

	private HashMap<String, HashMap<String, Integer>> clusterToWordToFreMapMap;
	private DocumentSet documentSet;
	
	private List<String> clusters;
	private HashMap<String, Integer> dictToFreMap;
	private List<String> dictionary;
	private HashMap<String, Double> dicToEntropyMap;

	public FeatureObtain(
			String dataDir, 
			double increment) 
					throws Exception {
		clusterToWordToFreMapMap = new 
				HashMap<String, HashMap<String, Integer>>();
		documentSet = new DocumentSet(
				dataDir, 
				clusterToWordToFreMapMap);

		clusters = documentSet.getClusters();
		dictToFreMap = documentSet.getDictionaryToFreMap();
		dictionary = CollectionUtil.keysToList(dictToFreMap);
		dicToEntropyMap = new HashMap<String, Double>();
		calculateEntropy(increment);
	}

	public List<String> getClusters() {
		return clusters;
	}

	private void calculateEntropy(double increment) throws Exception {
		for (String cluster : clusters) {
			HashMap<String, Integer> wordToFreMap = 
					clusterToWordToFreMapMap.get(cluster);
			for (String dic : dictionary) {
				double entropy = 0.0;
				if (dicToEntropyMap.containsKey(dic)) {
					entropy = dicToEntropyMap.get(dic);
				}
				double fre;
				if (wordToFreMap.containsKey(dic)) {
					fre = wordToFreMap.get(dic) + increment;
				} else {
					fre = increment;
				}
				double allFre = dictToFreMap.get(dic) + 3 * increment;
				entropy += entropy(fre/allFre, 2.0);
				dicToEntropyMap.put(dic, entropy);
			}
		}
	}

	private double entropy(double p_c_w, double base) 
			throws Exception {
		if (p_c_w < 0) 
			throw new Exception("Log of negative numbers is not defined.");
		if (p_c_w == 0.0) return 0.0;
		return -p_c_w * Math.log(p_c_w) / Math.log(base);
	}
	
	public List<String> saveClusterWordsFrequency (
			String clusterWordsFrequencyJsonPath) {
		List<String> clusterWordsFrequency = new ArrayList<String>();
		for (String dic : dictionary) {
			StringBuffer sb = new StringBuffer();
			sb.append("{\"word\": \"").append(dic).append("\", ");
			for (String cluster : clusters) {
				sb.append("\"").append(cluster).append("\": ");
				int fre = 0;
				if (clusterToWordToFreMapMap.get(cluster).get(dic)!=null) 
					fre = clusterToWordToFreMapMap.get(cluster).get(dic);
				sb.append(fre).append(", ");
			}
			sb.delete(sb.length()-2, sb.length()).append("}");
			clusterWordsFrequency.add(sb.toString());
		}
		FileUtil.listToFile(clusterWordsFrequency, clusterWordsFrequencyJsonPath);
		return clusterWordsFrequency;
	}
	
	public List<Entry<String, Double>> saveAllSortFeatures (
			String allFeaturesSortPath) {
		List<Entry<String, Double>> ASFeatures = 
				CollectionUtil.sortMapByValue(dicToEntropyMap);
		FileUtil.listToFile(ASFeatures, allFeaturesSortPath);
		return ASFeatures;
	}
	
	public List<String> saveLowFrequencyWords (
			int lessNum, String LFWordsParh) {
		List<String> LFWords = new ArrayList<String>();
		for (String dict : dictionary) {
			int wordFre = dictToFreMap.get(dict);
			if (wordFre < lessNum) 
				LFWords.add(dict);
		}
		FileUtil.listToFile(LFWords, LFWordsParh);
		return LFWords;
	}
	
	public Map<String, List<Map.Entry<String, Double>>> getClusterToFeaturesMap () {
		Map<String, String> dicCusterMap = new HashMap<String, String>();
		Map<String, Integer> dicFreMap = new HashMap<String, Integer>();
		for (String cluster : clusters) {
			HashMap<String, Integer> wordToFreMap = 
					clusterToWordToFreMapMap.get(cluster);
			for (String dic : dictionary) {
				if (wordToFreMap.containsKey(dic)) {
					if ((!dicFreMap.containsKey(dic)) || 
							wordToFreMap.get(dic) > dicFreMap.get(dic)) {
						dicFreMap.put(dic, wordToFreMap.get(dic));
						dicCusterMap.put(dic, cluster);
					}
				}
			}
		}
		
		HashMap<String, HashMap<String, Double>> clusterWordToEntropy = 
				new HashMap<String, HashMap<String, Double>>();
		for (String dic : dictionary) {
			if (!dicCusterMap.containsKey(dic)) continue;
			String cluster = dicCusterMap.get(dic);
			double entropt = dicToEntropyMap.get(dic);
			
			HashMap<String, Double> map;
			if (clusterWordToEntropy.containsKey(cluster)) {
				map = clusterWordToEntropy.get(cluster);
			} else {
				map = new HashMap<String, Double>();
			}
			map.put(dic, entropt);
			
			clusterWordToEntropy.put(cluster, map);
		}
		
		HashMap<String, List<Map.Entry<String, Double>>> result = 
				new HashMap<String, List<Map.Entry<String, Double>>>();
		for (String cluster : clusters) {
			List<Map.Entry<String, Double>> clusterFeatures = 
					CollectionUtil.sortMapByValue(clusterWordToEntropy.get(cluster)); 
			result.put(cluster, clusterFeatures);
		}
		return result;
	}
	
}
