package com.linmalu.library.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;

import com.comphenix.packetwrapper.WrapperPlayServerEntityMetadata;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.Registry;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.WrappedDataWatcherObject;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import com.linmalu.library.LinmaluLibrary;

public class LinmaluGlowing
{
	private static Map<UUID, List<UUID>> players = new HashMap<>();

	public static void initialization()
	{
		ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(LinmaluLibrary.getMain(), PacketType.Play.Server.ENTITY_METADATA)
		{
			@Override
			public void onPacketSending(PacketEvent event)
			{
				if(event.getPacketType() == PacketType.Play.Server.ENTITY_METADATA)
				{
					WrapperPlayServerEntityMetadata packet = new WrapperPlayServerEntityMetadata(event.getPacket());
					if(isGlowing(event.getPlayer().getUniqueId(), packet.getEntity(event).getUniqueId()))
					{
						boolean find = false;
						for(WrappedWatchableObject wwo : packet.getMetadata())
						{
							if(wwo.getIndex() == 0)
							{
								find = true;
								if(((byte)wwo.getRawValue() & 0x40) != 0x40)
								{
									Bukkit.getScheduler().scheduleSyncDelayedTask(LinmaluLibrary.getMain(), () ->
									{
										WrapperPlayServerEntityMetadata p = new WrapperPlayServerEntityMetadata();
										p.setEntityID(packet.getEntity(event).getEntityId());
										packet.setMetadata(new ArrayList<>(Arrays.asList(new WrappedWatchableObject(new WrappedDataWatcherObject(0, Registry.get(Byte.class)), (byte)((byte)wwo.getRawValue() | 0x40)))));
										p.sendPacket(event.getPlayer());
									});
								}
							}
						}
						if(!find)
						{
							Byte b = WrappedDataWatcher.getEntityWatcher(packet.getEntity(event)).getByte(0);
							if(b != null)
							{
								ArrayList<WrappedWatchableObject> list = new ArrayList<>(packet.getMetadata());
								list.add(new WrappedWatchableObject(new WrappedDataWatcherObject(0, Registry.get(Byte.class)), (byte)(b | 0x40)));
								packet.setMetadata(list);
							}
						}
					}
					else if(!(packet.getEntity(event) instanceof LivingEntity && ((LivingEntity)packet.getEntity(event)).hasPotionEffect(PotionEffectType.GLOWING)))
					{
						boolean find = false;
						for(WrappedWatchableObject wwo : packet.getMetadata())
						{
							if(wwo.getIndex() == 0)
							{
								find = true;
								wwo.setValue((byte)((byte)wwo.getRawValue() & 0xBF));
							}
						}
						if(!find)
						{
							Byte b = WrappedDataWatcher.getEntityWatcher(packet.getEntity(event)).getByte(0);
							if(b != null)
							{
								ArrayList<WrappedWatchableObject> list = new ArrayList<>(packet.getMetadata());
								list.add(new WrappedWatchableObject(new WrappedDataWatcherObject(0, Registry.get(Byte.class)), b));
								packet.setMetadata(list);
							}
						}
					}
				}
			}
		});
		Bukkit.getWorlds().forEach(world -> world.getEntities().forEach(entity -> setGlowing(entity.getUniqueId(), false)));
	}
	public static boolean isGlowing(UUID player, UUID target)
	{
		synchronized(players)
		{
			return players.containsKey(player) && players.get(player).contains(target);
		}
	}
	public static void setGlowing(UUID target, boolean glowing)
	{
		Bukkit.getOnlinePlayers().forEach(player -> setGlowing(player.getUniqueId(), target, glowing, null));
	}
	public static void setGlowing(UUID player, UUID target, boolean glowing)
	{
		setGlowing(player, target, glowing, null);
	}
	public static void setGlowing(UUID target, boolean glowing, ChatColor color)
	{
		Bukkit.getOnlinePlayers().forEach(player -> setGlowing(player.getUniqueId(), target, glowing, color));
	}
	public static void setGlowing(UUID player, UUID target, boolean glowing, ChatColor color)
	{
		synchronized(players)
		{
			if(!players.containsKey(player))
			{
				players.put(player, new ArrayList<>());
			}
			Entity entity = Bukkit.getEntity(target);
			if(entity != null)
			{
				if(glowing)
				{
					if(!isGlowing(player, target))
					{
						players.get(player).add(target);
					}
				}
				else
				{
					players.get(player).remove(target);
					if(players.get(player).isEmpty())
					{
						players.remove(player);
					}
				}
				if(color != null)
				{
					String name = "Linmalu" + color.name();
					Team team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(name);
					if(team == null)
					{
						team = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam(name);
						team.setCanSeeFriendlyInvisibles(false);
						team.setPrefix(color.toString());
					}
					if(entity.getType() == EntityType.PLAYER)
					{
						team.addEntry(((Player)entity).getName());
					}
					else
					{
						team.addEntry(target.toString());
					}
				}
				Player p = Bukkit.getPlayer(player);
				if(p != null)
				{
					WrapperPlayServerEntityMetadata packet = new WrapperPlayServerEntityMetadata();
					packet.setEntityID(entity.getEntityId());
					packet.sendPacket(p);
				}
			}
		}
	}
	public static void clear()
	{
		synchronized(players)
		{
			new HashSet<>(players.keySet()).forEach(LinmaluGlowing::clear);
		}
	}
	public static void clear(UUID player)
	{
		synchronized(players)
		{
			Player p = Bukkit.getPlayer(player);
			if(p != null)
			{
				new ArrayList<>(players.get(player)).stream().forEach(entity -> setGlowing(player, entity, false));
			}
			players.remove(player);
		}
	}
}
