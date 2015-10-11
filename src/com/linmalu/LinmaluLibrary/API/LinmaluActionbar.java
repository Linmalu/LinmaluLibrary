package com.linmalu.LinmaluLibrary.API;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.linmalu.LinmaluLibrary.PacketWrapper.WrapperPlayServerChat;

public class LinmaluActionbar
{
	public static void setMessage(String message)
	{
		for(Player player : Bukkit.getOnlinePlayers())
		{
			new LinmaluActionbar(player, message);
		}
	}
	public static void setMessage(Player player, String message)
	{
		new LinmaluActionbar(player, message);
	}

	private LinmaluActionbar(Player player, String message)
	{
		WrapperPlayServerChat psc = new WrapperPlayServerChat();
		psc.setPosition((byte)2);
		psc.setMessage(WrappedChatComponent.fromText(message));
		psc.sendPacket(player);
	}
}
