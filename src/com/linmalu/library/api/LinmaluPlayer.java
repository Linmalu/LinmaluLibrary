package com.linmalu.library.api;

import com.linmalu.library.LinmaluLibrary;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
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
	 */
	public static boolean addPotionEffect(Player player, PotionEffect potion)
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
	 */
	public static Collection<? extends Player> getPlayers(String name)
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
	 */
	public static void Respawn(Player player)
	{
		Bukkit.getScheduler().scheduleSyncDelayedTask(LinmaluLibrary.getInstance(), () -> player.spigot().respawn());
	}

	/**
	 * 액션바 메시지 전체플레이어에게 보내기
	 */
	public static void sendActionBarMessage(String message)
	{
		Bukkit.getOnlinePlayers().forEach(player -> sendActionBarMessage(player, message));
	}

	/**
	 * 액션바 메시지 플레이어에게 보내기
	 */
	public static void sendActionBarMessage(Player player, String message)
	{
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
	}

	/**
	 * 도움말 메시지 보내기
	 */
	public static void sendHelpMessage(CommandSender sender, String title, int size, int page, String... messages)
	{
		if(size < 1)
		{
			size = messages.length < 1 ? 1 : messages.length;
		}
		if(page < 1)
		{
			page = 1;
		}
		int maxPage = messages.length / size;
		if(messages.length % size != 0)
		{
			++maxPage;
		}
		if(page > maxPage)
		{
			page = maxPage;
		}
		sender.sendMessage(ChatColor.AQUA + " - - - - - [ " + title + ChatColor.RESET + " (" + ChatColor.YELLOW + page + ChatColor.RESET + "/" + ChatColor.GOLD + maxPage + ChatColor.RESET + ")" + ChatColor.AQUA + " ] - - - - - ");
		for(String message : messages)
		{
			sender.sendMessage(message);
		}
		sender.sendMessage(ChatColor.YELLOW + "제작자 : " + ChatColor.AQUA + "린마루(Linmalu)" + ChatColor.WHITE + " - http://blog.linmalu.com");
	}
}
