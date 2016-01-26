package com.linmalu.linmalulibrary.api;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class LinmaluRanking<T extends Number> implements Comparator<String>
{
	@Deprecated
	public static LinkedHashMap<String, Integer> getRanking(HashMap<String, Integer> map)
	{
		return getRanking(map, false);
	}
	public static <T extends Number> LinkedHashMap<String, T> getRanking(HashMap<String, T> map, boolean ascendingOrder)
	{
		TreeMap<String, T> key = new TreeMap<>(new LinmaluRanking<T>(map, ascendingOrder));
		key.putAll(map);
		LinkedHashMap<String, T> ranking = new LinkedHashMap<>();
		for(String name : key.keySet())
		{
			ranking.put(name, map.get(name));
		}
		return ranking;
	}

	private Map<String, T> base;
	private boolean ascendingOrder;

	private LinmaluRanking(HashMap<String, T> map, boolean ascendingOrder)
	{
		this.base = map;
		this.ascendingOrder = ascendingOrder;
	}
	@Override
	public int compare(String a, String b)
	{
		int compare = new BigDecimal(base.get(a).toString()).compareTo(new BigDecimal(base.get(b).toString()));
		if(compare == 0)
		{
			compare = 1;
		}
		return ascendingOrder ? compare : compare * -1;
	}
}
