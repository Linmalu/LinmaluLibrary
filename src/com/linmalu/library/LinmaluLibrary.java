package com.linmalu.library;

import com.linmalu.library.api.LinmaluMain;

public class LinmaluLibrary extends LinmaluMain
{
	@Override
	public void onEnable()
	{
		registerEvents(new Main_Event());
		getLogger().info("제작 : 린마루(Linmalu)");
	}
	@Override
	public void onDisable()
	{
		getLogger().info("제작 : 린마루(Linmalu)");
	}
}
