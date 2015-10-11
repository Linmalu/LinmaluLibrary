package com.linmalu.LinmaluLibrary.API;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.linmalu.LinmaluLibrary.LinmaluLibrary;

public class LinmaluAutoRespawn implements Runnable
{
	@Deprecated
	public static void Respawn(Player player)
	{
		new LinmaluAutoRespawn(player);
		//		WrapperPlayClientClientCommand cc = new WrapperPlayClientClientCommand();
		//		cc.setAction(ClientCommand.PERFORM_RESPAWN);
		//		cc.receivePacket(player);
	}

	private Player player;

	public LinmaluAutoRespawn(Player player)
	{
		this.player = player;
		Bukkit.getScheduler().scheduleSyncDelayedTask(LinmaluLibrary.getLinmaluLibrary(), this);
	}
	public void run()
	{
		player.spigot().respawn();
	}
}
