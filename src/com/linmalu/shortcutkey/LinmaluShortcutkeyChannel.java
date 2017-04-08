package com.linmalu.shortcutkey;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.linmalu.library.api.event.LinmaluShortcutkeyJoinEvent;
import com.linmalu.library.api.event.LinmaluShortcutkeyUseEvent;

public class LinmaluShortcutkeyChannel implements PluginMessageListener
{
	public static final String channel = "LinmaluShortcutkey";

	public void onPluginMessageReceived(String channel, Player player, byte[] message)
	{
		if(LinmaluShortcutkeyChannel.channel.equals(channel))
		{
			if(message.length == 1 && message[0] == 0)
			{
				LinmaluShortcutkeyJoinEvent event = new LinmaluShortcutkeyJoinEvent(player);
				Bukkit.getServer().getPluginManager().callEvent(event);
			}
			else if(message.length > 1 && message[0] == 1)
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
