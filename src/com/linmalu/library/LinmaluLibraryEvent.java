package com.linmalu.library;

import com.linmalu.library.api.LinmaluBlacklist;
import com.linmalu.library.api.LinmaluEvent;
import com.linmalu.library.api.LinmaluMain;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class LinmaluLibraryEvent extends LinmaluEvent
{
	public LinmaluLibraryEvent(LinmaluMain main)
	{
		super(main);
	}

	@EventHandler
	public void event(PlayerJoinEvent event)
	{
		LinmaluBlacklist.check(event.getPlayer());
	}
}
