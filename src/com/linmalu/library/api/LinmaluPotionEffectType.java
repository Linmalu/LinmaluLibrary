package com.linmalu.library.api;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.potion.PotionEffectType;

@Deprecated
public class LinmaluPotionEffectType
{
	protected static final ArrayList<LinmaluPotionEffectType> list = new ArrayList<>();

	static
	{
		list.add(new LinmaluPotionEffectType(PotionEffectType.SPEED, "신속"));
		list.add(new LinmaluPotionEffectType(PotionEffectType.SLOW, "구속"));
		list.add(new LinmaluPotionEffectType(PotionEffectType.FAST_DIGGING, "성급함"));
		list.add(new LinmaluPotionEffectType(PotionEffectType.SLOW_DIGGING, "피로"));
		list.add(new LinmaluPotionEffectType(PotionEffectType.INCREASE_DAMAGE, "힘"));
		list.add(new LinmaluPotionEffectType(PotionEffectType.HEAL, "즉시회복"));
		list.add(new LinmaluPotionEffectType(PotionEffectType.HARM, "즉시피해"));
		list.add(new LinmaluPotionEffectType(PotionEffectType.JUMP, "점프강화"));
		list.add(new LinmaluPotionEffectType(PotionEffectType.CONFUSION, "멀미"));
		list.add(new LinmaluPotionEffectType(PotionEffectType.REGENERATION, "재생"));
		list.add(new LinmaluPotionEffectType(PotionEffectType.DAMAGE_RESISTANCE, "저항"));
		list.add(new LinmaluPotionEffectType(PotionEffectType.FIRE_RESISTANCE, "화염저항"));
		list.add(new LinmaluPotionEffectType(PotionEffectType.WATER_BREATHING, "수중호흡"));
		list.add(new LinmaluPotionEffectType(PotionEffectType.INVISIBILITY, "투명"));
		list.add(new LinmaluPotionEffectType(PotionEffectType.BLINDNESS, "실명"));
		list.add(new LinmaluPotionEffectType(PotionEffectType.NIGHT_VISION, "야간투시"));
		list.add(new LinmaluPotionEffectType(PotionEffectType.HUNGER, "허기"));
		list.add(new LinmaluPotionEffectType(PotionEffectType.WEAKNESS, "나약함"));
		list.add(new LinmaluPotionEffectType(PotionEffectType.POISON, "독"));
		list.add(new LinmaluPotionEffectType(PotionEffectType.WITHER, "시듦"));
		list.add(new LinmaluPotionEffectType(PotionEffectType.HEALTH_BOOST, "체력강화"));
		list.add(new LinmaluPotionEffectType(PotionEffectType.ABSORPTION, "흡수"));
		list.add(new LinmaluPotionEffectType(PotionEffectType.SATURATION, "포화"));
		list.add(new LinmaluPotionEffectType(PotionEffectType.GLOWING, "발광"));
		list.add(new LinmaluPotionEffectType(PotionEffectType.LEVITATION, "공중부양"));
		list.add(new LinmaluPotionEffectType(PotionEffectType.LUCK, "행운"));
		list.add(new LinmaluPotionEffectType(PotionEffectType.UNLUCK, "불운"));
	}

	public static boolean hasPotionEffectType(String name)
	{
		for(LinmaluPotionEffectType lpet : list)
		{
			if(lpet.name.equals(name))
			{
				return true;
			}
		}
		return false;
	}
	public static PotionEffectType getPotionEffectType(String name)
	{
		for(LinmaluPotionEffectType lpet : list)
		{
			if(lpet.name.equals(name))
			{
				return lpet.type;
			}
		}
		return null;
	}
	public static boolean hasName(PotionEffectType type)
	{
		for(LinmaluPotionEffectType lpet : list)
		{
			if(lpet.type.equals(type))
			{
				return true;
			}
		}
		return false;
	}
	public static String getName(PotionEffectType type)
	{
		for(LinmaluPotionEffectType lpet : list)
		{
			if(lpet.type.equals(type))
			{
				return lpet.name;
			}
		}
		return null;
	}
	public static List<String> getNames()
	{
		return list.stream().map(a -> a.name).collect(Collectors.toList());
	}

	private final PotionEffectType type;
	private final String name;

	protected LinmaluPotionEffectType(PotionEffectType type, String name)
	{
		this.type = type;
		this.name = name;
	}
}
