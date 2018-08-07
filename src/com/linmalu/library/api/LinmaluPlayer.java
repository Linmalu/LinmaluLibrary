package com.linmalu.library.api;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.potion.PotionEffect;

import com.comphenix.packetwrapper.WrapperPlayServerAbilities;
import com.comphenix.packetwrapper.WrapperPlayServerExperience;
import com.comphenix.packetwrapper.WrapperPlayServerHeldItemSlot;
import com.comphenix.packetwrapper.WrapperPlayServerPlayerInfo;
import com.comphenix.packetwrapper.WrapperPlayServerRespawn;
import com.comphenix.packetwrapper.WrapperPlayServerUpdateHealth;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
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
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.linmalu.library.LinmaluLibrary;

public class LinmaluPlayer implements Runnable
{
	private static HashMap<UUID, LinmaluPlayer> players = new HashMap<>();
	private static HashMap<UUID, LinmaluSkin> skins = new HashMap<>();

	public static void initialization()
	{
		ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(LinmaluLibrary.getMain(), PacketType.Play.Server.PLAYER_INFO)
		{
			@Override
			public void onPacketSending(PacketEvent event)
			{
				if(event.getPacketType() == PacketType.Play.Server.PLAYER_INFO)
				{
					WrapperPlayServerPlayerInfo packet = new WrapperPlayServerPlayerInfo(event.getPacket());
					if(packet.getAction() == PlayerInfoAction.ADD_PLAYER && packet.getData() != null)
					{
						List<PlayerInfoData> list = new ArrayList<>();
						for(PlayerInfoData data : packet.getData())
						{
							UUID uuid = data.getProfile().getUUID();
							if(players.containsKey(uuid))
							{
								UUID skin = players.get(uuid).getSkin();
								if(skins.containsKey(skin))
								{
									list.add(skins.get(skin).getPlayerInfoData(data));
								}
							}
							else
							{
								list.add(data);
							}
						}
						packet.setData(list);
					}
				}
			}
		});
		LinmaluLibrary.getMain().registerEvents(new Listener()
		{
			@EventHandler(priority = EventPriority.HIGHEST)
			public void Event(AsyncPlayerChatEvent event)
			{
				UUID uuid = event.getPlayer().getUniqueId();
				if(players.containsKey(uuid))
				{
					event.setFormat(event.getFormat().replace("%1$s", players.get(uuid).name + ChatColor.RESET));
				}
			}
		});
	}
	public static void clearPlayers()
	{
		players.entrySet().iterator().forEachRemaining(data -> changePlayer(data.getKey(), data.getKey()));
		players.clear();
	}
	public static void changeName(OfflinePlayer player, String name)
	{
		changePlayerName(player, name, null);
	}
	public static void changeSkin(OfflinePlayer player, String skin)
	{
		changePlayerName(player, null, skin);
	}
	public static void changeSkin(OfflinePlayer player, UUID skin)
	{
		changePlayerUUID(player, null, skin);
	}
	public static void changePlayer(UUID player, UUID target)
	{
		changePlayer(Bukkit.getOfflinePlayer(player), Bukkit.getOfflinePlayer(target));
	}
	public static void changePlayer(OfflinePlayer player, OfflinePlayer target)
	{
		changePlayerUUID(player, target.getName(), target.getUniqueId());
	}
	@SuppressWarnings("deprecation")
	public static void changePlayerName(OfflinePlayer player, String name, String skin)
	{
		changePlayerUUID(player, name, skin == null ? null : Bukkit.getOfflinePlayer(skin).getUniqueId());
	}
	public static void changePlayerUUID(OfflinePlayer player, String name, UUID skin)
	{
		UUID uuid = player.getUniqueId();
		if(skin == null)
		{
			skin = uuid;
		}
		if(name == null)
		{
			name = player.getName();
		}
		else if(name.length() > 16)
		{
			name = name.substring(0, 16);
		}
		name = ChatColor.translateAlternateColorCodes('&', name);
		if(players.containsKey(uuid))
		{
			LinmaluPlayer lp = players.get(uuid);
			if(lp.getName().equals(name) && lp.getSkin().equals(skin))
			{
				players.remove(uuid).start(name, skin);
			}
			else
			{
				players.get(uuid).start(name, skin);
			}
		}
		else
		{
			players.put(uuid, new LinmaluPlayer(uuid, name, skin));
		}
	}
	public static boolean addPotionEffect(Player player, PotionEffect potion)
	{
		if(player.hasPotionEffect(potion.getType()))
		{
			for(PotionEffect pe : player.getActivePotionEffects())
			{
				if(pe.getType().getName().equals(potion.getType().getName()) && (pe.getAmplifier() < potion.getAmplifier() || (pe.getAmplifier() == potion.getAmplifier() && pe.getDuration() < potion.getDuration())))
				{
					player.addPotionEffect(potion, true);
					return true;
				}
			}
			return false;
		}
		else
		{
			player.addPotionEffect(potion, true);
			return true;
		}
	}
	public static Collection<? extends Player> getOnlinePlayers()
	{
		return Bukkit.getOnlinePlayers();
	}
	public static List<Player> getPlayers(String name)
	{
		if(name.equals("@"))
		{
			return new ArrayList<>(Bukkit.getOnlinePlayers());
		}
		List<Player> players = new ArrayList<>();
		Player player = Bukkit.getPlayer(name);
		if(player != null)
		{
			players.add(player);
		}
		return players;
	}

	private final UUID uuid;
	private String name;
	private UUID skin;
	private boolean first = true;

	private LinmaluPlayer(UUID uuid, String name, UUID skin)
	{
		this.uuid = uuid;
		start(name, skin);
	}
	public void start(String name, UUID skin)
	{
		this.name = name;
		this.skin = skin;
		if(!skins.containsKey(skin))
		{
			skins.put(skin, new LinmaluSkin());
		}
		LinmaluSkin ls = skins.get(skin);
		if(ls.isTimeOut())
		{
			new Thread(this).start();
		}
		else
		{
			first = false;
			run();
		}
	}
	@Override
	@SuppressWarnings("deprecation")
	public void run()
	{
		if(first)
		{
			first = false;
			try
			{
				URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + skin.toString().replace("-", "") + "?unsigned=false");
				HttpURLConnection huc = (HttpURLConnection)url.openConnection();
				if(huc.getResponseCode() == HttpURLConnection.HTTP_OK)
				{
					try(InputStreamReader isr = new InputStreamReader(huc.getInputStream(), Charsets.UTF_8))
					{
						new JsonParser().parse(isr).getAsJsonObject().getAsJsonArray("properties").forEach(json ->
						{
							if(skins.containsKey(skin))
							{
								JsonObject data = json.getAsJsonObject();
								skins.get(skin).setWrappedSignedProperty(new WrappedSignedProperty(data.get("name").getAsString(), data.get("value").getAsString(), data.get("signature").getAsString()));
							}
						});
					}
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			Bukkit.getScheduler().scheduleSyncDelayedTask(LinmaluLibrary.getMain(), this);
		}
		else
		{
			first = true;
			Player player = Bukkit.getPlayer(uuid);
			if(player != null)
			{
				WrapperPlayServerPlayerInfo info = new WrapperPlayServerPlayerInfo();
				info.setAction(PlayerInfoAction.ADD_PLAYER);
				if(skins.containsKey(skin))
				{
					info.setData(Arrays.asList(skins.get(skin).getPlayerInfoData(player)));
				}
				WrapperPlayServerRespawn respawn = new WrapperPlayServerRespawn();
				respawn.setDifficulty(Difficulty.valueOf(player.getWorld().getDifficulty().toString()));
				respawn.setDimension(player.getWorld().getEnvironment().getId());
				respawn.setGamemode(NativeGameMode.fromBukkit(player.getGameMode()));
				respawn.setLevelType(player.getWorld().getWorldType());
				WrapperPlayServerAbilities abilities = new WrapperPlayServerAbilities();
				abilities.setCanFly(player.getAllowFlight());
				abilities.setCanInstantlyBuild(player.getGameMode() == GameMode.CREATIVE);
				abilities.setFlying(player.isFlying());
				abilities.setFlyingSpeed(player.getFlySpeed() / 2);
				abilities.setInvulnurable(player.isInvulnerable());
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
				Bukkit.getOnlinePlayers().forEach(p -> info.sendPacket(p));
				respawn.sendPacket(player);
				abilities.sendPacket(player);
				health.sendPacket(player);
				exp.sendPacket(player);
				slot.sendPacket(player);
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
	public UUID getUUID()
	{
		return uuid;
	}
	public String getName()
	{
		return name;
	}
	public UUID getSkin()
	{
		return skin;
	}

	private class LinmaluSkin
	{
		private final Multimap<String, WrappedSignedProperty> textures = HashMultimap.create();
		private long time = 0;

		public boolean isTimeOut()
		{
			if((System.currentTimeMillis() - time) > 60000)
			{
				time = System.currentTimeMillis();
				return true;
			}
			else
			{
				return false;
			}
		}
		public PlayerInfoData getPlayerInfoData(Player player)
		{
			UUID uuid = player.getUniqueId();
			String name = players.containsKey(uuid) ? players.get(uuid).getName() : player.getName();
			WrappedGameProfile profile = new WrappedGameProfile(uuid, name);
			profile.getProperties().putAll(textures);
			return new PlayerInfoData(profile, 0, NativeGameMode.fromBukkit(player.getGameMode()), WrappedChatComponent.fromText(profile.getName()));
		}
		public PlayerInfoData getPlayerInfoData(PlayerInfoData data)
		{
			UUID uuid = data.getProfile().getUUID();
			String name = players.containsKey(uuid) ? players.get(uuid).getName() : data.getProfile().getName();
			WrappedGameProfile profile = new WrappedGameProfile(uuid, name);
			profile.getProperties().putAll(textures);
			return new PlayerInfoData(profile, data.getLatency(), data.getGameMode(), WrappedChatComponent.fromText(profile.getName()));
		}
		public void setWrappedSignedProperty(WrappedSignedProperty wsp)
		{
			textures.clear();
			textures.put("textures", wsp == null ? WrappedSignedProperty.fromValues("", "", "") : wsp);
		}
	}
}
