package com.linmalu.library.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class LinmaluMD5 implements Runnable
{
	public static void check(Plugin plugin, CommandSender sender, String msg)
	{
		new LinmaluMD5(plugin, sender, msg);
	}

	private final Plugin plugin;
	private final CommandSender sender;
	private final String message;

	private LinmaluMD5(Plugin plugin, CommandSender sender, String message)
	{
		this.plugin = plugin;
		this.sender = sender;
		this.message = message;
		new Thread(this).start();
	}
	public void run()
	{
		try(BufferedReader br = new BufferedReader(new InputStreamReader(new URL("http://minecraft.linmalu.com/" + plugin.getDescription().getName() + "/" + plugin.getDescription().getVersion()).openStream())))
		{
			StringBuilder md5 = new StringBuilder();
			for(byte b : MessageDigest.getInstance("MD5").digest(Files.readAllBytes(Paths.get(plugin.getDataFolder() + ".jar"))))
			{
				md5.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
			}
			String msg = br.readLine();
			if(msg != null && !msg.equals(md5.toString()))
			{
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> sender.sendMessage(message));
			}
		}
		catch(Exception e)
		{
		}
	}
}
