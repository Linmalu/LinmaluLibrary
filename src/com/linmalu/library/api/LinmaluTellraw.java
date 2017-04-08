package com.linmalu.library.api;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;

public class LinmaluTellraw
{
	public static void sendChatText(CommandSender sender, String cmd, String msg, String text)
	{
		new LinmaluTellraw("$AT:" + msg + "|" + cmd + "|" + text + "$").changeChatText().sendMessage(sender);
	}
	public static void sendChat(CommandSender sender, String cmd, String msg)
	{
		if(sender instanceof Player)
		{
			sendChatText(sender, cmd, msg, ChatColor.GREEN + "클릭시 채팅에 입력됩니다." + ChatColor.GRAY + " - " + ChatColor.GOLD + cmd);
		}
		else
		{
			sender.sendMessage(msg);
		}
	}
	public static void sendCmdText(CommandSender sender, String cmd, String msg, String text)
	{
		new LinmaluTellraw("$CT:" + msg + "|" + cmd + "|" + text + "$").changeCmdText().sendMessage(sender);
	}
	public static void sendCmd(CommandSender sender, String cmd, String msg)
	{
		sendCmdText(sender, cmd, msg, ChatColor.GREEN + "클릭시 명령어가 입력됩니다." + ChatColor.GRAY + " - " + ChatColor.GOLD + cmd);
	}
	public static void sendItem(CommandSender sender, ItemStack item, String msg)
	{
		new LinmaluTellraw(new LinmaluTellraw().getItem(item, msg, "")).sendMessage(sender);
	}

	private static final String[] items = new String[]{"$ITEM", "$I", "$아이템"};
	private static final String[] texts = new String[]{"$TEXT:", "$T:", "$텍스트:"};
	private static final String[] cmds = new String[]{"$CMD:", "$C:", "$명령어:"};
	private static final String[] itemCmds = new String[]{"$CMDITEM", "$CI", "$명령어아이템"};
	private static final String[] cmdTexts = new String[]{"$CMDTEXT:", "$CT:", "$명령어텍스트:"};
	private static final String[] chatTexts = new String[]{"$CHATTEXT:", "$AT:", "$채팅텍스트:"};

	private boolean change = false;
	private String msg;

	public LinmaluTellraw()
	{
		msg = "";
	}
	public LinmaluTellraw(String msg)
	{
		this.msg = msg;
	}
	public LinmaluTellraw setMessage(String msg)
	{
		this.msg = msg;
		return this;
	}
	public boolean isChange()
	{
		return change;
	}
	public <T extends CommandSender> void sendMessage(List<T> list)
	{
		list.forEach(sender -> sendMessage(sender));
	}
	public <T extends CommandSender> void sendMessage(Set<T> list)
	{
		list.forEach(sender -> sendMessage(sender));
	}
	public void sendMessage(CommandSender ... list)
	{
		Arrays.stream(list).forEach(sender ->
		{
			if(sender instanceof Player)
			{
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + sender.getName() + " {\"text\":\"\",\"extra\":[{\"text\":\"" + msg + "\"}]}");
			}
			else
			{
				sender.sendMessage("{\"text\":\"\",\"extra\":[{\"text\":\"" + msg + "\"}]}");
			}
		});
	}
	public LinmaluTellraw changeItem(Player player)
	{
		for(String cmd : items)
		{
			while(true)
			{
				int i1 = msg.indexOf(cmd);
				int i2 = msg.indexOf("$", i1 + 1);
				if(i1 == -1 || i2 == -1)
				{
					break;
				}
				String sub = msg.substring(i1, i2 + 1);
				boolean find = false;
				for(int i = 1; i < 10; i++)
				{
					if(sub.startsWith(cmd + i))
					{
						find = true;
						String display = sub.replace(cmd + i + ":", "").replace(cmd + i, "").replace("$", "").replace("&", "§");
						msg = msg.replace(sub, getItem(player.getInventory().getItem(i - 1), display, ""));
						break;
					}
				}
				if(!find)
				{
					String display = sub.replace(cmd + ":", "").replace(cmd, "").replace("$", "").replace("&", "§");
					msg = msg.replace(sub, getItem(player.getInventory().getItemInMainHand(), display, ""));
				}
				change = true;
			}
		}
		return this;
	}
	public LinmaluTellraw changeText()
	{
		for(String cmd : texts)
		{
			while(true)
			{
				int i1 = msg.indexOf(cmd);
				int i2 = msg.indexOf("$", i1 + 1);
				if(i1 == -1 || i2 == -1)
				{
					break;
				}
				String sub = msg.substring(i1, i2 + 1);
				msg = msg.replace(sub, getText(sub.replace(cmd, "").replace("$", "").replace("&", "§")));
				change = true;
			}
		}
		return this;
	}
	public LinmaluTellraw changeCmd()
	{
		for(String cmd : cmds)
		{
			while(true)
			{
				int i1 = msg.indexOf(cmd);
				int i2 = msg.indexOf("$", i1 + 1);
				if(i1 == -1 || i2 == -1)
				{
					break;
				}
				String sub = msg.substring(i1, i2 + 1);
				msg = msg.replace(sub, getCmd(sub.replace(cmd, "").replace("$", "").replace("&", "§")));
				change = true;
			}
		}
		return this;
	}
	public LinmaluTellraw changeCmdItem(Player player)
	{
		for(String cmd : itemCmds)
		{
			while(true)
			{
				int i1 = msg.indexOf(cmd);
				int i2 = msg.indexOf("$", i1 + 1);
				if(i1 == -1 || i2 == -1)
				{
					break;
				}
				String sub = msg.substring(i1, i2 + 1);
				boolean find = false;
				String ci = getCmdItem(sub.replace("$", "").replace("&", "§"));
				String display = sub.split("\\|")[0];
				for(int i = 1; i < 10; i++)
				{
					if(sub.startsWith(cmd + i))
					{
						find = true;
						display = display.replace(cmd + i + ":", "").replace(cmd + i, "").replace("$", "").replace("&", "§");
						msg = msg.replace(sub, getItem(player.getInventory().getItem(i - 1), display, ci));
						break;
					}
				}
				if(!find)
				{
					display = display.replace(cmd + ":", "").replace(cmd, "").replace("$", "").replace("&", "§");
					msg = msg.replace(sub, getItem(player.getInventory().getItemInMainHand(), display, ci));
				}
				change = true;
			}
		}
		return this;
	}
	public LinmaluTellraw changeCmdText()
	{
		for(String cmd : cmdTexts)
		{
			while(true)
			{
				int i1 = msg.indexOf(cmd);
				int i2 = msg.indexOf("$", i1 + 1);
				if(i1 == -1 || i2 == -1)
				{
					break;
				}
				String sub = msg.substring(i1, i2 + 1);
				msg = msg.replace(sub, getCmdText(sub.replace(cmd, "").replace("$", "").replace("&", "§")));
				change = true;
			}
		}
		return this;
	}
	public LinmaluTellraw changeChatText()
	{
		for(String cmd : chatTexts)
		{
			while(true)
			{
				int i1 = msg.indexOf(cmd);
				int i2 = msg.indexOf("$", i1 + 1);
				if(i1 == -1 || i2 == -1)
				{
					break;
				}
				String sub = msg.substring(i1, i2 + 1);
				msg = msg.replace(sub, getChatText(sub.replace(cmd, "").replace("$", "").replace("&", "§")));
				change = true;
			}
		}
		return this;
	}
	@SuppressWarnings("deprecation")
	private String getItem(ItemStack item, String msg, String cmd)
	{
		if(item == null || item.getType() == Material.AIR)
		{
			return "\"}, {\"text\":\"" + ChatColor.AQUA + "[맨손]\"" + cmd + "}, {\"text\":\"";
		}
		boolean count = true;
		ItemMeta im = item.getItemMeta();
		StringBuilder name = new StringBuilder();
		StringBuilder ench = new StringBuilder();
		StringBuilder lore = new StringBuilder();
		StringBuilder potion = new StringBuilder();
		String display;
		if(im.hasDisplayName())
		{
			display = im.getDisplayName();
			name.append("Name:\\\"").append(display.replace("\"", "\\\\\\\"")).append("\\\"");
		}
		else
		{
			String itemName = LinmaluLanguage.getTranslateItemStack(item);
			if(itemName == null)
			{
				itemName = item.getType().toString();
			}
			display = ChatColor.AQUA + "[" + itemName + "]";
		}
		count = true;
		if(im.hasLore())
		{
			if(im.hasDisplayName())
			{
				lore.append(", ");
			}
			lore.append("Lore:[");
			for(String s : im.getLore())
			{
				if(count)
				{
					count = false;
				}
				else
				{
					lore.append(", ");
				}
				lore.append("\\\"").append(s.replace("\"", "\\\\\\\"")).append("\\\"");
			}
			lore.append("]");
		}
		count = true;
		if(im.hasEnchants())
		{
			for(Enchantment en : im.getEnchants().keySet())
			{
				if(count)
				{
					count = false;
				}
				else
				{
					ench.append(", ");
				}
				ench.append("{id:" + en.getId() + ", lvl:" + im.getEnchantLevel(en) + "}");
			}
		}
		if(im instanceof EnchantmentStorageMeta)
		{
			EnchantmentStorageMeta esm = (EnchantmentStorageMeta)im;
			for(Enchantment en : esm.getStoredEnchants().keySet())
			{
				if(count)
				{
					count = false;
				}
				else
				{
					ench.append(", ");
				}
				ench.append("{id:" + en.getId() + ", lvl:" + esm.getStoredEnchantLevel(en) + "}");
			}
		}
		count = true;
		if(im instanceof PotionMeta && ((PotionMeta)im).hasCustomEffects())
		{
			potion.append(", CustomPotionEffects:[");
			for(PotionEffect pe : ((PotionMeta)im).getCustomEffects())
			{
				if(count)
				{
					count = false;
				}
				else
				{
					potion.append(", ");
				}
				potion.append("{Id:").append(pe.getType().getId()).append(", Amplifier:").append(pe.getAmplifier()).append(", Duration:").append(pe.getDuration()).append("}");
			}
			potion.append("]");
		}
		if(!msg.equals(""))
		{
			display = msg;
		}
		display = display.replace("\"", "\\\"");
		return "\"}, {\"text\":\"" + display + "\"" + cmd + ", \"hoverEvent\":{\"action\":\"show_item\", \"value\":\"{id:\\\"" + item.getTypeId() + "\\\", Damage:" + item.getDurability() + ", tag:{display:{" + name + lore + "}, ench:["
				+ ench + "]" + potion + "}}\"}}, {\"text\":\"";
	}
	private String getText(String msg)
	{
		String[] msgs = msg.replace("\"", "\\\"").split("\\|");
		String display = msgs[0];
		if(msgs.length > 1)
		{
			msg = ", \"hoverEvent\":{\"action\":\"show_text\", \"value\":{\"text\":\"" + msg.replace("\"", "\\\"").replace(msgs[0] + "|", "") + "\"}}";
		}
		else
		{
			msg = "";
		}
		return "\"}, {\"text\":\"" + display + "\"" + msg + "}, {\"text\":\"";
	}
	private String getCmd(String msg)
	{
		String[] msgs = msg.replace("\"", "\\\"").split("\\|");
		String display = msgs[0];
		if(msgs.length > 1)
		{
			msg = ", \"clickEvent\":{\"action\":\"run_command\", \"value\":\"" + msgs[1] + "\"}";
		}
		else
		{
			msg = "";
		}
		return "\"}, {\"text\":\"" + display + "\"" + msg + "}, {\"text\":\"";
	}
	private String getCmdItem(String msg)
	{
		String[] msgs = msg.replace("\"", "\\\"").split("\\|");
		if(msgs.length > 1)
		{
			msg = ", \"clickEvent\":{\"action\":\"run_command\", \"value\":\"" + msgs[1] + "\"}";
		}
		else
		{
			msg = "";
		}
		return msg;
	}
	private String getCmdText(String msg)
	{
		String[] msgs = msg.replace("\"", "\\\"").split("\\|");
		String display = msgs[0];
		String cmd;
		if(msgs.length > 1)
		{
			cmd = ", \"clickEvent\":{\"action\":\"run_command\", \"value\":\"" + msgs[1] + "\"}";
		}
		else
		{
			cmd = "";
		}
		if(msgs.length > 2)
		{
			msg = ", \"hoverEvent\":{\"action\":\"show_text\", \"value\":{\"text\":\"" + msg.replace("\"", "\\\"").replace(msgs[0] + "|" + msgs[1] + "|", "") + "\"}}";
		}
		else
		{
			msg = "";
		}
		return "\"}, {\"text\":\"" + display + "\"" + cmd + msg + "}, {\"text\":\"";
	}
	private String getChatText(String msg)
	{
		String[] msgs = msg.replace("\"", "\\\"").split("\\|");
		String display = msgs[0];
		String cmd;
		if(msgs.length > 1)
		{
			cmd = ", \"clickEvent\":{\"action\":\"suggest_command\", \"value\":\"" + msgs[1] + "\"}";
		}
		else
		{
			cmd = "";
		}
		if(msgs.length > 2)
		{
			msg = ", \"hoverEvent\":{\"action\":\"show_text\", \"value\":{\"text\":\"" + msg.replace("\"", "\\\"").replace(msgs[0] + "|" + msgs[1] + "|", "") + "\"}}";
		}
		else
		{
			msg = "";
		}
		return "\"}, {\"text\":\"" + display + "\"" + cmd + msg + "}, {\"text\":\"";
	}
}
