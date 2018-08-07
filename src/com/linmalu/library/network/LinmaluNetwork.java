package com.linmalu.library.network;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.linmalu.library.LinmaluLibrary;
import com.linmalu.library.api.LinmaluPlayer;
import com.linmalu.library.keyboard.LinmaluKeyboard;
import com.linmalu.library.keyboard.LinmaluKeyboardManager;

public class LinmaluNetwork implements PluginMessageListener, Runnable
{
	public static final String CHANNEL = "linmalulibrary";

	private static LinmaluNetwork linmaluNetwork;

	public static LinmaluNetwork getInstance()
	{
		if(linmaluNetwork == null)
		{
			linmaluNetwork = new LinmaluNetwork();
		}
		return linmaluNetwork;
	}

	private final Map<UUID, String> playerVersions = new HashMap<>();
	private final Map<UUID, Integer> playerCounts = new HashMap<>();
	private final Map<UUID, List<ByteBuffer>> playerBuffers = new HashMap<>();

	private LinmaluNetwork()
	{
		LinmaluPlayer.getOnlinePlayers().forEach(player ->
		{
			PlayerJoinEvent(player.getUniqueId());
			sendConnectMessage(player);
		});
		Bukkit.getScheduler().scheduleSyncRepeatingTask(LinmaluLibrary.getMain(), this, 0L, 20L);
	}

	@Override
	public void run()
	{
		for(Iterator<Entry<UUID, Integer>> it = playerCounts.entrySet().iterator(); it.hasNext();)
		{
			Entry<UUID, Integer> entry = it.next();
			Player player = Bukkit.getPlayer(entry.getKey());
			if(player == null || entry.getValue() <= 0)
			{
				it.remove();
				if(playerBuffers.containsKey(entry.getKey()))
				{
					playerBuffers.put(entry.getKey(), null);
				}
			}
			else
			{
				entry.setValue(entry.getValue() - 1);
				sendConnectMessage(player);
			}
		}
	}

	public Map<UUID, String> getPlayers()
	{
		return new HashMap<>(playerVersions);
	}

	public boolean isPlayer(UUID player)
	{
		if(playerVersions.containsKey(player))
		{
			return !playerVersions.get(player).isEmpty();
		}
		return false;
	}

	public void PlayerJoinEvent(UUID player)
	{
		if(!playerVersions.containsKey(player))
		{
			playerVersions.put(player, new String());
		}
	}

	public void PlayerQuitEvent(UUID player)
	{
		playerVersions.remove(player);
		playerCounts.remove(player);
		playerBuffers.remove(player);
	}

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message)
	{
		if(LinmaluNetwork.CHANNEL.equals(channel))
		{
			ByteBuffer buffer = ByteBuffer.wrap(message);
			if(buffer.get() == LinmaluNetworkType.NETWORK_ID)
			{
				switch(LinmaluNetworkType.getLinmaluNetworkType(buffer.getShort()))
				{
					case VERSION_CS_MESSAGE:
						if(!isPlayer(player.getUniqueId()))
						{
							byte[] msg = new byte[buffer.remaining()];
							buffer.get(msg);
							String version = new String(msg, StandardCharsets.UTF_8);
							playerVersions.put(player.getUniqueId(), version);
							playerCounts.remove(player.getUniqueId());
							if(playerBuffers.containsKey(player.getUniqueId()))
							{
								List<ByteBuffer> list = playerBuffers.get(player.getUniqueId());
								if(list != null)
								{
									list.forEach(buf -> sendMessage(player, buf));
									playerBuffers.put(player.getUniqueId(), null);
								}
							}
							Bukkit.getConsoleSender().sendMessage(LinmaluLibrary.getMain().getTitle() + ChatColor.GREEN + "LinmaluLibraryMod : " + ChatColor.GOLD + player.getName() + ChatColor.RESET + " - " + ChatColor.YELLOW + version);
						}
						break;
					case KEYUP_CS_MESSAGE:
						LinmaluKeyboardManager.getInstance().LinmaluKeyboardEvent(player, LinmaluKeyboard.getLinmaluKeyboard(buffer.get()), false);
						break;
					case KEYDOWN_CS_MESSAGE:
						LinmaluKeyboardManager.getInstance().LinmaluKeyboardEvent(player, LinmaluKeyboard.getLinmaluKeyboard(buffer.get()), true);
						break;
					case CREATE_IMAGE_CS_MESSAGE:
						break;
					default:
						break;
				}
			}
		}
	}

	// 클라이언트 연결
	public void sendConnectMessage(Player player)
	{
		if(!isPlayer(player.getUniqueId()))
		{
			ByteBuffer buffer = ByteBuffer.allocate(3);
			buffer.put(LinmaluNetworkType.NETWORK_ID);
			buffer.putShort(LinmaluNetworkType.CONNECT_SC_MESSAGE.ID);
			player.sendPluginMessage(LinmaluLibrary.getMain(), LinmaluNetwork.CHANNEL, buffer.array());
			if(!playerCounts.containsKey(player.getUniqueId()))
			{
				playerCounts.put(player.getUniqueId(), 10);
			}
		}
	}

	// 이미지 파일 생성
	public void sendCreateImageMessage(Player player, int id, byte[] data)
	{
		int index = 0;
		while(index < data.length)
		{
			int len = index + 10240 < data.length ? 10240 : data.length - index;
			ByteBuffer buffer = ByteBuffer.allocate(9 + len);
			buffer.put(LinmaluNetworkType.NETWORK_ID);
			buffer.putShort(LinmaluNetworkType.CREATE_IMAGE_SC_MESSAGE.ID);
			buffer.putShort((short)id);
			buffer.putInt(data.length);
			buffer.put(data, index, len);
			sendMessage(player, buffer);
			index += len;
		}
	}

	// 이미지 파일 삭제
	public void sendDeleteImageMessage(Player player, int id)
	{
		ByteBuffer buffer = ByteBuffer.allocate(7);
		buffer.put(LinmaluNetworkType.NETWORK_ID);
		buffer.putShort(LinmaluNetworkType.DELETE_IMAGE_SC_MESSAGE.ID);
		buffer.putInt(id);
		sendMessage(player, buffer);
	}

	// 이미지 그리기
	public void sendDrawImageMessage(Player player, int id, int image_id, int fade_in, int time, int fade_out, float alpha, float g_x1, float g_y1, float g_x2, float g_y2, float i_x1, float i_y1, float i_x2, float i_y2)
	{
		ByteBuffer buffer = ByteBuffer.allocate(55);
		buffer.put(LinmaluNetworkType.NETWORK_ID);
		buffer.putShort(LinmaluNetworkType.DRAW_IMAGE_SC_MESSAGE.ID);
		buffer.putShort((short)id);
		buffer.putShort((short)image_id);
		buffer.putInt(fade_in);
		buffer.putInt(time);
		buffer.putInt(fade_out);
		buffer.putFloat(alpha);
		buffer.putFloat(g_x1);
		buffer.putFloat(g_y1);
		buffer.putFloat(g_x2);
		buffer.putFloat(g_y2);
		buffer.putFloat(i_x1);
		buffer.putFloat(i_y1);
		buffer.putFloat(i_x2);
		buffer.putFloat(i_y2);
		sendMessage(player, buffer);
	}

	// 이미지 시간 변경
	public void sendTimeImageMessage(Player player, int id, int fade_in, int time, int fade_out, float alpha)
	{
		ByteBuffer buffer = ByteBuffer.allocate(21);
		buffer.put(LinmaluNetworkType.NETWORK_ID);
		buffer.putShort(LinmaluNetworkType.TIME_IMAGE_SC_MESSAGE.ID);
		buffer.putShort((short)id);
		buffer.putInt(fade_in);
		buffer.putInt(time);
		buffer.putInt(fade_out);
		buffer.putFloat(alpha);
		sendMessage(player, buffer);
	}

	// 이미지 위치 변경
	public void sendLocationImageMessage(Player player, int id, float g_x1, float g_y1, float g_x2, float g_y2, float i_x1, float i_y1, float i_x2, float i_y2)
	{
		ByteBuffer buffer = ByteBuffer.allocate(37);
		buffer.put(LinmaluNetworkType.NETWORK_ID);
		buffer.putShort(LinmaluNetworkType.LOCATION_IMAGE_SC_MESSAGE.ID);
		buffer.putShort((short)id);
		buffer.putFloat(g_x1);
		buffer.putFloat(g_y1);
		buffer.putFloat(g_x2);
		buffer.putFloat(g_y2);
		buffer.putFloat(i_x1);
		buffer.putFloat(i_y1);
		buffer.putFloat(i_x2);
		buffer.putFloat(i_y2);
		sendMessage(player, buffer);
	}

	// 이미지 변경
	public void sendChangeImageMessage(Player player, int id, int image_id)
	{
		ByteBuffer buffer = ByteBuffer.allocate(11);
		buffer.put(LinmaluNetworkType.NETWORK_ID);
		buffer.putShort(LinmaluNetworkType.CHANGE_IMAGE_SC_MESSAGE.ID);
		buffer.putShort((short)id);
		buffer.putShort((short)image_id);
		sendMessage(player, buffer);
	}

	// 텍스트 그리기
	public void sendDrawTextMessage(Player player, int id, int fade_in, int time, int fade_out, float alpha, float g_x, float g_y, float s_x, float s_y, int color, boolean shadow, String message)
	{
		byte[] msg = message.getBytes(StandardCharsets.UTF_8);
		ByteBuffer buffer = ByteBuffer.allocate(42 + msg.length);
		buffer.put(LinmaluNetworkType.NETWORK_ID);
		buffer.putShort(LinmaluNetworkType.DRAW_TEXT_SC_MESSAGE.ID);
		buffer.putShort((short)id);
		buffer.putInt(fade_in);
		buffer.putInt(time);
		buffer.putInt(fade_out);
		buffer.putFloat(alpha);
		buffer.putFloat(g_x);
		buffer.putFloat(g_y);
		buffer.putFloat(s_x);
		buffer.putFloat(s_y);
		buffer.putInt(color);
		buffer.put(shadow ? (byte)1 : (byte)0);
		buffer.put(msg);
		sendMessage(player, buffer);
	}

	// 텍스트 시간 변경
	public void sendTimeTextMessage(Player player, int id, int fade_in, int time, int fade_out, float alpha)
	{
		ByteBuffer buffer = ByteBuffer.allocate(21);
		buffer.put(LinmaluNetworkType.NETWORK_ID);
		buffer.putShort(LinmaluNetworkType.TIME_TEXT_SC_MESSAGE.ID);
		buffer.putShort((short)id);
		buffer.putInt(fade_in);
		buffer.putInt(time);
		buffer.putInt(fade_out);
		buffer.putFloat(alpha);
		sendMessage(player, buffer);
	}

	// 텍스트 위치 변경
	public void sendLocationTextMessage(Player player, int id, float g_x, float g_y, float s_x, float s_y)
	{
		ByteBuffer buffer = ByteBuffer.allocate(21);
		buffer.put(LinmaluNetworkType.NETWORK_ID);
		buffer.putShort(LinmaluNetworkType.LOCATION_TEXT_SC_MESSAGE.ID);
		buffer.putShort((short)id);
		buffer.putFloat(g_x);
		buffer.putFloat(g_y);
		buffer.putFloat(s_x);
		buffer.putFloat(s_y);
		sendMessage(player, buffer);
	}

	// 텍스트 변경
	public void sendChangeTextMessage(Player player, int id, String message)
	{
		byte[] msg = message.getBytes(StandardCharsets.UTF_8);
		ByteBuffer buffer = ByteBuffer.allocate(3 + msg.length);
		buffer.put(LinmaluNetworkType.NETWORK_ID);
		buffer.putShort(LinmaluNetworkType.CHANGE_TEXT_SC_MESSAGE.ID);
		buffer.put(msg);
		sendMessage(player, buffer);
	}

	// 텍스트 색상 변경
	public void sendColorTextMessage(Player player, int id, int color, boolean shadow)
	{
		ByteBuffer buffer = ByteBuffer.allocate(10);
		buffer.put(LinmaluNetworkType.NETWORK_ID);
		buffer.putShort(LinmaluNetworkType.COLOR_TEXT_SC_MESSAGE.ID);
		buffer.putShort((short)id);
		buffer.putInt(color);
		buffer.put(shadow ? (byte)1 : (byte)0);
		sendMessage(player, buffer);
	}

	// 그리기 취소
	public void sendEraseObjectMessage(Player player, int id)
	{
		ByteBuffer buffer = ByteBuffer.allocate(5);
		buffer.put(LinmaluNetworkType.NETWORK_ID);
		buffer.putShort(LinmaluNetworkType.ERASE_OBJECT_SC_MESSAGE.ID);
		buffer.putShort((short)id);
		sendMessage(player, buffer);
	}

	// 모든 그리기 취소
	public void sendClearObjectMessage(Player player)
	{
		ByteBuffer buffer = ByteBuffer.allocate(3);
		buffer.put(LinmaluNetworkType.NETWORK_ID);
		buffer.putShort(LinmaluNetworkType.CLEAR_OBJECT_SC_MESSAGE.ID);
		sendMessage(player, buffer);
	}

	// 랜더링 지우기
	public void sendEraseRenderMessage(Player player, LinmaluRenderType type)
	{
		ByteBuffer buffer = ByteBuffer.allocate(5);
		buffer.put(LinmaluNetworkType.NETWORK_ID);
		buffer.putShort(LinmaluNetworkType.ERASE_RENDER_SC_MESSAGE.ID);
		buffer.putShort(type.ID);
		sendMessage(player, buffer);
	}

	// 랜더링 그리기
	public void sendDrawRenderMessage(Player player, LinmaluRenderType type)
	{
		ByteBuffer buffer = ByteBuffer.allocate(5);
		buffer.put(LinmaluNetworkType.NETWORK_ID);
		buffer.putShort(LinmaluNetworkType.DRAW_RENDER_SC_MESSAGE.ID);
		buffer.putShort(type.ID);
		sendMessage(player, buffer);
	}

	// 랜더링 초기화
	public void sendResetRenderMessage(Player player)
	{
		ByteBuffer buffer = ByteBuffer.allocate(3);
		buffer.put(LinmaluNetworkType.NETWORK_ID);
		buffer.putShort(LinmaluNetworkType.RESET_RENDER_SC_MESSAGE.ID);
		sendMessage(player, buffer);
	}

	// 메세지 보내기
	public void sendMessage(Player player, ByteBuffer buffer)
	{
		if(isPlayer(player.getUniqueId()))
		{
			player.sendPluginMessage(LinmaluLibrary.getMain(), LinmaluNetwork.CHANNEL, buffer.array());
		}
		else
		{
			if(!playerBuffers.containsKey(player.getUniqueId()))
			{
				playerBuffers.put(player.getUniqueId(), new ArrayList<>());
			}
			List<ByteBuffer> list = playerBuffers.get(player.getUniqueId());
			if(list != null)
			{
				list.add(buffer);
			}
		}
	}
}
