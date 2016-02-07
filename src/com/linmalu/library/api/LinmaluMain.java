package com.linmalu.library.api;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class LinmaluMain extends JavaPlugin
{
	private static final HashMap<String, LinmaluMain> mains = new HashMap<>(); 

	public static LinmaluMain getMain()
	{
		try
		{
			for(StackTraceElement e : new Throwable().getStackTrace())
			{
				LinmaluMain main = mains.get(e.getClassName().split("\\.")[2]);
				if(main != null)
				{
					return main;
				}
			}
		}
		catch(Exception e){}
		return null;
	}
	@Override
	public void onLoad()
	{
		mains.put(getClass().getPackage().getName().split("\\.")[2], this);
	}
	public String getTitle()
	{
		return ChatColor.AQUA + "[" + getDescription().getDescription() + "] ";
	}
	public void registerCommand(CommandExecutor command)
	{
		getCommand(getDescription().getName()).setExecutor(command);
	}
	public void registerEvents(Listener event)
	{
		getServer().getPluginManager().registerEvents(event, this);
	}
}
