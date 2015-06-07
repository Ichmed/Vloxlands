package com.vloxlands.net.packet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vloxlands.net.Player;

/**
 * @author Dakror
 */
public class Packet4ServerInfo extends Packet {
	Player[] players;
	
	public Packet4ServerInfo(Player[] players) {
		super(4);
		this.players = players;
	}
	
	public Packet4ServerInfo() {
		super(4);
	}
	
	public Packet4ServerInfo(byte[] data) {
		super(4);
		String s = readData(data);
		try {
			JSONArray json = new JSONArray(s);
			players = new Player[json.length()];
			for (int i = 0; i < json.length(); i++) {
				players[i] = new Player(json.getJSONObject(i));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public Player[] getPlayers() {
		return players;
	}
	
	@Override
	protected byte[] getPacketData() {
		if (players == null) return "".getBytes();
		
		JSONArray json = new JSONArray();
		for (Player p : players) {
			try {
				json.put(new JSONObject(p.serialize()));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return json.toString().getBytes();
	}
}
