package com.linmalu.library.api;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class LinmaluLocation extends Location implements ConfigurationSerializable
{
	private static final String WORLD = "world";
	private static final String X = "x";
	private static final String Y = "y";
	private static final String Z = "z";
	private static final String YAW = "yaw";
	private static final String PITCH = "pitch";
	
	public LinmaluLocation(World world, double x, double y, double z)
	{
		super(world, x, y, z);
	}
	public LinmaluLocation(World world, double x, double y, double z, float yaw, float pitch)
	{
		super(world, x, y, z, yaw, pitch);
	}
	
	public static LinmaluLocation getLinmaluLocation(Location loc)
	{
		return new LinmaluLocation(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
	}
	public static LinmaluLocation deserialize(Map<String, Object> map)
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
		return new LinmaluLocation(world, map.containsKey(X) ? (double)map.get(X) : 0, map.containsKey(Y) ? (double)map.get(Y) : 0, map.containsKey(Z) ? (double)map.get(Z) : 0, map.containsKey(YAW) ? (float)(double)map.get(YAW) : 0, map.containsKey(PITCH) ? (float)(double)map.get(PITCH) : 0);
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
	
	@Deprecated
	private double x1, z1, x2, z2;
	
	@Deprecated
	public LinmaluLocation(World world, double x1, double z1, double x2, double z2)
	{
		super(world, 0, 0, 0);
		this.x1 = x1;
		this.z1 = z1;
		this.x2 = x2;
		this.z2 = z2;
		sort();
	}
	@Deprecated
	public void setWorld(World world)
	{
		setWorld(world);
	}
	@Deprecated
	public double getX1()
	{
		return x1;
	}
	@Deprecated
	public void setX1(double x1)
	{
		this.x1 = x1;
		sort();
	}
	@Deprecated
	public double getZ1()
	{
		return z1;
	}
	@Deprecated
	public void setZ1(double z1)
	{
		this.z1 = z1;
		sort();
	}
	@Deprecated
	public double getX2()
	{
		return x2;
	}
	@Deprecated
	public void setX2(double x2)
	{
		this.x2 = x2;
		sort();
	}
	@Deprecated
	public double getZ2()
	{
		return z2;
	}
	@Deprecated
	public void setZ2(double z2)
	{
		this.z2 = z2;
		sort();
	}
	@Deprecated
	private void sort()
	{
		if(x1 > x2)
		{
			double x = x1;
			x1 = x2;
			x2 = x;
		}
		if(z1 > z2)
		{
			double z = z1;
			z1 = z2;
			z2 = z;
		}
	}
	@Deprecated
	public static boolean checkLocation(Location loc)
	{
		try
		{
			toLocation(loc);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	@Deprecated
	public static boolean checkLocation(String msg)
	{
		try
		{
			toLocation(msg);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	@Deprecated
	public static String toLocation(Location loc)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(loc.getWorld().getName());
		sb.append(", ");
		sb.append(loc.getX());
		sb.append(", ");
		sb.append(loc.getY());
		sb.append(", ");
		sb.append(loc.getZ());
		sb.append(", ");
		sb.append(loc.getYaw());
		sb.append(", ");
		sb.append(loc.getPitch());
		return sb.toString();
	}
	@Deprecated
	public static Location toLocation(String msg)
	{
		String[] args = msg.replace(" ", "").split(",");
		return new Location(Bukkit.getWorld(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]), Float.parseFloat(args[4]), Float.parseFloat(args[5]));
	}
}
