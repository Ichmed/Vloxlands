package com.vloxlands.gen;

import java.awt.Point;
import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import com.vloxlands.game.Game;
import com.vloxlands.game.entity.Entity;
import com.vloxlands.game.entity.EntityBuilding;
import com.vloxlands.game.world.Island;
import com.vloxlands.game.world.Map;
import com.vloxlands.gen.island.IslandGenerator;
import com.vloxlands.net.packet.Packet10EntityBuilding;
import com.vloxlands.net.packet.Packet8Attribute;
import com.vloxlands.net.packet.Packet9Island;

public class MapGenerator extends Thread {
	public static enum MapSize {
		DEBUG(1),
		TINY(3),
		SMALL(5),
		MEDIUM(7),
		BIG(9),
		HUGE(12);
		
		private int size;
		
		private MapSize(int size) {
			this.size = size;
		}
		
		public int getSizeSQ() {
			return size * size;
		}
		
		public int getSize() {
			return size;
		}
	}
	
	public Map map;
	public float progress, progressBefore, lastProgress;
	
	IslandGenerator gen;
	int index;
	int playerCount;
	int playableIslands;
	float factor;
	MapSize size;
	ArrayList<Point> takenSpots;
	
	public MapGenerator(int playerCount, MapSize size) {
		this.size = size;
		this.playerCount = playerCount;
		playableIslands = (int) (Math.random() * (playerCount * 2) + playerCount);
		
		if (playableIslands > size.getSizeSQ()) playableIslands = size.getSizeSQ();
		
		progress = 0;
		progressBefore = 0;
		setDaemon(true);
		setName("MapGenerator-Thread");
		takenSpots = new ArrayList<>();
		factor = size.getSizeSQ();
	}
	
	@Override
	public void run() {
		map = new Map();
		try {
			Game.server.sendPacketToAllClients(new Packet8Attribute("mapeditor_progress_string", "generatemap"));
		} catch (Exception e) {}
		
		for (int i = 0; i < playableIslands; i++) {
			float y = (float) (Math.random() * 256);
			Point spot = pickRandomSpot();
			generateIsland(y, 32, 48);
			gen.finishedIsland.setPos(new Vector3f(spot.x * Island.SIZE, y, spot.y * Island.SIZE));
			
			map.addIsland(gen.finishedIsland);
			sendIsland(gen.finishedIsland);
		}
		
		for (int i = 0; i < size.getSize(); i++) {
			for (int j = 0; j < size.getSize(); j++) {
				if (takenSpots.contains(new Point(i, j))) continue;
				float y = (float) (Math.random() * 256);
				generateIsland(y, 12, 20);
				gen.finishedIsland.setPos(new Vector3f(i * Island.SIZE, y, j * Island.SIZE));
				
				map.addIsland(gen.finishedIsland);
				sendIsland(gen.finishedIsland);
			}
		}
		
		Game.server.setMap(map);
		try {
			Game.server.sendPacketToAllClients(new Packet8Attribute("mapeditor_progress_float", 1));
		} catch (Exception e) {}
	}
	
	private void generateIsland(float y, int minSize, int maxSize) {
		gen = new IslandGenerator(minSize, maxSize, y);
		gen.start();
		while (gen.finishedIsland == null) {
			lastProgress = new Float(progress);
			
			progress = (gen.progress / factor) + progressBefore;
			
			if (progress != lastProgress) {
				try {
					Game.server.sendPacketToAllClients(new Packet8Attribute("mapeditor_progress_float", progress));
				} catch (Exception e) {}
			}
		}
		progressBefore = new Float(progress);
	}
	
	private Point pickRandomSpot() {
		Point p = new Point();
		
		do {
			p.x = (int) (Math.random() * size.getSize());
			p.y = (int) (Math.random() * size.getSize());
		} while (takenSpots.contains(p));
		takenSpots.add(p);
		
		return p;
	}
	
	private void sendIsland(Island island) {
		try {
			Game.server.sendPacketToAllClients(new Packet9Island(island));
			
			for (Entity e : island.getEntities()) {
				if (e instanceof EntityBuilding) Game.server.sendPacketToAllClients(new Packet10EntityBuilding((EntityBuilding) e, map.islands.indexOf(island)));
			}
		} catch (Exception e) {}
	}
}
