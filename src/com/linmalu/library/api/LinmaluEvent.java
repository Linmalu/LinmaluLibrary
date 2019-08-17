package com.linmalu.library.api;

import org.bukkit.event.Listener;

public abstract class LinmaluEvent implements Listener
{
	public LinmaluEvent(LinmaluMain main)
	{
		main.getServer().getPluginManager().registerEvents(this, main);
	}
}
