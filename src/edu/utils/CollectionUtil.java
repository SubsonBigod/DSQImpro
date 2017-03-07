package edu.utils;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
/**
 * @author Subson Bigod
 * @email  subsontding@gmail.com
**/
public class CollectionUtil {

	@SuppressWarnings("rawtypes")
	public static String getKeyByValue(Map<?, String> hm, String value) {
		Set<?> s = hm.entrySet();
		Iterator<?> it = s.iterator();
		while (it.hasNext()) {
			Map.Entry m = (Map.Entry) it.next();
			if (m.getValue().equals(value))
				return m.getKey() + "";
		}
		return null;
	}

	public static List<Map.Entry<String, Double>> sortMapByValue(Map<String, Double> map) {
		List<Map.Entry<String, Double>> list = new ArrayList<Map.Entry<String, Double>>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
			@Override
			public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {
				double result = o2.getValue() - o1.getValue();
				if (result > 0) return -1;
				else if (result == 0) return 0;
				else return 1;
			}
		});
		return list;
	}
	
	public static List<Map.Entry<?, ?>> mapToList(Map<?, ?> map) {
		return new ArrayList<Map.Entry<?, ?>>(map.entrySet());
	}

	@SuppressWarnings("rawtypes")
	public static Map<Integer, String> swapStrKeysAndIntValues(Map<String, Integer> hm) {
		HashMap<Integer, String> nhm = new HashMap<Integer, String>();
		Set<?> s = hm.entrySet();
		Iterator<?> it = s.iterator();
		while (it.hasNext()) {
			Map.Entry m = (Map.Entry) it.next();
			nhm.put((Integer) m.getValue(), (String) m.getKey());
		}
		return nhm;
	}

	@SuppressWarnings("rawtypes")
	public static Map<String, Integer> swapIntKeysAndStrValues(Map<Integer, String> hm) {
		HashMap<String, Integer> nhm = new HashMap<String, Integer>();
		Set<?> s = hm.entrySet();
		Iterator<?> it = s.iterator();
		while (it.hasNext()) {
			Map.Entry m = (Map.Entry) it.next();
			nhm.put((String) m.getValue(), (Integer) m.getKey());
		}
		return nhm;
	}

	@SuppressWarnings("rawtypes")
	public static Map<Integer, Integer> swapIntKeysAndIntValues(Map<Integer, Integer> hm) {
		HashMap<Integer, Integer> nhm = new HashMap<Integer, Integer>();
		Set<?> s = hm.entrySet();
		Iterator<?> it = s.iterator();
		while (it.hasNext()) {
			Map.Entry m = (Map.Entry) it.next();
			nhm.put((Integer) m.getValue(), (Integer) m.getKey());
		}
		return nhm;
	}

	@SuppressWarnings("rawtypes")
	public static Map<String, String> swapStrKeysAndStrValues(Map<String, ?> hm) {
		HashMap<String, String> nhm = new HashMap<String, String>();
		Set<?> s = hm.entrySet();
		Iterator<?> it = s.iterator();
		while (it.hasNext()) {
			Map.Entry m = (Map.Entry) it.next();
			nhm.put((String) m.getValue(), (String) m.getKey());
		}
		return nhm;
	}

	@SuppressWarnings("rawtypes")
	public static Map<Double, String> swapStrKeysAndDouValues(Map<String, Double> hm) {
		HashMap<Double, String> nhm = new HashMap<Double, String>();
		Set<?> s = hm.entrySet();
		Iterator<?> it = s.iterator();
		while (it.hasNext()) {
			Map.Entry m = (Map.Entry) it.next();

			if (nhm.containsKey((Double) m.getValue())) {
				String has = nhm.get((Double) m.getValue());
				nhm.put((Double) m.getValue(), m.getKey() + "+" + has);
			}

			nhm.put((Double) m.getValue(), (String) m.getKey());
		}
		return nhm;
	}

	@SuppressWarnings("rawtypes")
	public static List<String> keysToList(Map<?, ?> map) {
		List<String> list = new ArrayList<String>();
		Set<?> set = map.entrySet();
		Iterator<?> iter = set.iterator();
		while (iter.hasNext()) {
			Map.Entry m = (Map.Entry) iter.next();
			list.add("" + m.getKey());
		}
		return list;
	}

}
