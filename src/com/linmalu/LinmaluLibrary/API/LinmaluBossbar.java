package com.linmalu.linmalulibrary.api;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;

import com.comphenix.packetwrapper.WrapperPlayServerEntityDestroy;
import com.comphenix.packetwrapper.WrapperPlayServerEntityMetadata;
import com.comphenix.packetwrapper.WrapperPlayServerEntityTeleport;
import com.comphenix.packetwrapper.WrapperPlayServerSpawnEntityLiving;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import com.linmalu.linmalulibrary.LinmaluLibrary;

public class LinmaluBossbar implements Runnable
{
	private static int bossbarID1 = 0;
	private static int bossbarID2 = 0;
	private static HashMap<UUID, PlayerInfo> players = new HashMap<>(); 

	@Deprecated
	public static void setMessage(String message)
	{
		for(Player player : Bukkit.getOnlinePlayers())
		{
			sendMessage(player, message, 100);
		}
	}
	@Deprecated
	public static void setMessage(String message, float health)
	{
		for(Player player : Bukkit.getOnlinePlayers())
		{
			sendMessage(player, message, health);
		}
	}
	@Deprecated
	public static void setMessage(Player player, String message)
	{
		sendMessage(player, message, 100);
	}
	@Deprecated
	public static void setMessage(Player player, String message, float health)
	{
		sendMessage(player, message, health);
	}
	public static void sendMessage(String message)
	{
		for(Player player : Bukkit.getOnlinePlayers())
		{
			sendMessage(player, message, 100);
		}
	}
	public static void sendMessage(String message, float health)
	{
		for(Player player : Bukkit.getOnlinePlayers())
		{
			sendMessage(player, message, health);
		}
	}
	public static void sendMessage(Player player, String message)
	{
		sendMessage(player, message, 100);
	}
	public static void sendMessage(Player player, String message, float health)
	{
		new LinmaluBossbar(player, message, health);
	}

	private int taskId;
	private Player player;
	private final int distance = 100;

	private LinmaluBossbar(Player player, String message, float health)
	{
		this.player = player;
		if(bossbarID1 == 0)
		{
			Zombie zombie = player.getWorld().spawn(new Location(player.getWorld(), 0, -100, 0), Zombie.class);
			bossbarID1 = zombie.getEntityId();
			zombie.remove();
		}
		if(bossbarID2 == 0)
		{
			Zombie zombie = player.getWorld().spawn(new Location(player.getWorld(), 0, -100, 0), Zombie.class);
			bossbarID2 = zombie.getEntityId();
			zombie.remove();
		}
		health = health / 100 * 300;
		health = health > 300 ? 300 : health <= 0 ? 1 : health;
		PlayerInfo info = players.get(player.getUniqueId());
		if(info != null && info.equalsWorld(player))
		{
			List<WrappedWatchableObject> datas = Arrays.asList(new WrappedWatchableObject(2, message), new WrappedWatchableObject(6, health));
			WrapperPlayServerEntityMetadata update1 = new WrapperPlayServerEntityMetadata();
			update1.setEntityID(bossbarID1);
			update1.setMetadata(datas);
			update1.sendPacket(player);
			WrapperPlayServerEntityMetadata update2 = new WrapperPlayServerEntityMetadata();
			update2.setEntityID(bossbarID2);
			update2.setMetadata(datas);
			update2.sendPacket(player);
			WrapperPlayServerEntityTeleport move1 = new WrapperPlayServerEntityTeleport();
			move1.setEntityID(bossbarID1);
			Location loc = player.getLocation();
			loc.add(loc.getDirection().multiply(distance));
			move1.setX(loc.getX());
			move1.setY(loc.getY());
			move1.setZ(loc.getZ());
			move1.setYaw(loc.getYaw());
			move1.setPitch(loc.getPitch());
			move1.sendPacket(player);
			WrapperPlayServerEntityTeleport move2 = new WrapperPlayServerEntityTeleport();
			move2.setEntityID(bossbarID2);
			loc = player.getLocation();
			loc.subtract(loc.getDirection().multiply(distance));
			move2.setX(loc.getX());
			move2.setY(loc.getY());
			move2.setZ(loc.getZ());
			move2.setYaw(loc.getYaw());
			move2.setPitch(loc.getPitch());
			move2.sendPacket(player);
		}
		else
		{
			WrappedDataWatcher data = new WrappedDataWatcher();
			data.setObject(0, (byte)0x20);
			data.setObject(2, message);
			data.setObject(6, health);
			WrapperPlayServerSpawnEntityLiving spawn1 = new WrapperPlayServerSpawnEntityLiving();
			spawn1.setEntityID(bossbarID1);
			spawn1.setType(EntityType.WITHER);
			Location loc = player.getLocation();
			loc.add(loc.getDirection().multiply(distance));
			spawn1.setX(loc.getX());
			spawn1.setY(loc.getY());
			spawn1.setZ(loc.getZ());
			spawn1.setYaw(loc.getYaw());
			spawn1.setHeadPitch(loc.getPitch());
			spawn1.setMetadata(data);
			spawn1.sendPacket(player);
			WrapperPlayServerSpawnEntityLiving spawn2 = new WrapperPlayServerSpawnEntityLiving();
			spawn2.setEntityID(bossbarID2);
			spawn2.setType(EntityType.WITHER);
			loc = player.getLocation();
			loc.subtract(loc.getDirection().multiply(distance));
			spawn2.setX(loc.getX());
			spawn2.setY(loc.getY());
			spawn2.setZ(loc.getZ());
			spawn2.setYaw(loc.getYaw());
			spawn2.setHeadPitch(loc.getPitch());
			spawn2.setMetadata(data);
			spawn2.sendPacket(player);
		}
		taskId = Bukkit.getScheduler().scheduleSyncDelayedTask(LinmaluLibrary.getLinmaluLibrary(), this, 20);
		if(info == null)
		{
			players.put(player.getUniqueId(), new PlayerInfo(player, taskId));
		}
		else
		{
			info.resetInfo(player, taskId);
		}
	}
	public void run()
	{
		if(players.containsKey(player.getUniqueId()) && players.get(player.getUniqueId()).isTaskId(taskId))
		{
			WrapperPlayServerEntityDestroy remove = new WrapperPlayServerEntityDestroy();
			remove.setEntityIds(new int[]{bossbarID1, bossbarID2});
			remove.sendPacket(player);
			players.remove(player.getUniqueId());
		}
	}
	private class PlayerInfo
	{
		private String worldName;
		private int taskId;

		public PlayerInfo(Player player, int taskId)
		{
			resetInfo(player, taskId);
		}
		public void resetInfo(Player player, int taskId)
		{
			worldName = player.getWorld().getName();
			this.taskId = taskId;
		}
		public boolean isTaskId(int taskId)
		{
			return this.taskId == taskId;
		}
		public boolean equalsWorld(Player player)
		{
			return worldName.equals(player.getWorld().getName());
		}
	}
}
