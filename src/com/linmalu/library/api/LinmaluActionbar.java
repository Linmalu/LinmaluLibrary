package com.linmalu.library.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.comphenix.packetwrapper.WrapperPlayServerChat;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.EnumWrappers.ChatType;

public class LinmaluActionbar
{
	public static void sendMessage(String message)
	{
		Bukkit.getOnlinePlayers().forEach(player -> sendMessage(player, message));
	}
	public static void sendMessage(Player player, String message)
	{
		WrapperPlayServerChat packet = new WrapperPlayServerChat();
		packet.setChatType(ChatType.GAME_INFO);
		packet.setMessage(WrappedChatComponent.fromText(message));
		packet.sendPacket(player);
	}
}
