package com.linmalu.library.api;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.LinkedHashMap;
import java.util.Map;

public class LinmaluLocation extends Location implements ConfigurationSerializable
{
	public LinmaluLocation(Location location)
	{
		super(location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
	}

	public LinmaluLocation(World world, double x, double y, double z)
	{
		super(world, x, y, z);
	}

	public LinmaluLocation(World world, double x, double y, double z, float yaw, float pitch)
	{
		super(world, x, y, z, yaw, pitch);
	}

	public LinmaluLocation(World world, double x, double y, double z, double yaw, double pitch)
	{
		super(world, x, y, z, (float)yaw, (float)pitch);
	}

	private static final String CONFIG_WORLD = "world";
	private static final String CONFIG_X = "x";
	private static final String CONFIG_Y = "y";
	private static final String CONFIG_Z = "z";
	private static final String CONFIG_YAW = "yaw";
	private static final String CONFIG_PITCH = "pitch";

	@Override
	public Map<String, Object> serialize()
	{
		Map<String, Object> map = new LinkedHashMap<>();
		map.put(CONFIG_WORLD, getWorld().getName());
		map.put(CONFIG_X, getX());
		map.put(CONFIG_Y, getY());
		map.put(CONFIG_Z, getZ());
		map.put(CONFIG_YAW, getYaw());
		map.put(CONFIG_PITCH, getPitch());
		return map;
	}

	public static LinmaluLocation deserialize(Map<String, Object> map)
	{
		World world = Bukkit.getWorld((String)map.get(CONFIG_WORLD));
		if(world == null)
		{
			return null;
		}
		return new LinmaluLocation(world, (double)map.get(CONFIG_X), (double)map.get(CONFIG_Y), (double)map.get(CONFIG_Z), (double)map.get(CONFIG_YAW), (double)map.get(CONFIG_PITCH));
	}
}
