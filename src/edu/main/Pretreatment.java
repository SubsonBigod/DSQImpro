package edu.main;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import edu.utils.FileUtil;
import edu.utils.StrUtil;

/**
 * @author Subson Bigod
 * @email  subsontding@gmail.com
**/
public class Pretreatment {
	
	private List<String> dataSet = new ArrayList<String>();
	
	public Pretreatment(
			String dataDir, 
			boolean removeNonWord, 
			boolean removeLowFrequencyWords, 
			int lessNum, 
			boolean removeSubstandardDocuments, 
			int minLen, 
			int maxLen, 
			String scrDir) 
					throws Exception {
		dataSet = FileUtil.fileToList(dataDir);
		if (removeNonWord) {
			long st = System.currentTimeMillis();
			removeNonWord();
			long et = System.currentTimeMillis();
			System.out.println("Remove non-word takes " + 
			(et-st)/1000.0 + " seconds.");
		}
		if (removeLowFrequencyWords) {
			long st = System.currentTimeMillis();
			removeLowFrequencyWords(lessNum);
			long et = System.currentTimeMillis();
			System.out.println("Remove low frequency word takes " + 
			(et-st)/1000.0 + " seconds.");
		}
		if (removeSubstandardDocuments) {
			long st = System.currentTimeMillis();
			removeSubstandardDocuments(minLen, maxLen);
			long et = System.currentTimeMillis();
			System.out.println("Remove substandard documents takes " + 
			(et-st)/1000.0 + " seconds.");
		}
		FileUtil.listToFile(dataSet, scrDir);
	}
	
	private void removeNonWord () throws Exception {
		for (int i = 0; i < dataSet.size(); i++) {
			String doc = dataSet.get(i);
			JSONObject obj = new JSONObject(doc);
			String text = obj.getString("text");
			String cluster = obj.getString("cluster");
			StringBuffer sb = new StringBuffer();
			sb.append("{\"text\": \"");
			for (String word : text.split("\\s+")) {
				if (StrUtil.isLetter(word)) continue;
				if (StrUtil.isLetters(word)) 
					sb.append(word).append(" ");
			}
			sb.append("\", \"cluster\": \"").append(cluster).append("\"}");
			dataSet.set(i, sb.toString());
		}
	}
	
	private void removeLowFrequencyWords (int lessNum) throws Exception {
		List<String> LFWords = new ArrayList<String>();
		new DocumentSet(dataSet, lessNum, LFWords);
		for (int i = 0; i < dataSet.size(); i++) {
			String doc = dataSet.get(i);
			JSONObject obj = new JSONObject(doc);
			String text = obj.getString("text");
			String cluster = obj.getString("cluster");
			StringBuffer sb = new StringBuffer();
			sb.append("{\"text\": \"");
			for (String word : text.split("\\s+")) {
				if (LFWords.contains(word)) continue;
				sb.append(word).append(" ");
			}
			sb.append("\", \"cluster\": \"").append(cluster).append("\"}");
			dataSet.set(i, sb.toString());
		}
	}
	
	private void removeSubstandardDocuments (int minLen, int maxLen) 
			throws Exception {
		for (int i = 0; i < dataSet.size(); i++) {
			String doc = dataSet.get(i);
			JSONObject obj = new JSONObject(doc);
			String text = obj.getString("text");
			int len = text.split("\\s+").length;
			if (len<minLen || len>maxLen) dataSet.remove(i);
		}
	}

}
