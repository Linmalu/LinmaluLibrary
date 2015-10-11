package com.linmalu.LinmaluLibrary.API;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.comphenix.protocol.wrappers.EnumWrappers.TitleAction;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.linmalu.LinmaluLibrary.PacketWrapper.WrapperPlayServerTitle;

public class LinmaluTitle
{
	public static void setMessage(String title, String subTitle, int fadeIn, int stay, int fadeOut)
	{
		for(Player player : Bukkit.getOnlinePlayers())
		{
			new LinmaluTitle(player, title, subTitle, fadeIn, stay, fadeOut);
		}
	}
	public static void setMessage(Player player, String title, String subTitle, int fadeIn, int stay, int fadeOut)
	{
		new LinmaluTitle(player, title, subTitle, fadeIn, stay, fadeOut);
	}

	private LinmaluTitle(Player player, String title, String subTitle, int fadeIn, int stay, int fadeOut)
	{
		WrapperPlayServerTitle pst = new WrapperPlayServerTitle();
		pst.setAction(TitleAction.TIMES);
		pst.setFadeIn(fadeIn);
		pst.setStay(stay);
		pst.setFadeOut(fadeOut);
		pst.sendPacket(player);
		pst = new WrapperPlayServerTitle();
		pst.setAction(TitleAction.SUBTITLE);
		pst.setTitle(WrappedChatComponent.fromText(subTitle));
		pst.sendPacket(player);
		pst = new WrapperPlayServerTitle();
		pst.setAction(TitleAction.TITLE);
		pst.setTitle(WrappedChatComponent.fromText(title));
		pst.sendPacket(player);
	}
}
