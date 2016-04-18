package com.linmalu.library.api;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public enum LinmaluItemStack
{
	TYPE, AMOUNT, DURABILITY, NAME, LORE;

	public static boolean equalsItemStack(ItemStack item1, ItemStack item2, LinmaluItemStack ... types)
	{
		Map<LinmaluItemStack, Boolean> map = new HashMap<>();
		map.put(TYPE, item1.getType() == item2.getType());
		map.put(AMOUNT, item1.getAmount() == item2.getAmount());
		map.put(DURABILITY, item1.getDurability() == item2.getDurability());
		boolean checkName = false;
		boolean checkLore = false;
		if(item1.hasItemMeta() == item2.hasItemMeta())
		{
			if(item1.hasItemMeta())
			{
				ItemMeta im1 = item1.getItemMeta();
				ItemMeta im2 = item2.getItemMeta();
				if(im1.hasDisplayName() == im2.hasDisplayName())
				{
					if(im1.hasDisplayName())
					{
						checkName = im1.getDisplayName().equals(im2.getDisplayName());
					}
					else
					{
						checkName = true;
					}
				}
				if(im1.hasLore() == im2.hasLore())
				{
					if(im1.hasLore())
					{
						List<String> list1 = im1.getLore();
						List<String> list2 = im2.getLore();
						if(list1.size() == list2.size())
						{
							checkLore = true;
							for(int i = 0; i < list1.size(); i++)
							{
								if(!list1.get(i).equals(list2.get(i)))
								{
									checkLore = false;
									break;
								}
							}
						}
					}
					else
					{
						checkLore = true;
					}
				}
			}
			else
			{
				checkName = true;
				checkLore = true;
			}
		}
		map.put(NAME, checkName);
		map.put(LORE, checkLore);
		for(LinmaluItemStack type : types)
		{
			if(!map.get(type))
			{
				return false;
			}
		}
		return true;
	}
	public static ItemStack getItemStack(Material type, int amount, int damage, boolean hideFlag, String name, String ... lore)
	{
		ItemStack item = new ItemStack(type, amount, (short)damage);
		ItemMeta im = item.getItemMeta();
		if(hideFlag)
		{
			im.addItemFlags(ItemFlag.values());
		}
		im.setDisplayName(name);
		if(!(lore.length == 1 && lore[0].equals("")))
		{
			im.setLore(Arrays.asList(lore));
		}
		item.setItemMeta(im);
		return item;
	}
}
