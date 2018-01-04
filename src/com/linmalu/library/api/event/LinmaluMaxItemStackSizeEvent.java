package com.linmalu.library.api.event;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class LinmaluMaxItemStackSizeEvent extends Event
{
	private static final HandlerList handlers = new HandlerList();

	public static HandlerList getHandlerList()
	{
		return handlers;
	}

	private final Player player;
	private final ItemStack itemStack;
	private int maxSize;
	private boolean first;

	public LinmaluMaxItemStackSizeEvent(Player player, ItemStack itemStack)
	{
		this.player = player;
		this.itemStack = itemStack.clone();
		maxSize = itemStack.getMaxStackSize();
		first = true;
	}
	public LinmaluMaxItemStackSizeEvent callEvent() throws IllegalStateException
	{
		if(first)
		{
			Bukkit.getPluginManager().callEvent(this);
			first = false;
		}
		return this;
	}
	public Player getPlayer()
	{
		return player;
	}
	public ItemStack getItemStack()
	{
		return itemStack;
	}
	public int getMaxSize()
	{
		return maxSize;
	}
	public void setMaxSize(int maxsize)
	{
		this.maxSize = maxsize;
	}
	@Override
	public HandlerList getHandlers()
	{
		return handlers;
	}
}
