package com.linmalu.library;

import com.linmalu.library.api.LinmaluBlacklist;
import com.linmalu.library.api.LinmaluServer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Main_Event implements Listener
{
	@EventHandler
	public void event(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		if(player.isOp())
		{
			LinmaluServer.version(LinmaluLibrary.getInstance(), player);
		}
		LinmaluBlacklist.check(player);
	}
}
