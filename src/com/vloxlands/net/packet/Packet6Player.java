package com.vloxlands.net.packet;

import org.json.JSONException;
import org.json.JSONObject;

import com.vloxlands.net.Player;

/**
 * @author Dakror
 */
public class Packet6Player extends Packet
{
	Player player;
	
	public Packet6Player(Player p)
	{
		super(6);
		player = p;
	}
	
	public Packet6Player(byte[] data)
	{
		super(6);
		try
		{
			player = new Player(new JSONObject(readData(data)));
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}
	
	public Player getPlayer()
	{
		return player;
	}
	
	@Override
	public byte[] getPacketData()
	{
		return player.serialize().getBytes();
	}
}
