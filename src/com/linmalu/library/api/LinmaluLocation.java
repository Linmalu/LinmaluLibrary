package com.linmalu.library.api;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.LinkedHashMap;
import java.util.Map;

public class LinmaluLocation extends Location implements ConfigurationSerializable
{
	private static final String WORLD = "world";
	private static final String X = "x";
	private static final String Y = "y";
	private static final String Z = "z";
	private static final String YAW = "yaw";
	private static final String PITCH = "pitch";

	/**
	 * LinmaluLocation 변환하기
	 */
	public static LinmaluLocation getLinmaluLocation(Location loc)
	{
		return new LinmaluLocation(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
	}

	public LinmaluLocation(World world, double x, double y, double z)
	{
		super(world, x, y, z);
	}

	public LinmaluLocation(World world, double x, double y, double z, float yaw, float pitch)
	{
		super(world, x, y, z, yaw, pitch);
	}

	public static LinmaluLocation deserialize(Map<String, Object> map)
	{
		World world = Bukkit.getWorld((String)map.get(WORLD));
		if(world == null)
		{
			return null;
		}
		return new LinmaluLocation(world, (double)map.get(X), (double)map.get(Y), (double)map.get(Z), (float)map.get(YAW), (float)map.get(PITCH));
	}

	@Override
	public Map<String, Object> serialize()
	{
		Map<String, Object> map = new LinkedHashMap<>();
		map.put(WORLD, getWorld().getName());
		map.put(X, getX());
		map.put(Y, getY());
		map.put(Z, getZ());
		map.put(YAW, getYaw());
		map.put(PITCH, getPitch());
		return map;
	}
}
