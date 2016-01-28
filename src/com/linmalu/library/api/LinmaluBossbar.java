package com.linmalu.library.api;

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
import com.linmalu.library.LinmaluLibrary;

public class LinmaluBossbar implements Runnable
{
	private static int[] bossbarIds = {0, 0, 0, 0, 0 ,0};
	private static HashMap<UUID, PlayerInfo> players = new HashMap<>(); 

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
		for(int i = 0; i < bossbarIds.length; i++)
		{
			if(bossbarIds[i] == 0)
			{
				Zombie zombie = player.getWorld().spawn(new Location(player.getWorld(), 0, -100, 0), Zombie.class);
				bossbarIds[i] = zombie.getEntityId();
				zombie.remove();
			}
		}
		health = health / 100 * 300;
		health = health > 300 ? 300 : health <= 0 ? 1 : health;
		PlayerInfo info = players.get(player.getUniqueId());
		Location loc = player.getLocation();
		loc.add(loc.getDirection().multiply(distance));
		Location loc1 = player.getLocation();
		loc1.setYaw(0);
		loc1.setPitch(0);
		loc1.add(loc1.getDirection().multiply(distance));
		Location[] locs = {loc.clone(), LinmaluMath.rotation(loc.clone(), player.getLocation(), 180, 90), LinmaluMath.rotation(loc.clone(), player.getLocation(), 180, 180), LinmaluMath.rotation(loc.clone(), player.getLocation(), 180, 270), LinmaluMath.rotationY(loc1.clone(), player.getLocation(), -LinmaluMath.yawAngle(loc, player.getLocation()) + 90), LinmaluMath.rotationY(loc1.clone(), player.getLocation(), -LinmaluMath.yawAngle(loc, player.getLocation()) + 270)};
		if(info != null && info.equalsWorld(player))
		{
			List<WrappedWatchableObject> datas = Arrays.asList(new WrappedWatchableObject(2, message), new WrappedWatchableObject(6, health));
			for(int i = 0; i < bossbarIds.length; i++)
			{
				WrapperPlayServerEntityTeleport move = new WrapperPlayServerEntityTeleport();
				move.setEntityID(bossbarIds[i]);
				move.setX(locs[i].getX());
				move.setY(locs[i].getY());
				move.setZ(locs[i].getZ());
				move.setYaw(locs[i].getYaw());
				move.setPitch(locs[i].getPitch());
				move.sendPacket(player);
				WrapperPlayServerEntityMetadata update = new WrapperPlayServerEntityMetadata();
				update.setEntityID(bossbarIds[i]);
				update.setMetadata(datas);
				update.sendPacket(player);
			}
		}
		else
		{
			WrappedDataWatcher data = new WrappedDataWatcher();
			data.setObject(0, (byte)0x20);
			data.setObject(2, message);
			data.setObject(6, health);
			for(int i = 0; i < bossbarIds.length; i++)
			{
				WrapperPlayServerSpawnEntityLiving spawn = new WrapperPlayServerSpawnEntityLiving();
				spawn.setEntityID(bossbarIds[i]);
				spawn.setType(EntityType.WITHER);
				spawn.setX(locs[i].getX());
				spawn.setY(locs[i].getY());
				spawn.setZ(locs[i].getZ());
				spawn.setYaw(locs[i].getYaw());
				spawn.setHeadPitch(locs[i].getPitch());
				spawn.setMetadata(data);
				spawn.sendPacket(player);
			}
		}
		taskId = Bukkit.getScheduler().scheduleSyncDelayedTask(LinmaluLibrary.getLinmaluLibrary(), this, 30);
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
			remove.setEntityIds(bossbarIds);
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
