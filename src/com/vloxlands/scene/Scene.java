package com.vloxlands.scene;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.json.JSONObject;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import com.vloxlands.game.Game;
import com.vloxlands.settings.CFG;
import com.vloxlands.settings.Settings;
import com.vloxlands.settings.Tr;
import com.vloxlands.ui.Action;
import com.vloxlands.ui.ClickableGui;
import com.vloxlands.ui.Container;
import com.vloxlands.ui.Dialog;
import com.vloxlands.ui.IGuiElement;
import com.vloxlands.ui.IGuiEvent;
import com.vloxlands.ui.ImageButton;
import com.vloxlands.ui.InputField;
import com.vloxlands.ui.Label;
import com.vloxlands.ui.TextButton;
import com.vloxlands.util.FontAssistant;
import com.vloxlands.util.NetworkAssistant;
import com.vloxlands.util.RenderAssistant;

public abstract class Scene
{
	public ArrayList<IGuiElement> content = new ArrayList<>();
	private boolean wasButton0Down;
	private boolean wasButton1Down;
	private boolean wasButton2Down;
	public boolean initialized = false;
	
	public abstract void init();
	
	boolean uiActive = true, worldActive = true;
	
	// -- title -- //
	boolean titled = false;
	
	// -- userZone -- //
	final int SPEED = 10;
	
	static Container userZone, userZoneContent;
	static Label user, username;
	static ImageButton friendList, logout;
	static int userZoneWidth, defaultUserZoneHeight, userZoneWantedHeight;
	static int selectedZoneButton;
	static boolean collapse;
	
	boolean showUserZone = false;
	
	protected void setBackground()
	{
		Label bg = new Label(0, 0, Display.getWidth(), Display.getHeight(), "");
		bg.setZIndex(-1);
		bg.setTexture("/graphics/textures/ui/paper.png");
		bg.stackTexture = true;
		bg.texW = 512;
		bg.texH = 512;
		content.add(bg);
	}
	
	public void initUserZone()
	{
		
		// NetworkAssistant.pullUserLogo(CFG.USERNAME);
		/* new URL("http://dakror.de/vloxlands/api/userlogo.php?user=" + name) */
		collapse = false;
		user = new Label(15, 15, 70, 70, "");
		user.setZIndex(4);
		user.setTexture(CFG.USERNAME + "_LOGO");
		username = new Label(100, 10, 10, 30, CFG.USERNAME, false);
		username.setZIndex(4);
		userZoneWidth = 140 + FontAssistant.getFont(username.font).getWidth(CFG.USERNAME);
		userZone = new Container(0, 0, (userZoneWidth > TextButton.WIDTH) ? userZoneWidth : TextButton.WIDTH, 100, true);
		userZone.setZIndex(3);
		defaultUserZoneHeight = userZoneWantedHeight = 100;
		selectedZoneButton = -1;
		userZoneContent = new Container(0, 90, userZone.getWidth(), 0);
		userZoneContent.border = false;
		userZoneContent.setWantsRender(false);
		userZone.add(userZoneContent);
		friendList = new ImageButton(95, 53, 32, 32);
		friendList.setZIndex(4);
		friendList.setTexture("/graphics/textures/ui/FriendList.png");
		friendList.setClickEvent(new IGuiEvent()
		{
			@Override
			public void trigger()
			{
				if (selectedZoneButton == 0)
				{
					collapse = true;
					userZoneWantedHeight = defaultUserZoneHeight;
				}
				else
				{
					userZoneContent.components.clear();
					TextButton add = new TextButton(userZoneContent.getWidth() / 2, 0, new Action(Tr._("addfriend"), new IGuiEvent()
					{
						@Override
						public void trigger()
						{
							final InputField input = new InputField(0, 0, 0, "", Tr._("username"));
							Dialog addDialog = new Dialog(Tr._("addfriend2"), Tr._("addfriend3") + ".", new Action(Tr._("abort"), Dialog.CLOSE_EVENT), new Action(Tr._("add"), new IGuiEvent()
							{
								@Override
								public void trigger()
								{
									if (input.getText().length() == 0)
									{
										Game.currentGame.addScene(new Dialog(Tr._("error"), Tr._("searchemtpy") + ".", new Action(Tr._("close"), Dialog.CLOSE_EVENT)));
										return;
									}
									if (input.getText().equals(CFG.USERNAME))
									{
										Game.currentGame.addScene(new Dialog(Tr._("error"), Tr._("searchself"), new Action(Tr._("close"), Dialog.CLOSE_EVENT)));
										return;
									}
									try
									{
										JSONObject data = NetworkAssistant.searchFriend(input.getText());
										if (data.length() == 0) Game.currentGame.addScene(new Dialog(Tr._("error"), Tr._("searcherror").replace("%name%", input.getText()) + ".", new Action(Tr._("close"), Dialog.CLOSE_EVENT)));
										else if (data.length() == 1) Game.currentGame.addScene(new Dialog(Tr._("error"), Tr._("searchexists").replace("%name%", input.getText()) + ".", new Action(Tr._("close"), Dialog.CLOSE_EVENT)));
										else
										{
											String response = NetworkAssistant.addFriend(data.getInt("ID"));
											switch (response)
											{
												case "1":
												{
													break;
												}
												case "-1":
												{
													Game.currentGame.addScene(new Dialog(Tr._("error"), Tr._("searchexists").replace("%name%", input.getText()) + ".", new Action(Tr._("close"), Dialog.CLOSE_EVENT)));
													break;
												}
												case "0":
												default:
													Game.currentGame.addScene(new Dialog(Tr._("error"), Tr._("errno") + " " + ((response.length() > 1) ? response : ""), new Action(Tr._("close"), Dialog.CLOSE_EVENT)));
													break;
											}
										}
									}
									catch (Exception e)
									{
										Game.currentGame.addScene(new Dialog(Tr._("error"), Tr._("errno") + " " + e.getClass().getName() + ": " + e.getMessage(), new Action(Tr._("close"), Dialog.CLOSE_EVENT)));
									}
								}
							}));
							input.setWidth(addDialog.getWidth() - 50);
							addDialog.addComponent(input);
							Game.currentGame.addScene(addDialog);
						}
					}));
					add.setWidth(userZoneContent.getWidth() - 30);
					add.setHeight((int) (TextButton.HEIGHT / (float) TextButton.WIDTH * add.getWidth()));
					
					userZoneContent.add(add);
					userZoneContent.pack(false, true);
					selectedZoneButton = 0;
					userZoneWantedHeight = userZoneContent.getHeight() + 100;
				}
			}
		});
		logout = new ImageButton(userZoneWidth - 10, 53, 32, 32);
		logout.setZIndex(4);
		logout.setTexture("/graphics/textures/ui/Logout.png");
		logout.setClickEvent(new IGuiEvent()
		{
			@Override
			public void trigger()
			{
				if (selectedZoneButton == 0)
				{
					collapse = true;
					userZoneWantedHeight = defaultUserZoneHeight;
				}
				else
				{
					Game.currentGame.addScene(new Dialog(Tr._("logout") + "?", Tr._("logoutquestion"), new Action(Tr._("abort"), Dialog.CLOSE_EVENT), new Action(Tr._("yes"), new IGuiEvent()
					{
						@Override
						public void trigger()
						{
							CFG.PASSWORD = "";
							Settings.saveSettings();
							Game.currentGame.setScene(new SceneLogin());
						}
					})));
					selectedZoneButton = 1;
					userZoneWantedHeight = defaultUserZoneHeight;
				}
			}
		});
	}
	
	protected void setUserZone()
	{
		if (Game.client == null) Game.initMultiplayer();
		// if (!CFG.INTERNET) return;
		//
		// if (user == null) initUserZone();
		//
		// showUserZone = true;
		// content.add(user);
		// content.add(username);
		// content.add(userZone);
		// content.add(friendList);
		// content.add(logout);
	}
	
	protected void setTitle(String title)
	{
		Label l = new Label(0, 0, Display.getWidth(), 60, title);
		l.font = l.font.deriveFont(Font.BOLD, 60f);
		content.add(l);
		
		titled = true;
	}
	
	public void onTickContent()
	{
		ArrayList<IGuiElement> sorted = getSortedContent();
		
		for (IGuiElement i : sorted)
			if (i instanceof ClickableGui)
			{
				((ClickableGui) i).onTick();
			}
	}
	
	public void onTick()
	{
		onTickContent();
		
		if (userZone != null && showUserZone)
		{
			float dif = userZoneWantedHeight - userZone.getHeight();
			if (Math.abs(dif) > Math.abs(dif) / SPEED)
			{
				if (dif > 0) userZone.setHeight((int) Math.ceil(userZone.getHeight() + dif / SPEED));
				else userZone.setHeight((int) Math.floor(userZone.getHeight() + dif / SPEED));
				
				if (userZone.getHeight() == defaultUserZoneHeight)
				{
					selectedZoneButton = -1;
					userZoneContent.components.clear();
				}
			}
			else if (Math.abs(dif) != 0)
			{
				userZone.setHeight(userZoneWantedHeight);
			}
		}
	}
	
	public void render()
	{
		renderContent();
		
		if (userZone != null && showUserZone)
		{
			RenderAssistant.renderLine(96, 10, 80, false, false);
			RenderAssistant.renderLine(90, 45, userZoneWidth - 62, true, false);
			glEnable(GL_BLEND);
			RenderAssistant.renderRect(84, 38, 19, 26, 787 / 1024f, 409 / 1024f, 19 / 1024f, 26 / 1024f);
			glDisable(GL_BLEND);
			if (userZone.getHeight() > defaultUserZoneHeight)
			{
				if (selectedZoneButton > -1)
				{
					RenderAssistant.renderLine(10, 83, 72, true, false);
					
					if (selectedZoneButton > 0)
					{
						RenderAssistant.renderLine(85, 83, 13 + selectedZoneButton * 32, true, false);
						glEnable(GL_BLEND);
						RenderAssistant.renderRect(77, 76, 26, 19, 780 / 1024f, 450 / 1024f, 26 / 1024f, 19 / 1024f);
						glDisable(GL_BLEND);
					}
					else
					{
						glEnable(GL_BLEND);
						RenderAssistant.renderRect(77, 76, 19, 19, 982 / 1024f, 498 / 1024f, 19 / 1024f, 19 / 1024f);
						glDisable(GL_BLEND);
					}
					RenderAssistant.renderLine(90 + (selectedZoneButton + 1) * 32, 83, userZoneWidth - (selectedZoneButton + 1) * 32 - 62, true, false);
					glEnable(GL_SCISSOR_TEST);
					glScissor(userZone.getX() + 15, Display.getHeight() - userZone.getY() - userZone.getHeight() + 15, userZone.getWidth() - 30, userZone.getHeight() - 30 - 75);
					
					// render container content
					userZoneContent.render();
					switch (selectedZoneButton)
					{
						case 0: // friendList
						{
							break;
						}
					}
					
					glDisable(GL_SCISSOR_TEST);
				}
			}
		}
		
		if (titled) RenderAssistant.renderLine((showUserZone) ? userZone.getX() + userZoneWidth + 30 : 0, 83, Display.getWidth(), true, true);
	}
	
	public void renderContent()
	{
		ArrayList<IGuiElement> sorted = getSortedContent();
		for (IGuiElement g : sorted)
			if (g.isVisible() && g.wantsRender()) g.render();
	}
	
	public void handleMouse()
	{
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
		
		
		
		if ((!uiActive || !handleMouseGUI(x, y, flag)))
		{
			if ((flag & 1) != 0) resetScene();
			if (worldActive) handleMouseWorld(x, y, flag);
		}
	}
	
	public void resetScene()
	{
		ArrayList<IGuiElement> sorted = getSortedContent();
		Collections.reverse(sorted);
		for (IGuiElement i : sorted)
			if (i instanceof ClickableGui) ((ClickableGui) i).resetElement();
	}
	
	public void handleKeyboard(int key, char chr, boolean down)
	{
		for (IGuiElement iG : content)
		{
			iG.handleKeyboard(key, chr, down);
		}
	}
	
	// not abstract so that implementing won't be forced
	public boolean handleMouseGUI(int posX, int posY, int flag)
	{
		ClickableGui iG = getObjectUnderCursor();
		if (iG != null && iG.isVisible() && iG.isEnabled())
		{
			iG.handleMouse(posX - iG.getX(), posY - iG.getY(), flag);
			return true;
		}
		return false;
	}
	
	private ClickableGui getObjectUnderCursor()
	{
		ArrayList<IGuiElement> sorted = getSortedContent();
		Collections.reverse(sorted);
		for (IGuiElement i : sorted)
			if (i instanceof ClickableGui)
			{
				ClickableGui iG = (ClickableGui) i;
				if (iG.isUnderCursor()) return iG;
			}
		return null;
	}
	
	protected ArrayList<IGuiElement> getSortedContent()
	{
		@SuppressWarnings("unchecked")
		final ArrayList<IGuiElement> sorted = (ArrayList<IGuiElement>) content.clone();
		if (sorted.size() == 0 || sorted.get(0) == null) return new ArrayList<>();
		
		Collections.sort(sorted, new Comparator<IGuiElement>()
		{
			
			@Override
			public int compare(IGuiElement o1, IGuiElement o2)
			{
				return o1.getZIndex() - o2.getZIndex();
			}
		});
		
		return sorted;
	}
	
	// not abstract so that implementing won't be forced
	public void handleMouseWorld(int x, int y, int flag)
	{}
	
	protected void lockScene()
	{
		for (IGuiElement i : content)
			if (i instanceof ClickableGui)
			{
				((ClickableGui) i).setEnabled(false);
			}
	}
	
	protected void unlockScene()
	{
		for (IGuiElement i : content)
			if (i instanceof ClickableGui)
			{
				((ClickableGui) i).setEnabled(true);
			}
	}
}
