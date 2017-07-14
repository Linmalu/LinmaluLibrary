package com.linmalu.library.api;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class LinmaluMessage
{
	public static void sendHelpMessage(CommandSender sender, String title, List<String> messages, int size, int page)
	{
		sendHelpMessage(sender, title, messages, ChatColor.RESET, size, page);
	}
	public static void sendHelpMessage(CommandSender sender, String title, List<String> messages, ChatColor color, int size, int page)
	{
		if(size < 1)
		{
			size = messages.size() < 1 ? 1 : messages.size();
		}
		if(page < 1)
		{
			page = 1;
		}
		int maxPage = messages.size() % size == 0 ? messages.size() / size : messages.size() / size + 1;
		if(page > maxPage)
		{
			page = maxPage;
		}
		sender.sendMessage(title);
		if(messages.size() > 0)
		{
			messages.stream().skip(size * (page - 1)).limit(size).forEach(msg -> sender.sendMessage(color + msg));
		}
		sender.sendMessage(ChatColor.AQUA + " - - - - - [ " + ChatColor.YELLOW + page + ChatColor.RESET + " / " + ChatColor.GOLD + maxPage + ChatColor.AQUA + " ] - - - - - ");
	}
}
