package com.linmalu.library.api;

import com.linmalu.library.LinmaluLibrary;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class LinmaluBlacklist
{
	/**
	 * 블랙리스트 체크
	 */
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
			LinmaluMain main = LinmaluLibrary.getInstance();
			URLConnection url = new URL("http://api.mc-blacklist.kr/API/Check/" + check).openConnection();
			url.setRequestProperty("User-Agent", main.getDescription().getName() + " v" + main.getDescription().getVersion());
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
			JSONObject json = (JSONObject)new JSONParser().parse(sb.toString());
			if(json.containsKey("blacklist") && (boolean)json.get("blacklist"))
			{
				Bukkit.getScheduler().scheduleSyncDelayedTask(main, () -> player.kickPlayer(ChatColor.AQUA + "[MC-BlackList]\n" + ChatColor.RESET + json.get("reason") + "\n" + ChatColor.GRAY + "문의 : http://mc-blacklist.kr/inquire"));
				return true;
			}
			return false;
		}
		catch(Exception e)
		{
			return false;
		}
	}
}
