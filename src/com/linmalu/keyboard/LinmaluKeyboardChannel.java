package com.linmalu.keyboard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.linmalu.library.api.event.LinmaluShortcutkeyUseEvent;

public class LinmaluKeyboardChannel implements PluginMessageListener
{
	public static final String channel = "LinmaluShortcutkey";

	public void onPluginMessageReceived(String channel, Player player, byte[] message)
	{
		Bukkit.broadcastMessage(channel);
		if(LinmaluKeyboardChannel.channel.equals(channel))
		{
			if(message.length > 1 && message[0] == 0)
			{
				byte[] bytes = new byte[message.length - 1];
				for(int i = 0; i < bytes.length; i++)
				{
					bytes[i] = message[i + 1];
				}
				try
				{
					Bukkit.getPluginManager().callEvent(new LinmaluShortcutkeyUseEvent(player, LinmaluKeyboardData.toLinmaluKeyboardData(bytes)));
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
