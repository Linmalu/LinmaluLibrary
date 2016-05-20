package com.linmalu.library.api;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class LinmaluMath
{
	public static Location rotation(Location loc, Location center, Player player)
	{
		loc.setZ(0);
		rotationZ(loc, center, 90);
		rotationX(loc, center, pitchAngle(player.getLocation(), center));
		rotationY(loc, center, yawAngle(player.getLocation(), center) + 180);
		return loc;
	}
	public static Location rotation(Location loc, Location center, double angleX, double angleY)
	{
		angleX += 180;
		Location base = loc.clone();
		rotationY(loc, center, yawAngle(base, center));
		rotationX(loc, center, angleY);
		rotationY(loc, center, -yawAngle(base, center) + angleX);
		return loc;
	}
	public static Location rotationX(Location loc, Location center, double angle)
	{
		double y = loc.getY() - center.getY();
		double z = loc.getZ() - center.getZ();
		loc.setX(loc.getX() - center.getX());
		loc.setY(Math.cos(Math.toRadians(angle)) * y - Math.sin(Math.toRadians(angle)) * z);
		loc.setZ(Math.sin(Math.toRadians(angle)) * y + Math.cos(Math.toRadians(angle)) * z);
		return loc.add(center);
	}
	public static Location rotationY(Location loc, Location center, double angle)
	{
		double x = loc.getX() - center.getX();
		double z = loc.getZ() - center.getZ();
		loc.setX(Math.cos(Math.toRadians(angle)) * x + Math.sin(Math.toRadians(angle)) * z);
		loc.setY(loc.getY() - center.getY());
		loc.setZ(-Math.sin(Math.toRadians(angle)) * x + Math.cos(Math.toRadians(angle)) * z);
		return loc.add(center);
	}
	public static Location rotationZ(Location loc, Location center, double angle)
	{
		double x = loc.getX() - center.getX();
		double y = loc.getY() - center.getY();
		loc.setX(Math.cos(Math.toRadians(angle)) * x - Math.sin(Math.toRadians(angle)) * y);
		loc.setY(Math.sin(Math.toRadians(angle)) * x + Math.cos(Math.toRadians(angle)) * y);
		loc.setZ(loc.getZ() - center.getZ());
		return loc.add(center);
	}
	public static double pitchAngle(Location loc, Location center)
	{
		double x = loc.getX() - center.getX();
		double z = loc.getZ() - center.getZ();
		double a = -Math.atan2(z, x);
		double x1 = Math.cos(a) * x - Math.sin(a) * z;
		double y = loc.getY() - center.getY();
		return Math.toDegrees(Math.atan2(x1, y)) - 90;
	}
	public static double yawAngle(Location loc, Location center)
	{
		double x = loc.getX() - center.getX();
		double z = loc.getZ() - center.getZ();
		return (Math.toDegrees(Math.atan2(z, x)) + 270) % 360;
	}
}
