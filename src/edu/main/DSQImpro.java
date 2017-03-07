package edu.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import edu.utils.CollectionUtil;
import edu.utils.FileUtil;

/**
 * @author Subson Bigod
 * @email  subsontding@gmail.com
**/
public class DSQImpro {
	
	private List<String> dataSet = new ArrayList<String>();
	
	public DSQImpro(
			String dataDir, 
			Map<String, List<String>> clusterFeatures, 
			int influentialFeatureNum, 
			Map<String, Integer> subClusterDocumentLength, 
			ArrayList<String> subDataSet) 
					throws Exception {
		dataSet = FileUtil.fileToList(dataDir);
		
		List<String> clusters = CollectionUtil.keysToList(clusterFeatures);
		int maxfeatureNum = 100000000;
		for (String cluster : clusters) {
			int featureNum = clusterFeatures.get(cluster).size();
			if (maxfeatureNum > featureNum) maxfeatureNum = featureNum;
		}
		if (influentialFeatureNum > maxfeatureNum) 
			influentialFeatureNum = maxfeatureNum;
		boolean isEnd = false;
		List<String> subDataSetMessage = new ArrayList<String>();
		for (int i = influentialFeatureNum; i > 0; i--) {
			if (isEnd) break;
			for (int j = i; j < influentialFeatureNum; j++) {
				if (isEnd) break;
				int accordTimes = 0;
				subDataSetMessage.clear();
				subDataSet.clear();
				for (String cluster : clusters) {
					ArrayList<String> subClusterDocuments = 
							getMoreContainFeaturesDocuments(
									cluster, 
									clusterFeatures.get(cluster), 
									j, 
									i);
					if (subClusterDocuments.size() < 
					subClusterDocumentLength.get(cluster)) {
						break;
					} else {
						accordTimes++;
					}
					subDataSetMessage.add("Cluster " + cluster + 
							"\'s documents Number of subDataSet is " + 
							subClusterDocuments.size() + ".");
					subDataSet.addAll(subClusterDocuments);
					if (accordTimes==clusters.size()) {
						isEnd = true;
						for (String eachSubDataSetMessage : subDataSetMessage) {
							System.out.println(eachSubDataSetMessage);
						}
						System.out.println(
								"getMoreContainFeaturesDocuments("+j+","+i+").");
					}
				}
			}
		}
	}
	
	private ArrayList<String> getMoreContainFeaturesDocuments (
			String clustering, 
			List<String> clusterFeatures, 
			int logicOrFeatureNum, 
			int logicAndFeatureNum) 
					throws Exception {
		ArrayList<String> subDataSet = new ArrayList<String>();
		for (int i = 0; i < dataSet.size(); i++) {
			String line = dataSet.get(i);
			JSONObject obj = new JSONObject(line);
			String text = obj.getString("text");
			String cluster = obj.getString("cluster");
			if (cluster.equals(clustering)) {
				int containFeaturesNum = 0;
				for (int j = 0; j < logicOrFeatureNum; j++) {
					if (text.contains(clusterFeatures.get(j)+" ")) {
						containFeaturesNum++;
						if (containFeaturesNum >= logicAndFeatureNum) {
							subDataSet.add(line);
							break;
						}
					}
				}
			}
		}
		return subDataSet;
	}

}
