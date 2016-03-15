package com.linmalu.library.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class LinmaluVersion implements Runnable
{
	public static void check(LinmaluMain plugin, CommandSender sender)
	{
		check(plugin, sender, plugin.getTitle() + ChatColor.GREEN + "최신버전이 존재합니다.");
	}
	@Deprecated
	public static void check(Plugin plugin, CommandSender sender, String msg)
	{
		new LinmaluVersion(plugin, sender, msg);
	}

	private final Plugin plugin;
	private final CommandSender sender;
	private final String msg;
	private final String url;
	private final float version;
	private boolean check = true;

	private LinmaluVersion(Plugin plugin, CommandSender sender, String msg)
	{
		this.plugin = plugin;
		this.sender = sender;
		this.msg = msg;
		url = "http://minecraft.linmalu.com/" + plugin.getDescription().getName() + "/version";
		version = Float.parseFloat(plugin.getDescription().getVersion());
		new Thread(this).start();
	}
	public void run()
	{
		if(check)
		{
			try
			{
				BufferedReader br = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
				String msg;
				while((msg = br.readLine()) != null)
				{
					if(version < Float.parseFloat(msg))
					{
						check = false;
						Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, this);
						br.close();
						return;
					}
				}
				br.close();
			}
			catch(Exception e)
			{
			}
		}
		else
		{
			sender.sendMessage(msg);
		}
	}
}
