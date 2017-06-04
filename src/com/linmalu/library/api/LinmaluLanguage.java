package com.linmalu.library.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;

import com.meowj.langutils.lang.convert.EnumEnchantment;
import com.meowj.langutils.lang.convert.EnumEnchantmentLevel;
import com.meowj.langutils.lang.convert.EnumEntity;
import com.meowj.langutils.lang.convert.EnumItem;
import com.meowj.langutils.lang.convert.EnumPotionEffect;

public class LinmaluLanguage
{
	private static Map<String, String> map = new HashMap<>();

	static
	{
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(LinmaluLanguage.class.getResourceAsStream("/lang/ko_KR.lang"), Charset.forName("UTF-8"))))
		{
			String data;
			while((data = reader.readLine()) != null)
			{
				String[] msg = data.split("=");
				if(msg.length == 2)
				{
					map.put(msg[0], msg[1]);
				}
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	public static String translateToName(String name)
	{
		return map.get(name);
	}

	public static String getTranslateItemStack(ItemStack item)
	{
		if(item.getType() == Material.POTION || item.getType() == Material.SPLASH_POTION || item.getType() == Material.LINGERING_POTION || item.getType() == Material.TIPPED_ARROW)
		{
			return EnumPotionEffect.get(item);
		}
		else if(item.getType() == Material.MONSTER_EGG)
		{
			return EnumEntity.getSpawnEggName(item);
		}
		else if(item.getType() == Material.SKULL_ITEM && item.getDurability() == 3)
		{
			return EnumItem.getPlayerSkullName(item);
		}
		return EnumItem.get(item);
	}

	public static String getTranslateEnchantment(Enchantment enchantment)
	{
		return EnumEnchantment.get(enchantment);
	}

	public static String getTranslateEnchantmentLevel(int level)
	{
		return EnumEnchantmentLevel.get(level);
	}

	public static String getTranslatePotion(PotionType effectType)
	{
		return EnumPotionEffect.get(effectType);
	}
}
