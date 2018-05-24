package com.linmalu.library.api;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class LinmaluConfig extends YamlConfiguration
{
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
	public boolean isLinmaluLocation(String path)
	{
		return isSet(path) && get(path) instanceof LinmaluLocation;
	}
	public LinmaluLocation getLinmaluLocation(String path)
	{
		return getLocation(path, new LinmaluLocation(Bukkit.getWorlds().get(0), 0, 0, 0));
	}
	public LinmaluLocation getLocation(String path, LinmaluLocation def)
	{
		if(isLinmaluLocation(path))
		{
			return (LinmaluLocation)get(path);
		}
		return def;
	}
	public boolean isLinmaluSquareLocation(String path)
	{
		return isSet(path) && get(path) instanceof LinmaluSquareLocation;
	}
	public LinmaluSquareLocation getLinmaluSquareLocation(String path)
	{
		return getLinmaluSquareLocation(path, new LinmaluSquareLocation(Bukkit.getWorlds().get(0), 0, 0, 0, 0, 0, 0));
	}
	public LinmaluSquareLocation getLinmaluSquareLocation(String path, LinmaluSquareLocation def)
	{
		if(isLinmaluSquareLocation(path))
		{
			return (LinmaluSquareLocation)get(path);
		}
		return def;
	}
	public <T> List<T> getListData(String path)
	{
		return getListData(path, new ArrayList<T>());
	}
	@SuppressWarnings("unchecked")
	public <T> List<T> getListData(String path, List<T> def)
	{
		try
		{
			def = (List<T>)getList(path, def);
		}
		catch(Exception e)
		{
		}
		return def;
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
		catch(FileNotFoundException e)
		{
		}
		catch(IOException | InvalidConfigurationException e)
		{
			e.printStackTrace();
		}
	}
	public void save()
	{
		try
		{
			save(file);
		}
		catch(IOException e)
		{
			e.printStackTrace();
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
