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
	private final String message;

	private LinmaluVersion(Plugin plugin, CommandSender sender, String message)
	{
		this.plugin = plugin;
		this.sender = sender;
		this.message = message;
		new Thread(this).start();
	}
	public void run()
	{
		try(BufferedReader br = new BufferedReader(new InputStreamReader(new URL("http://minecraft.linmalu.com/" + plugin.getDescription().getName() + "/version").openStream())))
		{
			String msg = br.readLine();
			if(msg !=null && !msg.equals(plugin.getDescription().getVersion()))
			{
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> sender.sendMessage(message));
			}
		}
		catch(Exception e)
		{
		}
	}
}
