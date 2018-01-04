package com.linmalu.library.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.file.Files;
import java.security.MessageDigest;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

@Deprecated // 1.12.2
public class LinmaluMD5 implements Runnable
{
	private final Plugin plugin;
	private final String name;

	public LinmaluMD5(Plugin plugin)
	{
		this.plugin = plugin;
		String[] names = plugin.getClass().getPackage().getName().split("\\.");
		name = names[1] + names[2];
		new Thread(this).start();
	}
	public void run()
	{
		if(!plugin.getDescription().getName().equalsIgnoreCase(name))
		{
			shutdown();
		}
		try
		{
			Field file = JavaPlugin.class.getDeclaredField("file");
			file.setAccessible(true);
			StringBuilder md5 = new StringBuilder();
			for(byte b : MessageDigest.getInstance("MD5").digest(Files.readAllBytes(((File)file.get(plugin)).toPath())))
			{
				md5.append(Integer.toString((b & 0xFF) + 0x100, 16).substring(1));
			}
			try(BufferedReader br = new BufferedReader(new InputStreamReader(new URL("http://minecraft.linmalu.com/minecraft/md5.html?name=" + plugin.getDescription().getName() + "&version=" + plugin.getDescription().getVersion() + "&md5=" + md5.toString()).openStream())))
			{
				String msg = br.readLine();
				if(msg != null && Boolean.parseBoolean(msg))
				{
					shutdown();
				}
			}
			catch(Exception e)
			{
			}
		}
		catch(Exception e)
		{
		}
	}
	private void shutdown()
	{
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () ->
		{
			Bukkit.broadcastMessage(ChatColor.AQUA + "[" + plugin.getDescription().getDescription() + "] " + ChatColor.YELLOW + "플러그인이 변조되어 서버가 종료됩니다.");
			Bukkit.shutdown();
		});
	}
}
