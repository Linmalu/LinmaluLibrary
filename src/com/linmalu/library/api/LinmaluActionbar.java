package com.linmalu.library.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.comphenix.packetwrapper.WrapperPlayServerChat;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

public class LinmaluActionbar
{
	private LinmaluActionbar(){}

	public static void sendMessage(String message)
	{
		for(Player player : Bukkit.getOnlinePlayers())
		{
			sendMessage(player, message);
		}
	}
	public static void sendMessage(Player player, String message)
	{
		WrapperPlayServerChat psc = new WrapperPlayServerChat();
		psc.setPosition((byte)2);
		psc.setMessage(WrappedChatComponent.fromText(message));
		psc.sendPacket(player);
	}
}
