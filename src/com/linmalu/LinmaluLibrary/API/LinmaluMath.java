package com.linmalu.LinmaluLibrary.API;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class LinmaluMath
{
	public static Location playerRotation(Location center, Location loc, Player player)
	{
		return new LinmaluMath(center, loc, player).loc;
	}

	private Location loc;

	private LinmaluMath(Location center, Location loc, Player player)
	{
		Location location = loc.clone();
		location.setX(loc.getY() - center.getY());
		location.setY(loc.getX() - center.getX());
		location.setZ(0);
		rotationZ(location, 90);
		rotationX(location, pitchAngle(player, center));
		rotationY(location, yawAngle(player, center) + 180);
		location.add(center.getX(), center.getY(), center.getZ());
		this.loc = location;
	}
	private void rotationX(Location center, double angle)
	{
		double y = center.getY();
		double z = center.getZ();
		center.setX(center.getX());
		center.setY(Math.cos(Math.toRadians(angle)) * y - Math.sin(Math.toRadians(angle)) * z);
		center.setZ(Math.sin(Math.toRadians(angle)) * y + Math.cos(Math.toRadians(angle)) * z);
	}
	private void rotationY(Location center, double angle)
	{
		double x = center.getX();
		double z = center.getZ();
		center.setX(Math.cos(Math.toRadians(angle)) * x + Math.sin(Math.toRadians(angle)) * z);
		center.setY(center.getY());
		center.setZ(-Math.sin(Math.toRadians(angle)) * x + Math.cos(Math.toRadians(angle)) * z);
	}
	private void rotationZ(Location center, double angle)
	{
		double x = center.getX();
		double y = center.getY();
		center.setX(Math.cos(Math.toRadians(angle)) * x - Math.sin(Math.toRadians(angle)) * y);
		center.setY(Math.sin(Math.toRadians(angle)) * x + Math.cos(Math.toRadians(angle)) * y);
		center.setZ(center.getZ());
	}
	private double pitchAngle(Player player, Location center)
	{
		Location loc = player.getLocation();
		double x = loc.getX() - center.getX();
		double z = loc.getZ() - center.getZ();
		double a = -Math.atan2(z, x);
		double x1 = Math.cos(a) * x - Math.sin(a) * z;
		//		double z1 = Math.sin(a) * x + Math.cos(a) * z;
		double y = loc.getY() - center.getY();
		return Math.toDegrees(Math.atan2(y, x1));
	}
	private double yawAngle(Player player, Location center)
	{
		Location loc = player.getLocation();
		double x = loc.getX() - center.getX();
		double z = loc.getZ() - center.getZ();
		return Math.toDegrees(Math.atan2(x, z));
	}
}
