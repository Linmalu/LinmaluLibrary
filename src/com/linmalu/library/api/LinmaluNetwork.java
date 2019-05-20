package com.linmalu.library.api;

import com.linmalu.library.LinmaluLibrary;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.nio.ByteBuffer;

public abstract class LinmaluNetwork implements PluginMessageListener
{
	private final LinmaluMain _plugin;
	private final String _channel;
	private final byte _id;

	public LinmaluNetwork(LinmaluMain plugin, String channel, byte id)
	{
		_plugin = plugin;
		_channel = channel;
		_id = id;
		Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, channel);
		Bukkit.getMessenger().registerIncomingPluginChannel(plugin, channel, this);
	}

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message)
	{
		if(message[0] != _id)
		{
			return;
		}
		try
		{
			onMessageEvent(ByteBuffer.wrap(message, 1, message.length));
		}
		catch(Exception e)
		{
			Bukkit.getConsoleSender().sendMessage(LinmaluLibrary.getInstance().getTitle() + ChatColor.GOLD + player.getName() + ChatColor.YELLOW + " - Network Message Error");
			player.kickPlayer(LinmaluLibrary.getInstance().getTitle() + ChatColor.YELLOW + "Network Message Error");
			e.printStackTrace();
		}
	}

	/**
	 * 플레이어에게 메시지 보내기
	 */
	protected void sendMessage(Player player, byte[] message)
	{
		ByteBuffer buffer = ByteBuffer.allocate(1 + message.length);
		buffer.put(_id);
		buffer.put(message);
		player.sendPluginMessage(_plugin, _channel, buffer.array());
	}

	/**
	 * 플레이어에게 온 메시지 이벤트
	 */
	protected abstract void onMessageEvent(ByteBuffer message);
}
