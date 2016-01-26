package com.linmalu.linmalulibrary.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.comphenix.packetwrapper.WrapperPlayServerTitle;
import com.comphenix.protocol.wrappers.EnumWrappers.TitleAction;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

public class LinmaluTitle
{
	private LinmaluTitle(){}
	@Deprecated
	public static void setMessage(String title, String subTitle, int fadeIn, int stay, int fadeOut)
	{
		for(Player player : Bukkit.getOnlinePlayers())
		{
			sendMessage(player, title, subTitle, fadeIn, stay, fadeOut);
		}
	}
	@Deprecated
	public static void setMessage(Player player, String title, String subTitle, int fadeIn, int stay, int fadeOut)
	{
		sendMessage(player, title, subTitle, fadeIn, stay, fadeOut);
	}
	public static void sendMessage(String title, String subTitle, int fadeIn, int stay, int fadeOut)
	{
		for(Player player : Bukkit.getOnlinePlayers())
		{
			sendMessage(player, title, subTitle, fadeIn, stay, fadeOut);
		}
	}
	public static void sendMessage(Player player, String title, String subTitle, int fadeIn, int stay, int fadeOut)
	{
		WrapperPlayServerTitle packet = new WrapperPlayServerTitle();
		packet.setAction(TitleAction.TIMES);
		packet.setFadeIn(fadeIn);
		packet.setStay(stay);
		packet.setFadeOut(fadeOut);
		packet.sendPacket(player);
		packet = new WrapperPlayServerTitle();
		packet.setAction(TitleAction.SUBTITLE);
		packet.setTitle(WrappedChatComponent.fromText(subTitle));
		packet.sendPacket(player);
		packet = new WrapperPlayServerTitle();
		packet.setAction(TitleAction.TITLE);
		packet.setTitle(WrappedChatComponent.fromText(title));
		packet.sendPacket(player);
	}
}
