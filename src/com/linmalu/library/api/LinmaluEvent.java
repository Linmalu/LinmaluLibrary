package com.linmalu.library.api;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public abstract class LinmaluEvent implements Listener
{
	protected final LinmaluMain _main;

	public LinmaluEvent(LinmaluMain main)
	{
		_main = main;
		main.getServer().getPluginManager().registerEvents(this, main);
	}

	@EventHandler
	public void linmaluEvent(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		if(player.isOp())
		{
			LinmaluServer.version(_main, player);
		}
	}
}
