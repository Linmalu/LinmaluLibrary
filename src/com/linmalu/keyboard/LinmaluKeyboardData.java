package com.linmalu.keyboard;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class LinmaluKeyboardData implements Serializable
{
	public static LinmaluKeyboardData toLinmaluKeyboardData(byte[] bytes) throws Exception
	{
		try(ObjectInputStream obj = new ObjectInputStream(new ByteArrayInputStream(bytes)))
		{
			return (LinmaluKeyboardData)obj.readObject();
		}
	}

	private final LinmaluKeyboard key;
	private final List<LinmaluKeyboard> keys;

	public LinmaluKeyboardData(LinmaluKeyboard key, List<LinmaluKeyboard> keys)
	{
		this.key = key;
		this.keys = keys;
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
	public List<LinmaluKeyboard> getKeys()
	{
		return new ArrayList<>(keys);
	}
	public byte[] toBytes() throws IOException
	{
		try(ByteArrayOutputStream bos = new ByteArrayOutputStream(); ObjectOutputStream oos = new ObjectOutputStream(bos))
		{
			oos.writeObject(this);
			oos.flush();
			byte[] bytes = bos.toByteArray();
			return bytes;
		}
	}
}
