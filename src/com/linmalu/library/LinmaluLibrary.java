package com.linmalu.library;

import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

import com.linmalu.keyboard.LinmaluKeyboardChannel;
import com.linmalu.library.api.LinmaluGlowing;
import com.linmalu.library.api.LinmaluLocation;
import com.linmalu.library.api.LinmaluMain;
import com.linmalu.library.api.LinmaluPlayer;
import com.linmalu.library.api.LinmaluSquareLocation;

public class LinmaluLibrary extends LinmaluMain
{
	@Override
	public void onEnable()
	{
		super.onEnable();
		registerEvents(new Main_Event());
		Bukkit.getMessenger().registerIncomingPluginChannel(this, LinmaluKeyboardChannel.channel, new LinmaluKeyboardChannel());
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, LinmaluKeyboardChannel.channel);
		ConfigurationSerialization.registerClass(LinmaluLocation.class);
		ConfigurationSerialization.registerClass(LinmaluSquareLocation.class);
		LinmaluPlayer.initialization();
		LinmaluGlowing.initialization();
	}
}
