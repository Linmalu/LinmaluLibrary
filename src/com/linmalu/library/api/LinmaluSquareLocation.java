package com.linmalu.library.api;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class LinmaluSquareLocation implements ConfigurationSerializable
{
	private static String WORLD = "world";
	private static String X1 = "x1";
	private static String Y1 = "y1";
	private static String Z1 = "z1";
	private static String X2 = "x2";
	private static String Y2 = "y2";
	private static String Z2 = "z2";

	private World world;
	private double x1, y1, z1, x2, y2, z2;

	public LinmaluSquareLocation(World world, double x, double y, double z)
	{
		this(world, x, y, z, x, y, z);
	}
	public LinmaluSquareLocation(World world, double x1, double y1, double z1, double x2, double y2, double z2)
	{
		this.world = world;
		this.x1 = x1;
		this.y1 = y1;
		this.z1 = z1;
		this.x2 = x2;
		this.y2 = y2;
		this.z2 = z2;
		sort();
	}
	public World getWorld()
	{
		return world;
	}
	public void setWorld(World world)
	{
		this.world = world;
	}
	public double getX1()
	{
		return x1;
	}
	public void setX1(double x1)
	{
		this.x1 = x1;
		sort();
	}
	public double getY1()
	{
		return y1;
	}
	public void setY1(double y1)
	{
		this.y1 = y1;
		sort();
	}
	public double getZ1()
	{
		return z1;
	}
	public void setZ1(double z1)
	{
		this.z1 = z1;
		sort();
	}
	public double getX2()
	{
		return x2;
	}
	public void setX2(double x2)
	{
		this.x2 = x2;
		sort();
	}
	public double getY2()
	{
		return y2;
	}
	public void setY2(double y2)
	{
		this.y2 = y2;
		sort();
	}
	public double getZ2()
	{
		return z2;
	}
	public void setZ2(double z2)
	{
		this.z2 = z2;
		sort();
	}
	public void setX(double x)
	{
		x1 = x2 = x;
	}
	public void setY(double y)
	{
		y1 = y2 = y;
	}
	public void setZ(double z)
	{
		z1 = z2 = z;
	}
	private void sort()
	{
		if(x1 > x2)
		{
			double x = x1;
			x1 = x2;
			x2 = x;
		}
		if(y1 > y2)
		{
			double y = y1;
			y1 = y2;
			y2 = y;
		}
		if(z1 > z2)
		{
			double z = z1;
			z1 = z2;
			z2 = z;
		}
	}
	@Override
	public Map<String, Object> serialize()
	{
		Map<String, Object> map = new LinkedHashMap<>();
		map.put(WORLD, world.getName());
		map.put(X1, x1);
		map.put(Y1, y1);
		map.put(Z1, z1);
		map.put(X2, x2);
		map.put(Y2, y2);
		map.put(Z2, z2);
		return map;
	}
	public static LinmaluSquareLocation deserialize(Map<String, Object> map)
	{
		World world = null;
		if(map.containsKey(WORLD))
		{
			world = Bukkit.getWorld((String)map.get(WORLD));
		}
		if(world == null)
		{
			world = Bukkit.getWorlds().get(0);
		}
		return new LinmaluSquareLocation(world, map.containsKey(X1) ? (double)map.get(X1) : 0, map.containsKey(Y1) ? (double)map.get(Y1) : 0, map.containsKey(Z1) ? (double)map.get(Z1) : 0, map.containsKey(X2) ? (double)map.get(X2) : 0, map.containsKey(Y1) ? (double)map.get(Y1) : 0, map.containsKey(Z2) ? (double)map.get(Z2) : 0);
	}
}
