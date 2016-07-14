package com.linmalu.library.api;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.linmalu.library.LinmaluLibrary;

public class LinmaluBlacklist
{
	public static void check(Player player)
	{
		new Thread(() ->
		{
			if(!getJSON(player, player.getUniqueId().toString()))
			{
				getJSON(player, player.getName());
			}
		}).start();
	}
	private static boolean getJSON(Player player, String check)
	{
		try
		{
			URLConnection url = new URL("http://api.mc-blacklist.kr/API/check/" + check).openConnection();
			url.setRequestProperty("User-Agent", "MC-Blacklist-System");
			StringBuilder sb = new StringBuilder();
			try(InputStream in = url.getInputStream())
			{
				byte[] data = new byte[1024];
				int size;
				while((size = in.read(data)) != -1)
				{
					sb.append(new String(data, 0, size));
				}
			}
			JsonObject json = new JsonParser().parse(sb.toString()).getAsJsonObject();
			if(json.get("blacklist").getAsBoolean())
			{
				Bukkit.getScheduler().scheduleSyncDelayedTask(LinmaluLibrary.getMain(), () ->
				{
					player.kickPlayer(ChatColor.AQUA + "[MC-BlackList]\n" + ChatColor.RESET + json.get("reason").getAsString() + "\n" + ChatColor.GRAY + "문의 : http://mc-blacklist.kr/inquire");
					try
					{
						URLConnection url1 = new URL("http://api.mc-blacklist.kr/API/blocklog/" + player.getName() + "/" + player.getUniqueId() + "/" + player.getAddress().getHostName()).openConnection();
						url1.setRequestProperty("User-Agent", "MC-Blacklist-System");
						try(InputStream in1 = url1.getInputStream())
						{
						}
					}
					catch(Exception e)
					{
					}
				});
				return true;
			}
		}
		catch(Exception e)
		{
		}
		return false;
	}
}
