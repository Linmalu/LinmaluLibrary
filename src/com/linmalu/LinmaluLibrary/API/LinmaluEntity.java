package com.linmalu.LinmaluLibrary.API;

import org.bukkit.entity.Entity;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

public class LinmaluEntity
{
	public static void reloadEntity(Entity entity)
	{
		ProtocolManager pm = ProtocolLibrary.getProtocolManager();
		pm.updateEntity(entity, pm.getEntityTrackers(entity));
	}
}
