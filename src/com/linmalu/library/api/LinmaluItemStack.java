package com.linmalu.library.api;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class LinmaluItemStack
{
	/**
	 * 아이템이 같은지 확인
	 */
	public static boolean equals(ItemStack item1, ItemStack item2)
	{
		item1 = item1.clone();
		item2 = item2.clone();
		item1.setAmount(1);
		item2.setAmount(1);
		return item1.equals(item2);
	}

	/**
	 * 아이템 이름 가져오기
	 */
	public static String getName(ItemStack item)
	{
		ItemMeta itemMeta = item.getItemMeta();
		if(itemMeta.hasDisplayName())
		{
			return itemMeta.getDisplayName();
		}
		return item.getType().toString();
	}
}
