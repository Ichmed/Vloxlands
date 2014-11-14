package com.vloxlands.scene;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import com.vloxlands.game.Game;
import com.vloxlands.net.packet.Packet;
import com.vloxlands.net.packet.Packet.PacketTypes;
import com.vloxlands.net.packet.Packet1Disconnect;
import com.vloxlands.net.packet.Packet4ServerInfo;
import com.vloxlands.settings.Tr;
import com.vloxlands.ui.Action;
import com.vloxlands.ui.ClickableGui;
import com.vloxlands.ui.Dialog;
import com.vloxlands.ui.IGuiElement;
import com.vloxlands.ui.IGuiEvent;
import com.vloxlands.ui.Label;
import com.vloxlands.util.RenderAssistant;

public abstract class Scene {
	public ArrayList<IGuiElement> content = new ArrayList<>();
	private boolean wasButton0Down;
	private boolean wasButton1Down;
	private boolean wasButton2Down;
	public boolean initialized = false;
	
	public abstract void init();
	
	boolean uiActive = true, worldActive = false;
	
	// -- title -- //
	boolean titled = false;
	
	protected void setBackground() {
		Label bg = new Label(0, 0, Display.getWidth(), Display.getHeight(), "");
		bg.setZIndex(-1);
		bg.setTexture("/graphics/textures/ui/paper.png");
		bg.stackTexture = true;
		bg.texW = 512;
		bg.texH = 512;
		content.add(bg);
	}
	
	protected void setTitle(String title) {
		Label l = new Label(0, 0, Display.getWidth(), 60, title);
		l.font = l.font.deriveFont(Font.BOLD, 60f);
		content.add(l);
		
		titled = true;
	}
	
	public void onTickContent() {
		ArrayList<IGuiElement> sorted = getSortedContent();
		
		for (IGuiElement i : sorted)
			if (i instanceof ClickableGui) {
				((ClickableGui) i).onTick();
			}
	}
	
	public void onTick() {
		onTickContent();
	}
	
	public void render() {
		renderContent();
		
		glColor3f(1, 1, 1);
		if (titled) RenderAssistant.renderLine(0, 83, Display.getWidth(), true, true);
	}
	
	public void renderContent() {
		ArrayList<IGuiElement> sorted = getSortedContent();
		for (IGuiElement g : sorted)
			if (g.isVisible() && g.wantsRender()) g.render();
	}
	
	public void handleMouse() {
		int x = Mouse.getX();
		int y = Display.getHeight() - Mouse.getY();
		int flag = 0;
		
		flag += Mouse.isButtonDown(0) ? 1 : 0;
		flag += wasButton0Down ? 2 : 0;
		flag += Mouse.isButtonDown(1) ? 4 : 0;
		flag += wasButton1Down ? 8 : 0;
		flag += Mouse.isButtonDown(2) ? 16 : 0;
		flag += wasButton2Down ? 32 : 0;
		
		wasButton0Down = Mouse.isButtonDown(0);
		wasButton1Down = Mouse.isButtonDown(1);
		wasButton2Down = Mouse.isButtonDown(2);
		
		
		
		if ((!uiActive || !handleMouseGUI(x, y, flag))) {
			if ((flag & 1) != 0) resetScene();
			if (worldActive) handleMouseWorld(x, y, flag);
		}
	}
	
	public void resetScene() {
		ArrayList<IGuiElement> sorted = getSortedContent();
		Collections.reverse(sorted);
		for (IGuiElement i : sorted)
			if (i instanceof ClickableGui) ((ClickableGui) i).resetElement();
	}
	
	public void handleKeyboard(int key, char chr, boolean down) {
		for (IGuiElement iG : content) {
			iG.handleKeyboard(key, chr, down);
		}
	}
	
	// not abstract so that implementing won't be forced
	public boolean handleMouseGUI(int posX, int posY, int flag) {
		ClickableGui iG = getObjectUnderCursor();
		if (iG != null && iG.isVisible() && iG.isEnabled()) {
			iG.handleMouse(posX - iG.getX(), posY - iG.getY(), flag);
			return true;
		}
		return false;
	}
	
	private ClickableGui getObjectUnderCursor() {
		ArrayList<IGuiElement> sorted = getSortedContent();
		Collections.reverse(sorted);
		for (IGuiElement i : sorted)
			if (i instanceof ClickableGui) {
				ClickableGui iG = (ClickableGui) i;
				if (iG.isUnderCursor()) return iG;
			}
		return null;
	}
	
	protected ArrayList<IGuiElement> getSortedContent() {
		@SuppressWarnings("unchecked")
		final ArrayList<IGuiElement> sorted = (ArrayList<IGuiElement>) content.clone();
		if (sorted.size() == 0 || sorted.get(0) == null) return new ArrayList<>();
		
		Collections.sort(sorted, new Comparator<IGuiElement>() {
			
			@Override
			public int compare(IGuiElement o1, IGuiElement o2) {
				return o1.getZIndex() - o2.getZIndex();
			}
		});
		
		return sorted;
	}
	
	// not abstract so that implementing won't be forced
	public void handleMouseWorld(int x, int y, int flag) {}
	
	public void lockScene() {
		for (IGuiElement i : content)
			if (i instanceof ClickableGui) {
				((ClickableGui) i).setEnabled(false);
			}
	}
	
	public void unlockScene() {
		for (IGuiElement i : content)
			if (i instanceof ClickableGui) {
				((ClickableGui) i).setEnabled(true);
			}
	}
	
	public boolean isWorldActive() {
		return worldActive;
	}
	
	// not abstract so that implementing won't be forced
	public void onClientMessage(String message) {}
	
	// not abstract so that implementing won't be forced
	public void onClientReveivedData(byte[] data) {
		PacketTypes type = Packet.lookupPacket(data[0]);
		switch (type) {
			case DISCONNECT: {
				Packet1Disconnect p = new Packet1Disconnect(data);
				if (p.getUsername().equals(Game.client.getUsername())) {
					if (!p.getReason().equals("mp.reason.disconnect")) {
						Game.currentGame.addScene(new Dialog(Tr._("info"), Tr._(p.getReason()), new Action(Tr._("close"), new IGuiEvent() {
							@Override
							public void trigger() {
								Game.currentGame.setScene(new SceneMainMenu());
							}
						})));
					} else Game.currentGame.setScene(new SceneMainMenu());
				}
				try {
					Game.client.sendPacket(new Packet4ServerInfo());
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}
			default:
				break;
		}
	}
}
