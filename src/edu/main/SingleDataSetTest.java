package edu.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.utils.FileUtil;

/**
 * @author Subson Bigod
 * @email  subsontding@gmail.com
**/
public class SingleDataSetTest {
	
	public static void main(String[] args) throws Exception {
		long startTime = System.currentTimeMillis();
		
		//data preprocessing (This module is unnecessary)
		new Pretreatment(
				"data/subson.json", //dataset's path
				true, //remove non-word or not
				true, //remove low-frequency words or not
				4, //low-frequency words is that frequency less than 4
				true, //remove substandard documents or not
				4, //substandard documents is the less 4 words
				100000, //substandard documents is the more 100000 words
				"data/subson_new.json"); //new dataset path
		
		//feature selection
		FeatureObtain featureObtain = new 
				FeatureObtain("data/subson_new.json", 1.0);
		//get the features of each cluster
		Map<String, List<Map.Entry<String, Double>>> clusterFeatures = 
				featureObtain.getClusterToFeaturesMap();
		
		//the following codes is unnecessary
		//save the word frequency of each cluster in JSON format
		featureObtain.saveClusterWordsFrequency(
				"data/clusterWordsFrequency.json");
		//save all features in ascending order of the entropy
		featureObtain.saveAllSortFeatures(
				"data/allSortFeatures.txt");
		//save all words that their frequency smaller than 4
		featureObtain.saveLowFrequencyWords(4, 
				"data/less4timesWords.txt");
		//save all features of each cluster in ascending order of the entropy
		for (String cluster : featureObtain.getClusters()) {
			FileUtil.listToFile(clusterFeatures.get(cluster), 
					"data/cluster"+cluster+"Feature.txt");
		}
		
		//change the type of the features of each cluster for DSQImpro's input
		//"Map<String, List<Map.Entry<String, Double>>>" to "Map<String, List<String>>"
		Map<String, List<String>> clusterFeatureList = 
				new HashMap<String, List<String>>();
		for (String cluster : featureObtain.getClusters()) {
			List<Map.Entry<String, Double>> featureToEntropy = 
					clusterFeatures.get(cluster);
			List<String> features = new ArrayList<String>();
			for (Entry<String, Double> feature : featureToEntropy) {
				features.add(feature.getKey());
			}
			clusterFeatureList.put(cluster, features);
		}
		
		//initialize parameters for DSQImpro's input
		//the "Map<String, Integer>" save the numbers of document of each cluster
		Map<String, Integer> subClusterDocumentLength = 
				new HashMap<String, Integer>();
		for (String cluster : featureObtain.getClusters()) {
			subClusterDocumentLength.put(cluster, 200);
		}
		
		ArrayList<String> subDataSet = new ArrayList<String>();
		//data improving
		new DSQImpro(
				"data/subson_new.json", //dataset path
				clusterFeatureList, //the map of the features of each cluster
				30, //influential feature Number
				subClusterDocumentLength, //the numbers of document of each cluster
				subDataSet); //the List of sub-dataset that we need
		
		//save the sub-dataset that we need
		FileUtil.listToFile(subDataSet, "data/subDataSet.json");
		
		long endTime = System.currentTimeMillis();
		System.out.println("The whole process takes " + 
		(endTime-startTime)/1000.0 + " seconds.");
	}

}
