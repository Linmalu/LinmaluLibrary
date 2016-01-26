package com.linmalu.library.api;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class LinmaluMath
{
	private LinmaluMath(){}
	@Deprecated
	public static Location playerRotation(Location center, Location loc, Player player)
	{
		return rotation(loc, center, player);
	}
	public static Location rotation(Location loc, Location center, Player player)
	{
		loc = loc.clone();
		loc.setZ(0);
		rotationZ(loc, center, 90);
		rotationX(loc, center, pitchAngle(player, center));
		rotationY(loc, center, yawAngle(player, center) + 180);
		return loc;
	}
	public static Location rotationX(Location loc, Location center, double angle)
	{
		double y = loc.getY() - center.getX();
		double z = loc.getZ() - center.getZ();
		loc.setY(Math.cos(Math.toRadians(angle)) * y - Math.sin(Math.toRadians(angle)) * z);
		loc.setZ(Math.sin(Math.toRadians(angle)) * y + Math.cos(Math.toRadians(angle)) * z);
		return loc;
	}
	public static Location rotationY(Location loc, Location center, double angle)
	{
		double x = loc.getX() - center.getX();
		double z = loc.getZ() - center.getZ();
		loc.setX(Math.cos(Math.toRadians(angle)) * x + Math.sin(Math.toRadians(angle)) * z);
		loc.setZ(-Math.sin(Math.toRadians(angle)) * x + Math.cos(Math.toRadians(angle)) * z);
		return loc;
	}
	public static Location rotationZ(Location loc, Location center, double angle)
	{
		double x = loc.getX() - center.getX();
		double y = loc.getY() - center.getY();
		loc.setX(Math.cos(Math.toRadians(angle)) * x - Math.sin(Math.toRadians(angle)) * y);
		loc.setY(Math.sin(Math.toRadians(angle)) * x + Math.cos(Math.toRadians(angle)) * y);
		return loc;
	}
	public static double pitchAngle(Player player, Location center)
	{
		Location loc = player.getLocation();
		double x = loc.getX() - center.getX();
		double z = loc.getZ() - center.getZ();
		double a = -Math.atan2(z, x);
		double x1 = Math.cos(a) * x - Math.sin(a) * z;
		//double z1 = Math.sin(a) * x + Math.cos(a) * z;
		double y = loc.getY() - center.getY();
		return Math.toDegrees(Math.atan2(y, x1));
	}
	public static double yawAngle(Player player, Location center)
	{
		Location loc = player.getLocation();
		double x = loc.getX() - center.getX();
		double z = loc.getZ() - center.getZ();
		return Math.toDegrees(Math.atan2(x, z));
	}
}
