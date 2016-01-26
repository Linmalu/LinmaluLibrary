package com.linmalu.library.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.logging.Level;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

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
	@Override
	public void save(File file) throws IOException
	{
		Validate.notNull(file, "File cannot be null");
		Files.createParentDirs(file);
		String data = saveToString();
		Writer writer = new OutputStreamWriter(new FileOutputStream(file), Charsets.UTF_8);
		try
		{
			writer.write(data);
		}
		finally
		{
			writer.close();
		}
	}
	@Override
	public void load(File file) throws FileNotFoundException, IOException, InvalidConfigurationException
	{
		Validate.notNull(file, "File cannot be null");
		FileInputStream stream = new FileInputStream(file);
		load(new InputStreamReader(stream, Charsets.UTF_8));
	}
	@Deprecated
	@Override
	public void load(InputStream stream) throws IOException, InvalidConfigurationException
	{
		Validate.notNull(stream, "Stream cannot be null");
		load(new InputStreamReader(stream, Charsets.UTF_8));
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
