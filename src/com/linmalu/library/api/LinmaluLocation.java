package com.linmalu.library.api;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LinmaluLocation
{
	private World world;
	private double x1, z1, x2, z2;

	public LinmaluLocation(World world, double x1, double z1, double x2, double z2)
	{
		this.world = world;
		this.x1 = x1;
		this.z1 = z1;
		this.x2 = x2;
		this.z2 = z2;
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
	}
	public double getZ1()
	{
		return z1;
	}
	public void setZ1(double z1)
	{
		this.z1 = z1;
	}
	public double getX2()
	{
		return x2;
	}
	public void setX2(double x2)
	{
		this.x2 = x2;
	}
	public double getZ2()
	{
		return z2;
	}
	public void setZ2(double z2)
	{
		this.z2 = z2;
	}
	public void sort()
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
