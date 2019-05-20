package com.linmalu.library.api;

import com.sun.istack.internal.NotNull;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class LinmaluItemStack
{
	/**
	 * 아이템이 같은지 확인
	 *
	 * @param item1
	 * @param item2
	 * @return
	 */
	public static boolean equalsItemStack(@NotNull ItemStack item1, @NotNull ItemStack item2)
	{
		item1 = item1.clone();
		item2 = item2.clone();
		item1.setAmount(1);
		item2.setAmount(1);
		return item1.equals(item2);
	}
}
