package com.linmalu.LinmaluLibrary.API;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class LinmaluRanking implements Comparator<String>
{
	public static LinkedHashMap<String, Integer> getRanking(HashMap<String, Integer> map)
	{
		TreeMap<String,Integer> key = new TreeMap<>(new LinmaluRanking(map));
		key.putAll(map);
		LinkedHashMap<String, Integer> ranking = new LinkedHashMap<>();
		for(String name : key.keySet())
		{
			ranking.put(name, map.get(name));
		}
		return ranking;
	}
	
	private Map<String, Integer> base;

	private LinmaluRanking(Map<String, Integer> base)
	{
		this.base = base;
	}
	public int compare(String a, String b)
	{
		if (base.get(a) >= base.get(b))
		{
			return -1;
		}
		else
		{
			return 1;
		}
	}
}
