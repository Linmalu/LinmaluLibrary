/*
 * Copyright (c) 2015 Hexosse
 *
 * This project is Open Source and distributed under The MIT License (MIT)
 * (http://opensource.org/licenses/MIT)
 *
 * You should have received a copy of the The MIT License along with
 * this project.   If not, see <http://opensource.org/licenses/MIT>.
 */

package com.meowj.langutils.lang.convert;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.linmalu.library.api.LinmaluLanguage;

/**
 * This file is part of LanguageUtils
 * <p>
 * A list of enchantments level.
 *
 * @author <b>hexosse</b> (<a href="https://github.com/hexosse">hexosse on GitHub</a>).
 */
public enum EnumEnchantmentLevel
{
    LEVEL1(1, "enchantment.level.1"),
    LEVEL2(2, "enchantment.level.2"),
    LEVEL3(3, "enchantment.level.3"),
    LEVEL4(4, "enchantment.level.4"),
    LEVEL5(5, "enchantment.level.5"),
    LEVEL6(6, "enchantment.level.6"),
    LEVEL7(7, "enchantment.level.7"),
    LEVEL8(8, "enchantment.level.8"),
    LEVEL9(9, "enchantment.level.9"),
    LEVEL10(10, "enchantment.level.10");

	private static final Map<Integer, EnumEnchantmentLevel> lookup = new HashMap<>();

	static
	{
		EnumSet.allOf(EnumEnchantmentLevel.class).forEach(level -> lookup.put(level.getLevel(), level));
	}

	private int level;
	private String unlocalizedName;

	/**
	 * Create an index of enchantments.
	 */
	EnumEnchantmentLevel(int level, String unlocalizedName)
	{
		this.level = level;
		this.unlocalizedName = unlocalizedName;
	}

	/**
	 * @param level
	 *            Enchantment level.
	 * @return The index of a level.
	 */
	public static String get(int level)
	{
		return lookup.containsKey(level) ? LinmaluLanguage.translateToName(lookup.get(level).unlocalizedName) : null;
	}

	/**
	 * @return Enchantment level
	 */
	public int getLevel()
	{
		return level;
	}

	/**
	 * @return The unlocalized name of the enchantment level
	 */
	public String getUnlocalizedName()
	{
		return unlocalizedName;
	}
}
