package com.linmalu.keyboard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.linmalu.library.api.event.LinmaluKeyboardEvent;

public class LinmaluKeyboardChannel implements PluginMessageListener
{
	public static final String channel = "LinmaluKeyboard";

	public void onPluginMessageReceived(String channel, Player player, byte[] message)
	{
		if(LinmaluKeyboardChannel.channel.equals(channel))
		{
			if(message.length > 0 && message[0] == 0)
			{
				byte[] bytes = new byte[message.length - 1];
				System.arraycopy(message, 1, bytes, 0, bytes.length);
				try
				{
					Bukkit.getPluginManager().callEvent(new LinmaluKeyboardEvent(player, LinmaluKeyboardData.toLinmaluKeyboardData(bytes)));
				}
				catch(Exception e)
				{
				}
			}
		}
	}
}
