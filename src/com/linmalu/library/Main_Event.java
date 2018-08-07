package com.linmalu.library;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRegisterChannelEvent;

import com.linmalu.library.api.LinmaluBlacklist;
import com.linmalu.library.api.LinmaluServer;
import com.linmalu.library.keyboard.LinmaluKeyboardManager;
import com.linmalu.library.network.LinmaluNetwork;

public class Main_Event implements Listener
{
	@EventHandler
	public void Event(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		if(player.isOp())
		{
			LinmaluServer.version(LinmaluLibrary.getMain(), player);
		}
		LinmaluBlacklist.check(player);
	}

	@EventHandler
	public void Event(PlayerQuitEvent event)
	{
		UUID player = event.getPlayer().getUniqueId();
		LinmaluKeyboardManager.getInstance().PlayerQuitEvent(player);
		LinmaluNetwork.getInstance().PlayerQuitEvent(player);
	}

	@EventHandler
	public void Event(PlayerRegisterChannelEvent event)
	{
		if(event.getChannel().equalsIgnoreCase(LinmaluNetwork.CHANNEL))
		{
			LinmaluNetwork.getInstance().sendConnectMessage(event.getPlayer());
		}
	}

	// @EventHandler
	// public void Event(PlayerPickupItemEvent event)
	// {
	// int size = new LinmaluMaxItemStackSizeEvent(event.getPlayer(), event.getItem().getItemStack()).callEvent().getMaxSize();
	// if(LinmaluItemStack.canAddItemStack(event.getPlayer(), size, event.getItem().getItemStack()))
	// {
	// LinmaluItemStack.addItemStack(event.getPlayer(), event.getItem().getItemStack()).forEach(item -> event.getPlayer().getWorld().dropItem(event.getPlayer().getLocation(), item));
	// }
	// else
	// {
	// event.setCancelled(true);
	// }
	// }
	// @EventHandler
	// public void Event(InventoryClickEvent event)
	// {
	// if(event.getWhoClicked().getType() == EntityType.PLAYER)
	// {
	// Player player = (Player)event.getWhoClicked();
	// // Bukkit.broadcastMessage(event.getEventName() + " / " + event.getAction() + " / " + event.getClick());
	// // ItemStack item1 = event.getCursor() == null ? null : event.getCursor().clone();
	// // ItemStack item2 = event.getCurrentItem() == null ? null : event.getCurrentItem().clone();
	//
	// // if(event.getCursor() != null && event.getCurrentItem() != null)
	// // {
	// // ItemStack item1 = event.getCursor().clone();
	// // ItemStack item2 = event.getCurrentItem().clone();
	// // if(item1.getType() != Material.AIR && item2.getType() != Material.AIR && LinmaluItemStack.equalsItemStack(item1, item2))
	// // {
	// // LinmaluMaxItemStackSizeEvent lme = new LinmaluMaxItemStackSizeEvent(player, item1).callEvent();
	// // if(lme.getMaxSize() != lme.getItemStack().getMaxStackSize())
	// // {
	// // event.setCancelled(true);
	// // int max = lme.getMaxSize();
	// // Bukkit.getScheduler().scheduleSyncDelayedTask(LinmaluLibrary.getMain(), () ->
	// // {
	// // int size = item1.getAmount() + item2.getAmount();
	// // switch(event.getAction())
	// // {
	// // case NOTHING:
	// // case PICKUP_ONE:
	// // case PICKUP_SOME:
	// // if(event.getClick() == ClickType.LEFT)
	// // {
	// // if(size > max)
	// // {
	// // item1.setAmount(size - max);
	// // item2.setAmount(max);
	// // }
	// // else
	// // {
	// // item1.setAmount(0);
	// // item2.setAmount(size);
	// // }
	// // }
	// // else if(event.getClick() == ClickType.RIGHT)
	// // {
	// // if(size < max)
	// // {
	// // item1.setAmount(item1.getAmount() - 1);
	// // item2.setAmount(item2.getAmount() + 1);
	// // }
	// // }
	// // break;
	// // case COLLECT_TO_CURSOR:
	// // if(item1.getAmount() < max)
	// // {
	// // for(ItemStack item : event.getClickedInventory().getStorageContents())
	// // {
	// // if(LinmaluItemStack.equalsItemStack(item1, item) && item1.getAmount() < max)
	// // {
	// // size = item1.getAmount() + item.getAmount();
	// // if(size > max)
	// // {
	// // item1.setAmount(max);
	// // item.setAmount(size - max);
	// // }
	// // else
	// // {
	// // item1.setAmount(size);
	// // item.setAmount(0);
	// // }
	// // }
	// // }
	// // }
	// // break;
	// // case CLONE_STACK:
	// // break;
	// // case DROP_ALL_CURSOR:
	// // break;
	// // case DROP_ALL_SLOT:
	// // break;
	// // case DROP_ONE_CURSOR:
	// // break;
	// // case DROP_ONE_SLOT:
	// // break;
	// // case HOTBAR_MOVE_AND_READD:
	// // break;
	// // case HOTBAR_SWAP:
	// // break;
	// // case MOVE_TO_OTHER_INVENTORY:
	// // break;
	// // case PICKUP_ALL:
	// // break;
	// // case PICKUP_HALF:
	// // break;
	// // case PLACE_ALL:
	// // break;
	// // case PLACE_ONE:
	// // break;
	// // case PLACE_SOME:
	// // break;
	// // case SWAP_WITH_CURSOR:
	// // break;
	// // case UNKNOWN:
	// // break;
	// // default:
	// // break;
	// // }
	// // player.setItemOnCursor(item1);
	// // event.getClickedInventory().setItem(event.getSlot(), item2);
	// // });
	// // }
	// // }
	//
	// int size = event.getCursor().getAmount() + event.getCurrentItem().getAmount();
	// switch(event.getAction())
	// {
	// case NOTHING:
	// case PICKUP_ONE:
	// case PICKUP_SOME:
	// if(LinmaluItemStack.equalsItemStack(event.getCursor(), event.getCurrentItem()))
	// {
	// ItemStack item1 = event.getCursor().clone();
	// ItemStack item2 = event.getCurrentItem().clone();
	// Bukkit.getScheduler().scheduleSyncDelayedTask(LinmaluLibrary.getMain(), () ->
	// {
	// if(event.getClick() == ClickType.LEFT)
	// {
	// item1.setAmount(size > 64 ? size - 64 : 0);
	// item2.setAmount(size > 64 ? 64 : size);
	// }
	// else if(event.getClick() == ClickType.RIGHT)
	// {
	// if(size <= 64)
	// {
	// item1.setAmount(item1.getAmount() - 1);
	// item2.setAmount(item2.getAmount() + 1);
	// }
	// }
	// player.setItemOnCursor(item1);
	// event.getClickedInventory().setItem(event.getSlot(), item2);
	// });
	// }
	// break;
	// case COLLECT_TO_CURSOR:
	// Bukkit.getScheduler().scheduleSyncDelayedTask(LinmaluLibrary.getMain(), () ->
	// {
	// ItemStack item1 = player.getItemOnCursor();
	// if(item1 != null && item1.getType() != Material.AIR && item1.getAmount() < 64)
	// {
	// for(ItemStack item2 : event.getClickedInventory().getContents())
	// {
	// if(LinmaluItemStack.equalsItemStack(item1, item2))
	// {
	// int size1 = item1.getAmount() + item2.getAmount();
	// item1.setAmount(size1 > 64 ? 64 : size1);
	// item2.setAmount(size1 > 64 ? size1 - 64 : 0);
	// }
	// }
	// player.setItemOnCursor(item1);
	// }
	// });
	// break;
	// case MOVE_TO_OTHER_INVENTORY:
	// break;
	// default:
	// break;
	// }
	// }
	// }
	// @EventHandler
	// public void Event(InventoryDragEvent event)
	// {
	// Bukkit.broadcastMessage(event.getEventName());
	// }
	// @EventHandler
	// public void Event(InventoryCreativeEvent event)
	// {
	// Bukkit.broadcastMessage(event.getEventName());
	// }
	// @EventHandler
	// public void Event(CraftItemEvent event)
	// {
	// Bukkit.broadcastMessage(event.getEventName());
	// }
}
