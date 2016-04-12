package com.linmalu.library.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.linmalu.library.LinmaluLibrary;

public class LinmaluAutoRespawn
{
	public static void respawn(Player player)
	{
		new LinmaluAutoRespawn(player);
	}

	private LinmaluAutoRespawn(Player player)
	{
		Bukkit.getScheduler().scheduleSyncDelayedTask(LinmaluLibrary.getMain(), () -> player.spigot().respawn());
	}
}
