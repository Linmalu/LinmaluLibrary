package com.linmalu.library;

import org.bukkit.Bukkit;

import com.linmalu.library.api.LinmaluMD5;
import com.linmalu.library.api.LinmaluMain;
import com.linmalu.library.api.LinmaluPlayer;
import com.linmalu.shortcutkey.LinmaluShortcutkeyChannel;

public class LinmaluLibrary extends LinmaluMain
{
	@Override
	public void onEnable()
	{
		super.onEnable();
		registerEvents(new Main_Event());
		new LinmaluMD5(this);
		Bukkit.getMessenger().registerIncomingPluginChannel(this, LinmaluShortcutkeyChannel.channel, new LinmaluShortcutkeyChannel());
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, LinmaluShortcutkeyChannel.channel);
		Initialization();
	}
	private void Initialization()
	{
		LinmaluPlayer.initialization();
	}
}
