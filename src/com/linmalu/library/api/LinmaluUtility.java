package com.linmalu.library.api;

import java.util.Random;

public class LinmaluUtility
{
	public static String newPassword(int size)
	{
		StringBuilder sb = new StringBuilder();
		Random ran = new Random();
		for(int i = 0; i < size; i++)
		{
			switch(ran.nextInt(3))
			{
				case 0:
					sb.append(ran.nextInt(10));
					break;
				case 1:
					sb.append((char)(ran.nextInt(26) + 0x41));
					break;
				case 2:
					sb.append((char)(ran.nextInt(26) + 0x61));
					break;
			}
		}
		return sb.toString();
	}
	public static long byteTolong(byte ... bytes)
	{
		long i = 0;
		for(byte b : bytes)
		{
			i = (i << 8) | b;
		}
		return i;
	}
	public static String consoleToText(String msg)
	{
		StringBuilder sb = new StringBuilder();
		String[] msgs = msg.split("\u001B");
		for(int i = 0; i < msgs.length; i++)
		{
			if(i == 0)
			{
				sb.append(msgs[i]);
			}
			else
			{
				int index = msgs[i].indexOf("\u006D");
				if(index != -1)
				{
					sb.append(msgs[i].substring(index + 1));
				}
				else
				{
					sb.append(msgs[i]);
				}
			}
		}
		return sb.toString();
	}
}
