package com.linmalu.library.api;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class LinmaluNetworkPacket
{
	private final ByteBuffer _buffer;
	private byte _id;
	private short _type;

	/**
	 * 쓰기용
	 *
	 * @param capacity 최대 크기
	 */
	protected LinmaluNetworkPacket(byte id, int capacity)
	{
		_id = id;
		_buffer = ByteBuffer.allocate(Byte.BYTES + Short.BYTES + capacity);
		_buffer.put(id);
	}

	/**
	 * 읽기용
	 *
	 * @param array 데이터
	 */
	protected LinmaluNetworkPacket(byte[] array)
	{
		_buffer = ByteBuffer.wrap(array);
		_id = _buffer.get();
		_type = _buffer.getShort();
	}

	/**
	 * 가져오거나 쓸수있는 크기
	 */
	public int getSize()
	{
		return _buffer.remaining();
	}

	/**
	 * 배열 가져오기
	 */
	public byte[] getArray()
	{
		return _buffer.array();
	}

	/**
	 * Code 가져오기
	 */
	public byte getId()
	{
		return _id;
	}

	/**
	 * 타입 가져오기
	 */
	public short getType()
	{
		return _type;
	}

	/**
	 * 타입 설정
	 */
	public LinmaluNetworkPacket setType(short type)
	{
		_type = type;
		return this;
	}

	/**
	 * Byte 가져오기
	 */
	public byte getByte()
	{
		return _buffer.get();
	}

	/**
	 * Byte 넣기
	 */
	public LinmaluNetworkPacket setByte(byte value)
	{
		_buffer.put(value);
		return this;
	}

	/**
	 * Bytes 가져오기
	 */
	public byte[] getBytes(byte[] result)
	{
		_buffer.get(result);
		return result;
	}

	/**
	 * Bytes 넣기
	 */
	public LinmaluNetworkPacket addBytes(byte[] value)
	{
		_buffer.put(value);
		return this;
	}

	/**
	 * Short 가져오기
	 */
	public short getShort()
	{
		return _buffer.getShort();
	}

	/**
	 * Short 넣기
	 */
	public LinmaluNetworkPacket addShort(short value)
	{
		_buffer.putShort(value);
		return this;
	}

	/**
	 * Int 가져오기
	 */
	public int getInt()
	{
		return _buffer.getInt();
	}

	/**
	 * Int 넣기
	 */
	public LinmaluNetworkPacket addInt(int value)
	{
		_buffer.putInt(value);
		return this;
	}

	/**
	 * Long 가져오기
	 */
	public long getLong()
	{
		return _buffer.getLong();
	}

	/**
	 * Long 넣기
	 */
	public LinmaluNetworkPacket addLong(long value)
	{
		_buffer.putLong(value);
		return this;
	}

	/**
	 * Float 가져오기
	 */
	public float getFloat()
	{
		return _buffer.getFloat();
	}

	/**
	 * Float 넣기
	 */
	public LinmaluNetworkPacket addFloat(float value)
	{
		_buffer.putFloat(value);
		return this;
	}

	/**
	 * Double 가져오기
	 */
	public double getDouble()
	{
		return _buffer.getDouble();
	}

	/**
	 * Double 넣기
	 */
	public LinmaluNetworkPacket addDouble(double vlaue)
	{
		_buffer.putDouble(vlaue);
		return this;
	}

	/**
	 * String 가져오기
	 */
	public String getString()
	{
		byte[] data = new byte[_buffer.getShort()];
		_buffer.get(data);
		return new String(data, StandardCharsets.UTF_8);
	}

	/**
	 * String 넣기
	 */
	public LinmaluNetworkPacket addString(String value)
	{
		byte[] data = value.getBytes(StandardCharsets.UTF_8);
		_buffer.putShort((short)data.length);
		_buffer.put(data);
		return this;
	}

	/**
	 * String 넣기
	 */
	public LinmaluNetworkPacket addString(byte[] value)
	{
		_buffer.putShort((short)value.length);
		_buffer.put(value);
		return this;
	}
}
