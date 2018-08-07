package com.linmalu.library.api.event;

import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.linmalu.library.keyboard.LinmaluKeyboard;

public class LinmaluKeyboardEvent extends Event
{
	private static final HandlerList handlers = new HandlerList();

	public static HandlerList getHandlerList()
	{
		return handlers;
	}

	private final Player player;
	private final LinmaluKeyboard key;
	private final Set<LinmaluKeyboard> keys;

	public LinmaluKeyboardEvent(Player player, LinmaluKeyboard key, Set<LinmaluKeyboard> keys)
	{
		this.player = player;
		this.key = key;
		this.keys = keys;
	}
	public Player getPlayer()
	{
		return player;
	}

	public LinmaluKeyboard getKey()
	{
		return key;
	}

	public boolean isKeyState()
	{
		return keys.contains(key);
	}

	public boolean isKeyState(LinmaluKeyboard key)
	{
		return keys.contains(key);
	}

	public boolean isAltState()
	{
		return keys.contains(LinmaluKeyboard.KEY_LMENU) || keys.contains(LinmaluKeyboard.KEY_RMENU);
	}

	public boolean isCtrlState()
	{
		return keys.contains(LinmaluKeyboard.KEY_LCONTROL) || keys.contains(LinmaluKeyboard.KEY_RCONTROL);
	}

	public boolean isShiftState()
	{
		return keys.contains(LinmaluKeyboard.KEY_LSHIFT) || keys.contains(LinmaluKeyboard.KEY_RSHIFT);
	}

	@Override
	public HandlerList getHandlers()
	{
		return handlers;
	}
}
