package com.linmalu.library.api;

import java.io.BufferedWriter;
import java.io.File;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.util.Scanner;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class LinmaluServer
{
	public static void md5(LinmaluMain plugin)
	{
		boolean result = false;
		String[] names = plugin.getClass().getPackage().getName().split("\\.");
		String name = names[1] + names[2];
		try
		{
			Field field = JavaPlugin.class.getDeclaredField("file");
			field.setAccessible(true);
			StringBuilder md5 = new StringBuilder();
			for(byte b : MessageDigest.getInstance("MD5").digest(Files.readAllBytes(((File)field.get(plugin)).toPath())))
			{
				md5.append(Integer.toString((b & 0xFF) + 0x100, 16).substring(1));
			}
			URLConnection url = new URL("http://minecraft.linmalu.com/minecraft/md5.html").openConnection();
			url.setDoOutput(true);
			try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(url.getOutputStream())))
			{
				bw.write("name=" + plugin.getDescription().getName());
				bw.write("&version=" + plugin.getDescription().getVersion());
				bw.write("&md5=" + md5.toString());
				bw.write("&bukkit=" + Bukkit.getVersion());
				bw.write("&java=" + System.getProperty("java.version"));
				bw.write("&type=json");
				bw.flush();
			}
			try(Scanner sc = new Scanner(url.getInputStream()))
			{
				StringBuilder sb = new StringBuilder();
				while(sc.hasNextLine())
				{
					sb.append(sc.nextLine());
					sb.append("\r\n");
				}
				JsonObject json = new JsonParser().parse(sb.toString()).getAsJsonObject();
				if(json.has("result"))
				{
					result = json.get("result").getAsBoolean();
				}
			}
		}
		catch(Exception e)
		{
		}
		if(result || !plugin.getDescription().getName().equalsIgnoreCase(name))
		{
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () ->
			{
				Bukkit.broadcastMessage(plugin.getTitle() + ChatColor.YELLOW + "플러그인이 변조되어 서버가 종료됩니다.");
				Bukkit.shutdown();
			});
		}
	}
	public static void version(LinmaluMain plugin, CommandSender sender)
	{
		new Thread(() ->
		{
			try
			{
				URLConnection url = new URL("http://minecraft.linmalu.com/minecraft/version.html").openConnection();
				url.setDoOutput(true);
				try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(url.getOutputStream())))
				{
					bw.write("name=" + plugin.getDescription().getName());
					bw.write("&version=" + plugin.getDescription().getVersion());
					bw.write("&bukkit=" + Bukkit.getVersion());
					bw.write("&java=" + System.getProperty("java.version"));
					bw.write("&type=json");
					bw.flush();
				}
				try(Scanner sc = new Scanner(url.getInputStream()))
				{
					StringBuilder sb = new StringBuilder();
					while(sc.hasNextLine())
					{
						if(sb.length() > 0)
						{
							sb.append("\r\n");
						}
						sb.append(sc.nextLine());
					}
					JsonObject json = new JsonParser().parse(sb.toString()).getAsJsonObject();
					if(json.has("result") && json.get("result").getAsBoolean())
					{
						Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> sender.sendMessage(plugin.getTitle() + ChatColor.GREEN + "최신버전이 존재합니다."));
					}
				}
			}
			catch(Exception e)
			{
			}
		}).start();
	}
	public static void report(LinmaluMain plugin, String title, String message)
	{
		new Thread(() ->
		{
			try
			{
				URLConnection url = new URL("http://minecraft.linmalu.com/minecraft/report.html").openConnection();
				url.setDoOutput(true);
				try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(url.getOutputStream())))
				{
					bw.write("name=" + plugin.getDescription().getName());
					bw.write("&version=" + plugin.getDescription().getVersion());
					bw.write("&bukkit=" + Bukkit.getVersion());
					bw.write("&java=" + System.getProperty("java.version"));
					bw.write("&title=" + title);
					bw.write("&message=" + message);
					bw.flush();
				}
			}
			catch(Exception e)
			{
			}
		}).start();
	}
}
