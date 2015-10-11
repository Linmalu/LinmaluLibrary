package com.linmalu.LinmaluLibrary;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.linmalu.LinmaluLibrary.API.LinmaluCheckMD5;
import com.linmalu.LinmaluLibrary.API.LinmaluCheckVersion;

public class Main_Event implements Listener
{
	private final String title;

	public Main_Event()
	{
		title = ChatColor.AQUA + "[" + LinmaluLibrary.getLinmaluLibrary().getDescription().getName() + "] ";
	}
	@EventHandler
	public void Event(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		if(player.isOp())
		{
			new LinmaluCheckVersion(LinmaluLibrary.getLinmaluLibrary(), player, title + ChatColor.GREEN + "최신버전이 존재합니다.");
			new LinmaluCheckMD5(LinmaluLibrary.getLinmaluLibrary(), player, title + ChatColor.GREEN + "파일이 변조되었습니다.");
		}
	}
}
