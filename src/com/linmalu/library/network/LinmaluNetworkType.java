package com.linmalu.library.network;

public enum LinmaluNetworkType
{
	NONE,
	CONNECT_SC_MESSAGE,
	VERSION_CS_MESSAGE,
	KEYUP_CS_MESSAGE,
	KEYDOWN_CS_MESSAGE,
	CREATE_IMAGE_CS_MESSAGE,
	CREATE_IMAGE_SC_MESSAGE,
	DELETE_IMAGE_SC_MESSAGE,
	DRAW_IMAGE_SC_MESSAGE,
	TIME_IMAGE_SC_MESSAGE,
	LOCATION_IMAGE_SC_MESSAGE,
	CHANGE_IMAGE_SC_MESSAGE,
	DRAW_TEXT_SC_MESSAGE,
	TIME_TEXT_SC_MESSAGE,
	LOCATION_TEXT_SC_MESSAGE,
	CHANGE_TEXT_SC_MESSAGE,
	COLOR_TEXT_SC_MESSAGE,
	ERASE_OBJECT_SC_MESSAGE,
	CLEAR_OBJECT_SC_MESSAGE,
	ERASE_RENDER_SC_MESSAGE,
	DRAW_RENDER_SC_MESSAGE,
	RESET_RENDER_SC_MESSAGE;

	public final short ID = (short)ordinal();

	public static byte NETWORK_ID = (byte)0xCC;

	public static LinmaluNetworkType getLinmaluNetworkType(int id)
	{
		for(LinmaluNetworkType type : values())
		{
			if(type.ordinal() == id)
			{
				return type;
			}
		}
		return NONE;
	}
}
