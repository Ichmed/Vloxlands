package com.vloxlands.ui;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Font;
import java.io.IOException;

import com.vloxlands.game.Game;
import com.vloxlands.net.Player;
import com.vloxlands.net.packet.Packet1Disconnect;
import com.vloxlands.net.packet.Packet6Player;
import com.vloxlands.settings.Tr;
import com.vloxlands.util.FontAssistant;
import com.vloxlands.util.RenderAssistant;

/**
 * @author Dakror
 */
public class LobbySlot extends Container {
	public static final int HEIGHT = 80;
	public static final int HEIGHT2 = 68;
	public Font font = FontAssistant.GAMEFONT.deriveFont(Font.BOLD, 40f);
	Player p;
	
	public LobbySlot(Player p) {
		super(0, 0, 0, HEIGHT, false, false);
		this.p = p;
	}
	
	public Player getPlayer() {
		return p;
	}
	
	@Override
	public void render() {
		if (p.getUsername().equals(Game.client.getUsername())) glColor3f(166 / 256f, 213 / 256f, 236 / 256f);
		RenderAssistant.renderText(x + 15, y + height / 6, p.getUsername(), font);
		if (p.getUsername().equals(Game.client.getUsername())) glColor3f(1, 1, 1);
		
		RenderAssistant.renderLine(x, y + height - 20, width, true, false);
		RenderAssistant.renderLine(x + width - 128, y + height / 2 - 15, 128, true, false);
		RenderAssistant.renderLine(x + width - 122, y - 5, 30, false, false);
		RenderAssistant.renderLine(x + width - 122, y + 35, 30, false, false);
		glEnable(GL_BLEND);
		RenderAssistant.bindTexture("/graphics/textures/ui/gui.png");
		RenderAssistant.renderRect(x + width - 134, y + 18, 19, 26, 787 / 1024f, 409 / 1024f, 19 / 1024f, 26 / 1024f);
		
		RenderAssistant.renderRect(x + width - 141, y + height - 27, 26, 19, 780 / 1024f, 450 / 1024f, 26 / 1024f, 19 / 1024f);
		glDisable(GL_BLEND);
		
		renderContent();
	}
	
	/**
	 * Should have set the slot's width first
	 */
	public void initButtons() {
		components.clear();
		
		ImageButton rename = new ImageButton(width - 124, 0, 45, 45);
		rename.setTexture("/graphics/textures/ui/Rename.png");
		rename.disabledColor = IGuiElement.gray;
		rename.setClickEvent(new IGuiEvent() {
			@Override
			public void trigger() {
				final InputField name = new InputField(0, 0, 0, Game.client.getUsername(), Tr._("username"));
				Dialog dialog = new Dialog(Tr._("namechange"), "", new Action(Tr._("cancel"), Dialog.CLOSE_EVENT), new Action(Tr._("ok"), new IGuiEvent() {
					@Override
					public void trigger() {
						new Thread() {
							@Override
							public void run() {
								Game.currentGame.getActiveScene().lockScene();
								String oldUsername = Game.client.getUsername();
								Game.client.renameClient(name.getText());
								while (Game.client.getUsername().equals(oldUsername)) {
									if (Game.client.isRejected()) {
										Game.currentGame.getActiveScene().unlockScene();
										Game.currentGame.addScene(new Dialog(Tr._("error"), Tr._("mp.reject." + Game.client.getRejectionCause().name().toLowerCase()), new Action(Tr._("close"),
																																																																															Dialog.CLOSE_EVENT)));
										Game.client.resetRejection();
										return;
									}
									try {
										Thread.sleep(100);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								}
								Game.currentGame.getActiveScene().unlockScene();
								Game.currentGame.removeActiveScene();
							}
						}.start();
					}
				}));
				name.setWidth(dialog.getWidth() - 50);
				dialog.addComponent(name);
				Game.currentGame.addScene(dialog);
			}
		});
		add(rename);
		
		ImageButton kick = new ImageButton(width - 124, 36, 45, 45);
		kick.setTexture("/graphics/textures/ui/Logout.png");
		kick.disabledColor = IGuiElement.gray;
		kick.setClickEvent(new IGuiEvent() {
			@Override
			public void trigger() {
				Dialog dialog = new Dialog(Tr._("kick"), Tr._("kickquestion").replace("%player%", p.getUsername()), new Action(Tr._("cancel"), Dialog.CLOSE_EVENT),
																		new Action(Tr._("ok"), new IGuiEvent() {
																			@Override
																			public void trigger() {
																				try {
																					Game.client.sendPacket(new Packet1Disconnect(p.getUsername(), "mp.reason.kicked"));
																					Game.currentGame.removeActiveScene();
																				} catch (IOException e) {
																					e.printStackTrace();
																				}
																			}
																		}));
				Game.currentGame.addScene(dialog);
			}
		});
		add(kick);
		
		int nw = 190;
		float ratio = TextButton.HEIGHT / (float) TextButton.WIDTH;
		final TextButton ready = new TextButton(width - 128 - nw / 2, 15, Tr._("ready"));
		ready.setWidth(nw);
		ready.setHeight((int) (ratio * nw));
		ready.setToggleMode(true);
		ready.setClickEvent(new IGuiEvent() {
			@Override
			public void trigger() {
				try {
					p.setReady(ready.isActive());
					Game.client.sendPacket(new Packet6Player(p));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		add(ready);
		
		ColorLabel c = new ColorLabel(width - 400, 1, height - 20, p.getColor());
		c.grayOnDisable = false;
		add(c);
	}
}
