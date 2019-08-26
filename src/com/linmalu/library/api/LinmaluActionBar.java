package com.linmalu.library.api;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class LinmaluActionBar
{
	/**
	 * 액션바 메시지 전체플레이어에게 보내기
	 */
	public static void sendActionBarMessage(String message)
	{
		LinmaluPlayer.getOnlinePlayers().forEach(player -> sendActionBarMessage(player, message));
	}

	/**
	 * 액션바 메시지 플레이어에게 보내기
	 */
	public static void sendActionBarMessage(Player player, String message)
	{
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
		//			player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder("Title to send").color(ChatColor.AQUA).create());
	}
}
