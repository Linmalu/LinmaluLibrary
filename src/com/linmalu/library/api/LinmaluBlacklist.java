package com.linmalu.library.api;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import com.sun.istack.internal.NotNull;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.linmalu.library.LinmaluLibrary;

public class LinmaluBlacklist
{
	/**
	 * 블랙리스트 체크
	 *
	 * @param player
	 */
	public static void Check(@NotNull Player player)
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
			URLConnection url = new URL("http://api.mc-blacklist.kr/API/Check/" + check).openConnection();
			url.setRequestProperty("User-Agent", LinmaluLibrary.getInstance().getDescription().getName() + " v" + LinmaluLibrary.getInstance().getDescription().getVersion());
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
				Bukkit.getScheduler().scheduleSyncDelayedTask(LinmaluLibrary.getInstance(), () ->
				{
					player.kickPlayer(ChatColor.AQUA + "[MC-BlackList]\n" + ChatColor.RESET + json.get("reason").getAsString() + "\n" + ChatColor.GRAY + "문의 : http://mc-blacklist.kr/inquire");
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
