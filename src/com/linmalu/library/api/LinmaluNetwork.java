package com.linmalu.library.api;

import com.linmalu.library.LinmaluLibrary;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

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
		LinmaluNetworkPacket packet = new LinmaluNetworkPacket(message);
		if(packet.getId() != _id)
		{
			return;
		}
		try
		{
			onMessageEvent(packet);
		}
		catch(Exception e)
		{
			Bukkit.getConsoleSender().sendMessage(LinmaluLibrary.getInstance().getTitle() + ChatColor.GOLD + player.getName() + ChatColor.YELLOW + " - Network Message Error");
			player.kickPlayer(LinmaluLibrary.getInstance().getTitle() + ChatColor.YELLOW + "Network Message Error");
			e.printStackTrace();
		}
	}

	protected LinmaluNetworkPacket getLinmaluNetworkPacket(int capacity)
	{
		return new LinmaluNetworkPacket(_id, capacity);
	}

	/**
	 * 플레이어에게 메시지 보내기
	 */
	protected void sendMessage(Player player, LinmaluNetworkPacket packet)
	{
		player.sendPluginMessage(_plugin, _channel, packet.getArray());
	}

	/**
	 * 플레이어에게 온 메시지 이벤트
	 */
	protected abstract void onMessageEvent(LinmaluNetworkPacket packet);
}
