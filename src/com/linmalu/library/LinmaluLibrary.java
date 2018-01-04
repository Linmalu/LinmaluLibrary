package com.linmalu.library;

import org.bukkit.Bukkit;

import com.linmalu.keyboard.LinmaluKeyboardChannel;
import com.linmalu.library.api.LinmaluGlowing;
import com.linmalu.library.api.LinmaluMain;
import com.linmalu.library.api.LinmaluPlayer;

public class LinmaluLibrary extends LinmaluMain
{
	@Override
	public void onEnable()
	{
		super.onEnable();
		registerEvents(new Main_Event());
		Bukkit.getMessenger().registerIncomingPluginChannel(this, LinmaluKeyboardChannel.channel, new LinmaluKeyboardChannel());
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, LinmaluKeyboardChannel.channel);
		LinmaluPlayer.initialization();
		LinmaluGlowing.initialization();
	}
}
