package com.linmalu.LinmaluLibrary;

import org.bukkit.plugin.java.JavaPlugin;

public class LinmaluLibrary extends JavaPlugin
{
	private static LinmaluLibrary linmaluLibrary;

	public void onEnable()
	{
		linmaluLibrary = this;
		getServer().getPluginManager().registerEvents(new Main_Event(), this);
		getLogger().info("제작 : 린마루");
	}
	public void onDisable()
	{
		getLogger().info("제작 : 린마루");
	}
	public static LinmaluLibrary getLinmaluLibrary()
	{
		return linmaluLibrary;
	}
}
