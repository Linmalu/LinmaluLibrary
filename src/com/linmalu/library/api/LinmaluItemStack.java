package com.linmalu.library.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.linmalu.library.api.event.LinmaluMaxItemStackSizeEvent;

public class LinmaluItemStack
{
	public static boolean equalsItemStack(ItemStack item1, ItemStack item2)
	{
		if(item1 != null && item2 != null)
		{
			item1 = item1.clone();
			item2 = item2.clone();
			item1.setAmount(1);
			item2.setAmount(1);
			return item1.equals(item2);
		}
		return false;
	}
	public static boolean canAddItemStack(Player player, int size, ItemStack itemStack)
	{
		for(ItemStack item : player.getInventory().getStorageContents())
		{
			if(item == null || item.getType() == Material.AIR || (equalsItemStack(item, itemStack) && item.getAmount() < size))
			{
				return true;
			}
		}
		return false;
	}
	public static List<ItemStack> addItemStack(Player player, ItemStack ... itemStacks)
	{
		List<ItemStack> list = new ArrayList<>();
		if(player != null)
		{
			for(ItemStack item : itemStacks)
			{
				item = item.clone();
				int size = new LinmaluMaxItemStackSizeEvent(player, item).callEvent().getMaxSize();
				List<Integer> indexs = new ArrayList<>();
				ItemStack[] items = player.getInventory().getStorageContents();
				for(int i = 0; i < items.length; i++)
				{
					if(items[i] == null || items[i].getType() == Material.AIR)
					{
						indexs.add(i);
					}
					else if(equalsItemStack(item, items[i]) && items[i].getAmount() < size)
					{
						if(item.getAmount() + items[i].getAmount() > size)
						{
							item.setAmount(item.getAmount() - (size - items[i].getAmount()));
							items[i].setAmount(64);
						}
						else
						{
							items[i].setAmount(item.getAmount() + items[i].getAmount());
							item.setAmount(0);
						}
					}
				}
				for(int index : indexs)
				{
					if(item.getAmount() > 0)
					{
						if(item.getAmount() > size)
						{
							items[index] = item.clone();
							items[index].setAmount(size);
							item.setAmount(item.getAmount() - size);
						}
						else
						{
							items[index] = item.clone();
							item.setAmount(0);
						}
					}
				}
				player.getInventory().setStorageContents(items);
				if(item.getAmount() > 0)
				{
					list.add(item);
				}
			}
		}
		else
		{
			list.addAll(Arrays.asList(itemStacks));
		}
		return list;
	}
}
