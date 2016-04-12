package com.linmalu.library.api;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class LinmaluRanking<K, V extends Number> implements Comparator<K>
{
	public static <K, V extends Number> LinkedHashMap<K, V> getRanking(Map<K, V> map, boolean ascendingOrder)
	{
		TreeMap<K, V> key = new TreeMap<>(new LinmaluRanking<K, V>(map, ascendingOrder));
		key.putAll(map);
		LinkedHashMap<K, V> ranking = new LinkedHashMap<>();
		for(K name : key.keySet())
		{
			ranking.put(name, map.get(name));
		}
		return ranking;
	}

	private final Map<K, V> map;
	private final boolean ascendingOrder;

	private LinmaluRanking(Map<K, V> map, boolean ascendingOrder)
	{
		this.map = map;
		this.ascendingOrder = ascendingOrder;
	}
	@Override
	public int compare(K a, K b)
	{
		int compare = new BigDecimal(String.valueOf(map.get(a))).compareTo(new BigDecimal(String.valueOf(map.get(b))));
		if(compare == 0)
		{
			compare = 1;
		}
		return ascendingOrder ? compare : -compare;
	}
}
