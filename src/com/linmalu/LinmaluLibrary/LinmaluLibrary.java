package com.linmalu.LinmaluLibrary;

import org.bukkit.plugin.java.JavaPlugin;

public class LinmaluLibrary extends JavaPlugin
{
	private static LinmaluLibrary linmaluLibrary;

	public void onEnable()
	{
		linmaluLibrary = this;
		getServer().getPluginManager().registerEvents(new Main_Event(), this);
		getLogger().info("���� : ������");
	}
	public void onDisable()
	{
		getLogger().info("���� : ������");
	}
	public static LinmaluLibrary getLinmaluLibrary()
	{
		return linmaluLibrary;
	}
}
