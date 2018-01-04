package com.linmalu.library.api;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

public class LinmaluConfig extends YamlConfiguration
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

	private final File file;
	private boolean autoSave;

	public LinmaluConfig(File file)
	{
		this(file, true);
	}
	public LinmaluConfig(File file, boolean autoSave)
	{
		this.file = file;
		this.autoSave = autoSave;
		reload();
	}
	public boolean isAutoSave()
	{
		return autoSave;
	}
	public void setAutoSave(boolean autoSave)
	{
		this.autoSave = autoSave;
	}
	@Override
	public void set(String path, Object value)
	{
		super.set(path, value);
		if(autoSave)
		{
			save();
		}
	}
	public <T> List<T> getListData(String path)
	{
		return getListData(path, new ArrayList<T>());
	}
	@SuppressWarnings("unchecked")
	public <T> List<T> getListData(String path, List<T> list)
	{
		try
		{
			list = (List<T>)getList(path, list);
		}
		catch(Exception e)
		{
		}
		return list;
	}
	public void remove(String key)
	{
		set(key, null);
	}
	public void clear()
	{
		getKeys(false).iterator().forEachRemaining(this::remove);
	}
	public void reload()
	{
		try
		{
			load(file);
		}
		catch(Exception e)
		{
		}
	}
	public void save()
	{
		try
		{
			save(file);
		}
		catch(Exception e)
		{
		}
	}
}
