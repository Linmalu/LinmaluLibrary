package com.linmalu.LinmaluLibrary.API;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class LinmaluCheckVersion implements Runnable
{
	private boolean check = true;
	private Plugin plugin;
	private UUID uuid;
	private String msg;
	private String url;
	private float version;

	public LinmaluCheckVersion(Plugin plugin, Player player, String msg)
	{
		this.plugin = plugin;
		this.uuid = player.getUniqueId();
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
			Player player = Bukkit.getPlayer(uuid);
			if(player != null)
			{
				player.sendMessage(msg);
			}
		}
	}
}
