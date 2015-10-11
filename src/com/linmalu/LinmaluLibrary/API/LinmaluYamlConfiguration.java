package com.linmalu.LinmaluLibrary.API;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.logging.Level;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class LinmaluYamlConfiguration extends YamlConfiguration
{
	@Override
	public String saveToString()
	{
		String data = new String();
		boolean first = true;
		for(String s : super.saveToString().split("\\\\u"))
		{
			if(s.length() >= 4 && !first)
			{
				data += (char)Integer.parseInt(s.substring(0, 4), 16);
				if(s.length() >= 5)
				{
					data += s.substring(4);
				}
			}
			else
			{
				data += s;
				first = false;
			}
		}
		return data;
	}
	public static LinmaluYamlConfiguration loadConfiguration(File file)
	{
		Validate.notNull(file, "File cannot be null");
		LinmaluYamlConfiguration config = new LinmaluYamlConfiguration();
		try
		{
			config.load(file);
		}
		catch(FileNotFoundException e)
		{
		}
		catch(IOException | InvalidConfigurationException e)
		{
			Bukkit.getLogger().log(Level.SEVERE, "Cannot load " + file, e);
		}
		return config;
	}
	@Deprecated
	public static LinmaluYamlConfiguration loadConfiguration(InputStream stream)
	{
		Validate.notNull(stream, "Stream cannot be null");
		LinmaluYamlConfiguration config = new LinmaluYamlConfiguration();
		try
		{
			config.load(stream);
		}
		catch(IOException | InvalidConfigurationException e)
		{
			Bukkit.getLogger().log(Level.SEVERE, "Cannot load configuration from stream", e);
		}
		return config;
	}
	public static LinmaluYamlConfiguration loadConfiguration(Reader reader)
	{
		Validate.notNull(reader, "Stream cannot be null");
		LinmaluYamlConfiguration config = new LinmaluYamlConfiguration();
		try
		{
			config.load(reader);
		}
		catch (IOException | InvalidConfigurationException e)
		{
			Bukkit.getLogger().log(Level.SEVERE, "Cannot load configuration from stream", e);
		}
		return config;
	}
}
