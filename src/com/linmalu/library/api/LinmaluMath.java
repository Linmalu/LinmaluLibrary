package com.linmalu.library.api;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Random;

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

	/**
	 * 사이값으로 바꾸기
	 */
	public static int betweenValue(int value, int min, int max)
	{
		return (value + min) % (max - min) + (value < 0 ? max : min);
	}

	/**
	 * 사이값으로 바꾸기
	 */
	public static long betweenValue(long value, long min, long max)
	{
		return (value + min) % (max - min) + (value < 0 ? max : min);
	}

	/**
	 * 사이값으로 바꾸기
	 */
	public static float betweenValue(float value, float min, float max)
	{
		return (value + min) % (max - min) + (value < 0 ? max : min);
	}

	/**
	 * 사이값으로 바꾸기
	 */
	public static double betweenValue(double value, double min, double max)
	{
		return (value + min) % (max - min) + (value < 0 ? max : min);
	}

	/**
	 * 사이값에서 거리 구하기
	 */
	public static int distanceValue(int value1, int value2, int min, int max)
	{
		return betweenValue(value2 - value1, 0, max - min);
	}

	/**
	 * 사이값에서 거리 구하기
	 */
	public static long distanceValue(long value1, long value2, long min, long max)
	{
		return betweenValue(value2 - value1, 0, max - min);
	}

	/**
	 * 사이값에서 거리 구하기
	 */
	public static float distanceValue(float value1, float value2, float min, float max)
	{
		return betweenValue(value2 - value1, 0, max - min);
	}

	/**
	 * 사이값에서 거리 구하기
	 */
	public static double distanceValue(double value1, double value2, double min, double max)
	{
		return betweenValue(value2 - value1, 0, max - min);
	}

	public static double distance(Location loc1, Location loc2)
	{
		return loc1.getWorld() != loc2.getWorld() ? Double.MAX_VALUE : loc1.distance(loc2);
	}

	public static Vector getRandomVector(int x1, int z1, int x2, int z2)
	{
		Random ran = new Random();
		if(x1 > x2)
		{
			int x = x1;
			x1 = x2;
			x2 = x;
		}
		if(z1 > z2)
		{
			int z = z1;
			z1 = z2;
			z2 = z;
		}
		return new Vector(ran.nextInt(x2 - x1 + 1) + x1 + 0.5D, 0, ran.nextInt(z2 - z1 + 1) + z1 + 0.5D);
	}
}
