package com.linmalu.library.api;

import com.linmalu.library.LinmaluLibrary;
import com.sun.istack.internal.NotNull;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class LinmaluPlayer
{
	/**
	 * 포션 효과 추가하기
	 *
	 * @param player
	 * @param potion
	 * @return
	 */
	public static boolean addPotionEffect(@NotNull Player player, @NotNull PotionEffect potion)
	{
		if(!player.hasPotionEffect(potion.getType()))
		{
			player.addPotionEffect(potion, true);
			return true;
		}
		for(PotionEffect effect : player.getActivePotionEffects())
		{
			if(effect.getType().getName().equals(potion.getType().getName()) && (effect.getAmplifier() < potion.getAmplifier() || (effect.getAmplifier() == potion.getAmplifier() && effect.getDuration() < potion.getDuration())))
			{
				player.addPotionEffect(potion, true);
				return true;
			}
		}
		return false;

	}

	/**
	 * 접속중인 플레이어들 얻기
	 *
	 * @return
	 */
	public static Collection<? extends Player> getOnlinePlayers()
	{
		Object players = Bukkit.getOnlinePlayers();
		if(players instanceof Collection)
		{
			return (Collection<Player>)players;
		}
		return Arrays.asList((Player[])players);
	}

	/**
	 * 플레이어 얻기
	 *
	 * @param name
	 * @return
	 */
	public static Collection<? extends Player> getPlayers(@NotNull String name)
	{
		if(name.equals("@"))
		{
			return getOnlinePlayers();
		}
		List<Player> players = new ArrayList<>();
		Player player = Bukkit.getPlayer(name);
		if(player != null)
		{
			players.add(player);
		}
		return players;
	}

	/**
	 * 리스폰 하기
	 *
	 * @param player
	 */
	public static void Respawn(@NotNull Player player)
	{
		Bukkit.getScheduler().scheduleSyncDelayedTask(LinmaluLibrary.getInstance(), () -> player.spigot().respawn());
	}

	/**
	 * 액션바 메시지 전체플레이어에게 보내기
	 *
	 * @param message
	 */
	public static void SendActionBarMessage(@NotNull String message)
	{
		Bukkit.getOnlinePlayers().forEach(player -> SendActionBarMessage(player, message));
	}

	/**
	 * 액션바 메시지 플레이어에게 보내기
	 *
	 * @param player
	 * @param message
	 */
	public static void SendActionBarMessage(@NotNull Player player, @NotNull String message)
	{
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
	}
}
