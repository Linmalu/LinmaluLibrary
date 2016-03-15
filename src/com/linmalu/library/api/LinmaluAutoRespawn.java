package com.linmalu.library.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.linmalu.library.LinmaluLibrary;

public class LinmaluAutoRespawn implements Runnable
{
	public static void respawn(Player player)
	{
		new LinmaluAutoRespawn(player);
	}

	private final Player player;

	private LinmaluAutoRespawn(Player player)
	{
		this.player = player;
		Bukkit.getScheduler().scheduleSyncDelayedTask(LinmaluLibrary.getMain(), this);
	}
	public void run()
	{
		player.spigot().respawn();
	}
}
