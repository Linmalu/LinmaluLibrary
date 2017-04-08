package com.linmalu.library;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.linmalu.library.api.LinmaluBlacklist;
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
			new LinmaluMD5(LinmaluLibrary.getMain());
		}
		LinmaluBlacklist.check(player);
	}
	// @EventHandler
	// public void Event(LinmaluShortcutkeyUseEvent event)
	// {
	// LinmaluKeyboardData key = event.getLinmaluKeyboardData();
	// Bukkit.broadcastMessage(LinmaluKeyboard.getLinmaluKeyboard(key.getLinmaluKeyboard().getKeyCode()).toString() + " / " + key.isShift() + " / " + key.isCtrl() + " / " + key.isAlt());
	// }
}
