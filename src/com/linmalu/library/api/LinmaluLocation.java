package com.linmalu.library.api;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LinmaluLocation
{
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
	public static Location toLocation(String msg)
	{
		String[] args = msg.replace(" ", "").split(",");
		return new Location(Bukkit.getWorld(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]), Float.parseFloat(args[4]), Float.parseFloat(args[5]));
	}
}
