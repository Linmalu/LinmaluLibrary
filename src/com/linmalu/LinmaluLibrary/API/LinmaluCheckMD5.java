package com.linmalu.LinmaluLibrary.API;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.MessageDigest;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class LinmaluCheckMD5 implements Runnable
{
	private boolean check = true;
	private Plugin plugin;
	private UUID uuid;
	private String msg;
	private File file;
	private String url;

	public LinmaluCheckMD5(Plugin plugin, Player player, String msg)
	{
		this.plugin = plugin;
		this.uuid = player.getUniqueId();
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
			Player player = Bukkit.getPlayer(uuid);
			if(player != null)
			{
				player.sendMessage(msg);
			}
		}
	}
}
