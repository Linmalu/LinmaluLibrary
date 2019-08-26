package com.linmalu.library.api;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.LinkedHashMap;
import java.util.Map;

public class LinmaluSquareLocation implements ConfigurationSerializable
{
	private World _world;
	private double _x1, _y1, _z1, _x2, _y2, _z2;

	public LinmaluSquareLocation(World world, double x1, double y1, double z1, double x2, double y2, double z2)
	{
		this._world = world;
		this._x1 = x1;
		this._y1 = y1;
		this._z1 = z1;
		this._x2 = x2;
		this._y2 = y2;
		this._z2 = z2;
	}

	/**
	 * 월드 얻기
	 */
	public World getWorld()
	{
		return _world;
	}

	/**
	 * 월드 설정
	 */
	public LinmaluSquareLocation setWorld(World world)
	{
		_world = world;
		return this;
	}

	/**
	 * X1 얻기
	 */
	public double getX1()
	{
		return _x1;
	}

	/**
	 * X1 설정
	 */
	public LinmaluSquareLocation setX1(double x1)
	{
		_x1 = x1;
		return this;
	}

	/**
	 * Y1 얻기
	 */
	public double getY1()
	{
		return _y1;
	}

	/**
	 * Y1 설정
	 */
	public LinmaluSquareLocation setY1(double y1)
	{
		_y1 = y1;
		return this;
	}

	/**
	 * Z1 얻기
	 */
	public double getZ1()
	{
		return _z1;
	}

	/**
	 * Z1 설정
	 */
	public LinmaluSquareLocation setZ1(double z1)
	{
		_z1 = z1;
		return this;
	}

	/**
	 * x2 얻기
	 */
	public double getX2()
	{
		return _x2;
	}

	/**
	 * X2 설정
	 */
	public LinmaluSquareLocation setX2(double x2)
	{
		_x2 = x2;
		return this;
	}

	/**
	 * Y2 얻기
	 */
	public double getY2()
	{
		return _y2;
	}

	/**
	 * Y2 설정
	 */
	public LinmaluSquareLocation setY2(double y2)
	{
		_y2 = y2;
		return this;
	}

	/**
	 * Z2 얻기
	 */
	public double getZ2()
	{
		return _z2;
	}

	/**
	 * Z2 설정
	 */
	public LinmaluSquareLocation setZ2(double z2)
	{
		_z2 = z2;
		return this;
	}

	private static String WORLD = "world";
	private static String X1 = "x1";
	private static String Y1 = "y1";
	private static String Z1 = "z1";
	private static String X2 = "x2";
	private static String Y2 = "y2";
	private static String Z2 = "z2";

	@Override
	public Map<String, Object> serialize()
	{
		Map<String, Object> map = new LinkedHashMap<>();
		map.put(WORLD, _world.getName());
		map.put(X1, _x1);
		map.put(Y1, _y1);
		map.put(Z1, _z1);
		map.put(X2, _x2);
		map.put(Y2, _y2);
		map.put(Z2, _z2);
		return map;
	}

	public static LinmaluSquareLocation deserialize(Map<String, Object> map)
	{
		World world = Bukkit.getWorld((String)map.get(WORLD));
		if(world == null)
		{
			return null;
		}
		return new LinmaluSquareLocation(world, (double)map.get(X1), (double)map.get(Y1), (double)map.get(Z1), (double)map.get(X2), (double)map.get(Y2), (double)map.get(Z2));
	}
}
