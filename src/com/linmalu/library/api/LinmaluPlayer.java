package com.linmalu.library.api;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.comphenix.packetwrapper.WrapperPlayServerAbilities;
import com.comphenix.packetwrapper.WrapperPlayServerExperience;
import com.comphenix.packetwrapper.WrapperPlayServerHeldItemSlot;
import com.comphenix.packetwrapper.WrapperPlayServerPlayerInfo;
import com.comphenix.packetwrapper.WrapperPlayServerRespawn;
import com.comphenix.packetwrapper.WrapperPlayServerUpdateHealth;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.wrappers.EnumWrappers.Difficulty;
import com.comphenix.protocol.wrappers.EnumWrappers.NativeGameMode;
import com.comphenix.protocol.wrappers.EnumWrappers.PlayerInfoAction;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;
import com.google.common.base.Charsets;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.linmalu.library.LinmaluLibrary;

public class LinmaluPlayer implements Runnable
{
	private static HashMap<UUID, LinmaluProfile> profiles = new HashMap<>();

	public static void changeName(Player player, String name)
	{
		changePlayer(player, name, null);
	}
	public static void changeSkin(Player player, String skin)
	{
		changePlayer(player, null, skin);
	}
	@SuppressWarnings("deprecation")
	public static void changePlayer(Player player, String name, String skin)
	{
		UUID uuid = skin == null ? null : Bukkit.getOfflinePlayer(skin).getUniqueId();
		new LinmaluPlayer(uuid, new LinmaluPacket(player, name, uuid));
	}

	private UUID uuid;
	private Runnable runnable;

	private LinmaluPlayer(UUID uuid, Runnable runnable)
	{
		this.uuid = uuid;
		this.runnable = runnable;
		if(!profiles.containsKey(uuid))
		{
			profiles.put(uuid, new LinmaluProfile());
		}
		new Thread(this).start();
	}
	@Override
	public void run()
	{
		if(uuid != null && profiles.get(uuid).isTimeOut())
		{
			try
			{
				URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString().replace("-", "") + "?unsigned=false");
				HttpURLConnection huc = (HttpURLConnection)url.openConnection();
				if(huc.getResponseCode() == HttpURLConnection.HTTP_OK)
				{
					InputStreamReader isr = new InputStreamReader(huc.getInputStream(), Charsets.UTF_8);
					JSONObject json = (JSONObject)new JSONParser().parse(isr);
					JSONArray properties = (JSONArray)json.get("properties");
					for (int i = 0; i < properties.size(); i++)
					{
						JSONObject property = (JSONObject)properties.get(i);
						String name = (String)property.get("name");
						String value = (String)property.get("value");
						String signature = (String)property.get("signature");
						WrappedSignedProperty wsp = new WrappedSignedProperty(name, value, signature);
						profiles.get(uuid).setWrappedSignedProperty(wsp);
					}
				}
				else if(huc.getResponseCode() == 429)
				{
					if(!profiles.get(uuid).isWrappedSignedProperty())
					{
						Thread.sleep(10000);
						profiles.get(uuid).time = 0;
						run();
						return;
					}
					profiles.get(uuid).time = System.currentTimeMillis() - 590000;
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		if(runnable != null)
		{
			Bukkit.getScheduler().scheduleSyncDelayedTask(LinmaluLibrary.getMain(), runnable);
		}
	}
	private static class LinmaluPacket implements Runnable
	{
		private Player player;
		private String name;
		private UUID uuid;

		private LinmaluPacket(Player player, String name, UUID uuid)
		{
			this.player = player;
			this.name = name;
			this.uuid = uuid;
		}
		@Override
		@SuppressWarnings("deprecation")
		public void run()
		{
			if(player != null && player.isOnline())
			{
				WrapperPlayServerPlayerInfo info = new WrapperPlayServerPlayerInfo();
				info.setAction(PlayerInfoAction.ADD_PLAYER);
				WrappedGameProfile profile = name != null ? new WrappedGameProfile(player.getUniqueId(), name) : WrappedGameProfile.fromPlayer(player);
				profile.getProperties().putAll(uuid != null && profiles.get(uuid).isWrappedSignedProperty() ? profiles.get(uuid).getWrappedSignedProperty() : WrappedGameProfile.fromPlayer(player).getProperties());
				info.setData(Arrays.asList(new PlayerInfoData(profile, 0, NativeGameMode.fromBukkit(player.getGameMode()), WrappedChatComponent.fromText(profile.getName()))));
				WrapperPlayServerRespawn respawn = new WrapperPlayServerRespawn();
				respawn.setDifficulty(Difficulty.valueOf(player.getWorld().getDifficulty().toString()));
				respawn.setDimension(player.getWorld().getEnvironment().getId());
				respawn.setGamemode(NativeGameMode.fromBukkit(player.getGameMode()));
				respawn.setLevelType(player.getWorld().getWorldType());
				WrapperPlayServerAbilities abilities = new WrapperPlayServerAbilities();
				abilities.setCanFly(player.getAllowFlight());
				//abilities.setCanInstantlyBuild(false);
				abilities.setFlying(player.isFlying());
				abilities.setFlyingSpeed(player.getFlySpeed() / 2);
				//abilities.setInvulnurable(false);
				abilities.setWalkingSpeed(player.getWalkSpeed() / 2);
				WrapperPlayServerUpdateHealth health = new WrapperPlayServerUpdateHealth();
				health.setFood(player.getFoodLevel());
				health.setFoodSaturation(player.getSaturation());
				health.setHealth((float)player.getHealth());
				WrapperPlayServerExperience exp = new WrapperPlayServerExperience();
				exp.setExperienceBar(player.getExp());
				exp.setLevel(player.getLevel());
				exp.setTotalExperience(player.getTotalExperience());
				WrapperPlayServerHeldItemSlot slot = new WrapperPlayServerHeldItemSlot();
				slot.setSlot(player.getInventory().getHeldItemSlot());
				for(Player p : Bukkit.getOnlinePlayers())
				{
					info.sendPacket(p);
				}
				respawn.sendPacket(player);
				abilities.sendPacket(player);
				health.sendPacket(player);
				exp.sendPacket(player);
				slot.sendPacket(player);
				player.getWorld().refreshChunk(player.getLocation().getChunk().getX(), player.getLocation().getChunk().getZ());
				player.teleport(player);
				player.updateInventory();
				for(PotionEffect pe : player.getActivePotionEffects())
				{
					player.addPotionEffect(pe, true);
				}
				ProtocolLibrary.getProtocolManager().updateEntity(player, ProtocolLibrary.getProtocolManager().getEntityTrackers(player));
			}
		}
	}
	private class LinmaluProfile
	{
		private long time = 0;
		private WrappedSignedProperty wsp;

		public boolean isTimeOut()
		{
			if((System.currentTimeMillis() - time) > 600000)
			{
				time = System.currentTimeMillis();
				return true;
			}
			else
			{
				return false;
			}
		}
		public boolean isWrappedSignedProperty()
		{
			return wsp != null;
		}
		public Multimap<String, WrappedSignedProperty> getWrappedSignedProperty()
		{
			Multimap<String, WrappedSignedProperty> map = HashMultimap.create();
			map.put("textures", wsp == null ? WrappedSignedProperty.fromValues("", "", "") : wsp);
			return map;
		}
		public void setWrappedSignedProperty(WrappedSignedProperty wsp)
		{
			this.wsp = wsp;
			time = System.currentTimeMillis();
		}
	}
}
