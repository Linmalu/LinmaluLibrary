package com.linmalu.linmalulibrary.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.linmalu.linmalulibrary.LinmaluLibrary;

public class LinmaluAutoRespawn implements Runnable
{
	public static void respawn(Player player)
	{
		new LinmaluAutoRespawn(player);
	}

	private Player player;

	private LinmaluAutoRespawn(Player player)
	{
		this.player = player;
		Bukkit.getScheduler().scheduleSyncDelayedTask(LinmaluLibrary.getLinmaluLibrary(), this);
	}
	public void run()
	{
		player.spigot().respawn();
	}
}
