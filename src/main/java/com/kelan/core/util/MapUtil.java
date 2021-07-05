package com.kelan.core.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class MapUtil {
	
	/**
	 * Map按值降序排序
	 * @param oriMap
	 * @return
	 */
	public static Map<String, Double> sortMapByValue(Map<String, Double> oriMap) {
		Map<String, Double> sortedMap = new LinkedHashMap<>();
	    if (oriMap != null && !oriMap.isEmpty()) {
	        List<Map.Entry<String, Double>> entryList = new ArrayList<>(oriMap.entrySet());
	        Collections.sort(entryList, new Comparator<Map.Entry<String, Double>>() {
                public int compare(Entry<String, Double> entry1,
                        Entry<String, Double> entry2) {
                    return entry2.getValue().compareTo(entry1.getValue());
                }
            });
	        Iterator<Map.Entry<String, Double>> iter = entryList.iterator();
	        Map.Entry<String, Double> tmpEntry = null;
	        while (iter.hasNext()) {
	            tmpEntry = iter.next();
	            sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
	        }
	    }
	    return sortedMap;
	}

}
