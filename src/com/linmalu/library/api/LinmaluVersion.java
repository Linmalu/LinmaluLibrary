package com.linmalu.library.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@Deprecated // 1.12.2
public class LinmaluVersion
{
	public static void check(LinmaluMain plugin, CommandSender sender)
	{
		new Thread(() ->
		{
			try(BufferedReader br = new BufferedReader(new InputStreamReader(new URL("http://minecraft.linmalu.com/minecraft/version.html?name=" + plugin.getDescription().getName() + "&version=" + plugin.getDescription().getVersion()).openStream())))
			{
				String msg = br.readLine();
				if(msg != null && Boolean.parseBoolean(msg))
				{
					Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> sender.sendMessage(plugin.getTitle() + ChatColor.GREEN + "최신버전이 존재합니다."));
				}
			}
			catch(Exception e)
			{
			}
		}).start();
	}
}
