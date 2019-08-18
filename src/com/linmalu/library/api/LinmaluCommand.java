package com.linmalu.library.api;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class LinmaluCommand implements CommandExecutor
{
	protected abstract List<String> TabCompleter(CommandSender sender, Command command, String alias, String[] args);

	public LinmaluCommand(LinmaluMain main)
	{
		PluginCommand cmd = main.getCommand(main.getDescription().getName());
		cmd.setExecutor(this);
		cmd.setTabCompleter((sender, command, alias, args) ->
		{
			List<String> list = TabCompleter(sender, command, alias, args);
			if(list == null)
			{
				return null;
			}
			String arg = args[args.length - 1].toLowerCase();
			if(arg.isEmpty())
			{
				Collections.sort(list);
				return list;
			}
			List<String> result = new ArrayList<>();
			for(String s : list)
			{
				if(s.toLowerCase().startsWith(arg))
				{
					result.add(s);
				}
			}
			Collections.sort(result);
			return result;
		});
	}

	protected String joinString(String[] args, int beginIndex)
	{
		StringBuilder sb = new StringBuilder();
		for(int i = beginIndex; i < args.length; i++)
		{
			sb.append(args[i]);
			if(i < args.length - 1)
			{
				sb.append(" ");
			}
		}
		return ChatColor.translateAlternateColorCodes('&', sb.toString());
	}
}
