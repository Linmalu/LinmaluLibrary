package com.linmalu.library;

import com.linmalu.library.api.LinmaluBlacklist;
import com.linmalu.library.api.LinmaluEvent;
import com.linmalu.library.api.LinmaluMain;
import com.linmalu.library.api.LinmaluPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRegisterChannelEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.util.ArrayList;
import java.util.List;

public class LinmaluLibraryEvent extends LinmaluEvent
{
	public LinmaluLibraryEvent(LinmaluMain main)
	{
		super(main);
	}

	@EventHandler
	public void event(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		LinmaluBlacklist.check(player);
		player.setMetadata(LinmaluPlayer.LINMALU_CHANNEL, new FixedMetadataValue(_main, new ArrayList<String>()));
	}

	@EventHandler
	@SuppressWarnings("unchecked")
	public void event(PlayerRegisterChannelEvent event)
	{
		for(MetadataValue metadata : event.getPlayer().getMetadata(LinmaluPlayer.LINMALU_CHANNEL))
		{
			if(metadata.getOwningPlugin().toString().equals(_main.toString()))
			{
				((List<String>)metadata.value()).add(event.getChannel());
			}
		}

	}
}
