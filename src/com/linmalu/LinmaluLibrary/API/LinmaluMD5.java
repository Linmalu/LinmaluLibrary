package com.linmalu.linmalulibrary.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
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

	private boolean check = true;
	private Plugin plugin;
	private CommandSender sender;
	private String msg;
	private File file;
	private String url;

	private LinmaluMD5(Plugin plugin, CommandSender sender, String msg)
	{
		this.plugin = plugin;
		this.sender = sender;
		this.msg = msg;
		file = new File(plugin.getDataFolder() + ".jar");
		url = "http://minecraft.linmalu.com/" + plugin.getDescription().getName() + "/" + plugin.getDescription().getVersion();
		new Thread(this).start();
	}
	public void run()
	{
		if(check)
		{
			try
			{
				MessageDigest md = MessageDigest.getInstance("MD5");
				byte[] b = new byte[1024];
				FileInputStream fis = new FileInputStream(file);
				int read; 
				while ((read = fis.read(b)) != -1)
				{
					md.update(b, 0, read);
				}
				fis.close();
				b = md.digest();
				StringBuilder md5 = new StringBuilder();
				for (int i = 0; i < b.length; i++)
				{
					md5.append(Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1));
				}
				BufferedReader br = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
				String msg;
				while((msg = br.readLine()) != null)
				{
					if(!msg.equals(md5.toString()))
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
