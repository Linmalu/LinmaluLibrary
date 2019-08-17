package com.linmalu.library;

import com.linmalu.library.api.LinmaluConfig;
import com.linmalu.library.api.LinmaluMain;

public class LinmaluLibrary extends LinmaluMain
{
	@Override
	public void onEnable()
	{
		super.onEnable();
		new Main_Event(this);
		LinmaluConfig.initialize();
	}

	@Override
	public void onDisable()
	{
		super.onDisable();
		LinmaluConfig.close();
	}
}
