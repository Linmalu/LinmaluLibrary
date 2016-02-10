package com.linmalu.library;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.linmalu.library.api.LinmaluMD5;
import com.linmalu.library.api.LinmaluVersion;

public class Main_Event implements Listener
{
	@EventHandler
	public void Event(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		if(player.isOp())
		{
			LinmaluVersion.check(LinmaluLibrary.getMain(), player);
			LinmaluMD5.check(LinmaluLibrary.getMain(), player, LinmaluLibrary.getMain().getTitle() + ChatColor.GREEN + "파일이 변조되었습니다.");
		}
	}
}
