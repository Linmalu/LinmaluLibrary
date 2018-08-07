package com.linmalu.library;

import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

import com.linmalu.library.api.LinmaluGlowing;
import com.linmalu.library.api.LinmaluLocation;
import com.linmalu.library.api.LinmaluMain;
import com.linmalu.library.api.LinmaluPlayer;
import com.linmalu.library.api.LinmaluSquareLocation;
import com.linmalu.library.network.LinmaluNetwork;

public class LinmaluLibrary extends LinmaluMain
{
	@Override
	public void onEnable()
	{
		super.onEnable();
		registerEvents(new Main_Event());
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, LinmaluNetwork.CHANNEL);
		Bukkit.getMessenger().registerIncomingPluginChannel(this, LinmaluNetwork.CHANNEL, LinmaluNetwork.getInstance());
		ConfigurationSerialization.registerClass(LinmaluLocation.class);
		ConfigurationSerialization.registerClass(LinmaluSquareLocation.class);
		LinmaluPlayer.initialization();
		LinmaluGlowing.initialization();
	}
}
