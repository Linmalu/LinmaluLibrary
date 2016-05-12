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
		try(BufferedReader br = new BufferedReader(new InputStreamReader(new URL("http://minecraft.linmalu.com/" + plugin.getDescription().getName() + "/" + plugin.getDescription().getVersion()).openStream())))
		{
			Field file = JavaPlugin.class.getDeclaredField("file");
			file.setAccessible(true);
			StringBuilder md5 = new StringBuilder();
			for(byte b : MessageDigest.getInstance("MD5").digest(Files.readAllBytes(((File)file.get(plugin)).toPath())))
			{
				md5.append(Integer.toString((b & 0xFF) + 0x100, 16).substring(1));
			}
			String msg = br.readLine();
			if(msg != null && !msg.equalsIgnoreCase(md5.toString()))
			{
				shutdown();
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
