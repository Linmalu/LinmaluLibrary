package com.linmalu.LinmaluLibrary.API;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.linmalu.LinmaluLibrary.LinmaluLibrary;

public class LinmaluBossbar implements Runnable
{
	private static int bossbarID = 0;
	private static HashMap<Integer, Integer> schedule = new HashMap<>();

	public static void setMessage(String message)
	{
		for(Player player : Bukkit.getOnlinePlayers())
		{
			new LinmaluBossbar(player, message, 100);
		}
	}
	public static void setMessage(String message, float health)
	{
		for(Player player : Bukkit.getOnlinePlayers())
		{
			new LinmaluBossbar(player, message, health);
		}
	}
	public static void setMessage(Player player, String message)
	{
		new LinmaluBossbar(player, message, 100);
	}
	public static void setMessage(Player player, String message, float health)
	{
		new LinmaluBossbar(player, message, health);
	}

	private Player player;
	private int taskId;

	@SuppressWarnings("deprecation")
	private LinmaluBossbar(Player player, String message, float health)
	{
		this.player = player;
		if(bossbarID == 0)
		{
			Zombie zombie = player.getWorld().spawn(new Location(player.getWorld(), 0, -100, 0), Zombie.class);
			bossbarID = zombie.getEntityId();
			zombie.remove();
		}
		health = health / 100 * 200;
		health = health > 200 ? 200 : health <= 0 ? 1 : health;
		ProtocolManager pm = ProtocolLibrary.getProtocolManager();
		PacketContainer pc = pm.createPacket(PacketType.Play.Server.SPAWN_ENTITY_LIVING);
		StructureModifier<Integer> ints = pc.getIntegers();
		ints.writeDefaults();
		ints.write(0, bossbarID);
		ints.write(1, (int)EntityType.ENDER_DRAGON.getTypeId());
		ints.write(2, player.getLocation().getBlockX() * 32);
		ints.write(3, (player.getLocation().getBlockY() -300) * 32);
		ints.write(4, player.getLocation().getBlockZ() * 32);
		WrappedDataWatcher watcher = new WrappedDataWatcher();
		watcher.setObject(0, (byte)0);
		watcher.setObject(2, message);
		watcher.setObject(6, health);
//		watcher.setObject(10, message);
		pc.getDataWatcherModifier().writeDefaults();
		pc.getDataWatcherModifier().write(0, watcher);
		try
		{
			pm.sendServerPacket(player, pc);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		taskId = Bukkit.getScheduler().scheduleSyncDelayedTask(LinmaluLibrary.getLinmaluLibrary(), this, 20L);
		schedule.put(player.getEntityId(), taskId);
	}
	public void run()
	{
		int id = player.getEntityId();
		if(schedule.containsKey(id) && schedule.get(id) != taskId)
		{
			return;
		}
		ProtocolManager pm = ProtocolLibrary.getProtocolManager();
		PacketContainer pc = pm.createPacket(PacketType.Play.Server.ENTITY_DESTROY);
		pc.getIntegerArrays().write(0, new int[]{bossbarID});
		try
		{
			pm.sendServerPacket(player, pc);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		schedule.remove(id);
	}
}
