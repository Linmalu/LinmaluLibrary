package com.linmalu.library.api.event;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.linmalu.keyboard.LinmaluKeyboard;
import com.linmalu.keyboard.LinmaluKeyboardData;

public class LinmaluKeyboardEvent extends Event
{
	private static final HandlerList handlers = new HandlerList();

	public static HandlerList getHandlerList()
	{
		return handlers;
	}

	private final Player player;
	private final LinmaluKeyboardData lkd;

	public LinmaluKeyboardEvent(Player player, LinmaluKeyboardData lkd)
	{
		this.player = player;
		this.lkd = lkd;
	}
	public Player getPlayer()
	{
		return player;
	}
	public boolean isAltState()
	{
		return lkd.isAltState();
	}
	public boolean isCtrlState()
	{
		return lkd.isCtrlState();
	}
	public boolean isShiftState()
	{
		return lkd.isShiftState();
	}
	public LinmaluKeyboard getKey()
	{
		return lkd.getKey();
	}
	public boolean isKeyState()
	{
		return lkd.isKeyState();
	}
	public boolean isKeyState(LinmaluKeyboard key)
	{
		return lkd.isKeyState(key);
	}
	public List<LinmaluKeyboard> getKeys()
	{
		return lkd.getKeys();
	}
	@Override
	public HandlerList getHandlers()
	{
		return handlers;
	}
}
