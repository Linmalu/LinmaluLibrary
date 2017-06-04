package com.linmalu.library.api;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

public class LinmaluConfig extends YamlConfiguration
{
	private final File file;

	public LinmaluConfig(File file)
	{
		this.file = file;
		reload();
	}
	@Override
	public void set(String path, Object value)
	{
		super.set(path, value);
		save();
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
		getKeys(true).forEach(key -> remove(key));
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
	private void save()
	{
		try
		{
			save(file);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	@Deprecated
	public void setData(String key, Object value)
	{
		set(key, value);
	}
	@Deprecated
	public boolean isData(String key)
	{
		return isSet(key);
	}
	@Deprecated
	public <T> T getData(String key, Class<T> clazz)
	{
		return clazz.cast(get(key));
	}
	@Deprecated
	public <T> T getData(String key, Class<T> clazz, T data)
	{
		try
		{
			return clazz.cast(get(key));
		}
		catch(Exception e)
		{
			return data;
		}
	}
	@Deprecated
	public void removeData(String key)
	{
		set(key, null);
	}
	@Deprecated
	public void clearData()
	{
		getKeys(true).forEach(key -> removeData(key));
	}
	@Deprecated
	public YamlConfiguration getConfig()
	{
		return this;
	}
}
