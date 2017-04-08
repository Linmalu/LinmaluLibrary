package com.linmalu.library.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class LinmaluShortcutkeyJoinEvent extends Event
{
	private static final HandlerList handlers = new HandlerList();
	private Player player;

	public static HandlerList getHandlerList()
	{
		return handlers;
	}
	public LinmaluShortcutkeyJoinEvent(Player player)
	{
		this.player = player;
	}
	@Override
	public HandlerList getHandlers()
	{
		return handlers;
	}
	public Player getPlayer()
	{
		return player;
	}
}
