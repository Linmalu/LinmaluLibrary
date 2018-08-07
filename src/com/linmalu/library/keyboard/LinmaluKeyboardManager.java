package com.linmalu.library.keyboard;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.linmalu.library.api.event.LinmaluKeyboardEvent;

public class LinmaluKeyboardManager
{
	private static LinmaluKeyboardManager linmaluKeyboardManager;

	public static LinmaluKeyboardManager getInstance()
	{
		if(linmaluKeyboardManager == null)
		{
			linmaluKeyboardManager = new LinmaluKeyboardManager();
		}
		return linmaluKeyboardManager;
	}

	private final Map<UUID, Set<LinmaluKeyboard>> players = new HashMap<>();

	private LinmaluKeyboardManager()
	{
	};

	public void LinmaluKeyboardEvent(Player player, LinmaluKeyboard key, boolean down)
	{
		if(key == LinmaluKeyboard.KEY_NONE)
		{
			return;
		}
		if(!players.containsKey(player.getUniqueId()))
		{
			players.put(player.getUniqueId(), new HashSet<>());
		}
		if(down)
		{
			players.get(player.getUniqueId()).add(key);
		}
		else
		{
			players.get(player.getUniqueId()).remove(key);
		}
		Bukkit.getPluginManager().callEvent(new LinmaluKeyboardEvent(player, key, new HashSet<>(players.get(player.getUniqueId()))));
	}

	public void PlayerQuitEvent(UUID uuid)
	{
		if(players.containsKey(uuid))
		{
			players.get(uuid).clear();
			players.remove(uuid);
		}
	}
}
