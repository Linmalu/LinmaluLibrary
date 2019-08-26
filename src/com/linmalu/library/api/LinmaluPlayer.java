package com.linmalu.library.api;

import com.linmalu.library.LinmaluLibrary;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffect;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class LinmaluPlayer
{
	public static final String LINMALU_CHANNEL = "LinmaluChannel";

	/**
	 * 채널 가져오기
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getChannels(Player player)
	{
		LinmaluMain main = LinmaluLibrary.getInstance();
		for(MetadataValue metadata : player.getMetadata(LINMALU_CHANNEL))
		{
			if(metadata.getOwningPlugin().toString().equals(main.toString()))
			{
				return (List<String>)metadata.value();
			}
		}
		return null;
	}

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
	 * 플레이어에게 아이템 주기
	 */
	public static void addItemStack(Player player, ItemStack item)
	{
		player.getInventory().addItem(item).forEach((key, value) -> player.getWorld().dropItem(player.getLocation(), value));
	}

	/**
	 * 접속중인 플레이어들 얻기
	 */
	@SuppressWarnings("unchecked")
	public static Collection<? extends Player> getOnlinePlayers()
	{
		try
		{
			Method method = Bukkit.class.getMethod("getOnlinePlayers");
			Object players = method.invoke(null);
			if(players instanceof Collection)
			{
				return (Collection<Player>)players;
			}
			else
			{
				return Arrays.asList((Player[])players);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 플레이어 얻기
	 */
	@Deprecated
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
	 * 플레이어 얻기
	 */
	public static Collection<? extends Player> getPlayers(CommandSender sender, String name)
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
	 * 도움말 메시지 보내기
	 */
	public static void sendHelpMessage(CommandSender sender, String title, int size, int page, String... messages)
	{
		if(size < 1)
		{
			size = Math.max(messages.length, 1);
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
