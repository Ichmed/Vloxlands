package com.vloxlands.ui;

import static org.lwjgl.opengl.GL11.glColor3f;

import java.awt.Font;

import org.lwjgl.util.vector.Vector2f;

import com.vloxlands.util.FontAssistant;
import com.vloxlands.util.RenderAssistant;

public class Spinner extends ClickableGui {
	public static final int HEIGHT = 52;
	
	public Font font = FontAssistant.GAMEFONT.deriveFont(Font.BOLD, 30f);
	
	int min, max, value, step;
	GuiRotation rot;
	ArrowButton minus, plus;
	String[] titles;
	
	public Spinner(int x, int y, int size, int min, int max, int val, int step, GuiRotation rot) {
		this.x = x;
		this.y = y;
		if (rot == GuiRotation.HORIZONTAL) {
			width = size;
			height = HEIGHT;
		} else {
			width = HEIGHT;
			height = size;
		}
		value = val;
		this.min = min;
		this.max = max;
		this.step = step;
		this.rot = rot;
		
		titles = new String[max - min];
		
		minus = new ArrowButton(x, y + ((rot == GuiRotation.HORIZONTAL) ? 0 : height - ArrowButton.HEIGHT), (rot == GuiRotation.HORIZONTAL) ? ArrowButton.MINUS_HOR
				: ArrowButton.MINUS_VER);
		minus.setClickEvent(new IGuiEvent() {
			
			@Override
			public void trigger() {
				value = (value >= Spinner.this.min + Spinner.this.step) ? value - Spinner.this.step : Spinner.this.min;
				if (clickEvent != null) clickEvent.trigger();
			}
		});
		plus = new ArrowButton(x + ((rot == GuiRotation.HORIZONTAL) ? width - ArrowButton.WIDTH : 0), y, (rot == GuiRotation.HORIZONTAL) ? ArrowButton.PLUS_HOR : ArrowButton.PLUS_VER);
		plus.setClickEvent(new IGuiEvent() {
			
			@Override
			public void trigger() {
				value = (value < Spinner.this.max - Spinner.this.step) ? value + Spinner.this.step : Spinner.this.max - 1;
				if (clickEvent != null) clickEvent.trigger();
			}
		});
	}
	
	public void setArrowButtonTypes(Vector2f minus, Vector2f plus) {
		this.minus.setType(minus);
		this.plus.setType(plus);
	}
	
	public void setTitle(int index, String s) {
		titles[index] = s;
	}
	
	public void setTitles(String... strings) {
		if (strings.length == titles.length) titles = strings;
	}
	
	@Override
	public void onTick() {
		minus.onTick();
		if (value == min) minus.setEnabled(false);
		else minus.setEnabled(enabled);
		
		plus.onTick();
		if (value == max - step) plus.setEnabled(false);
		else plus.setEnabled(enabled);
	}
	
	@Override
	public void handleMouse(int posX, int posY, int flag) {
		if (minus.isUnderCursor()) minus.handleMouse(posX, posY, flag);
		if (plus.isUnderCursor()) plus.handleMouse(posX, posY, flag);
	}
	
	@Override
	public void render() {
		minus.render();
		plus.render();
		
		if (enabled) glColor3f(1, 1, 1);
		else glColor3f(0.5f, 0.5f, 0.5f);
		
		String title = (titles[Math.round(value - min)] != null) ? titles[Math.round(value - min)] : value + "";
		
		if (rot == GuiRotation.HORIZONTAL) {
			int tx = FontAssistant.getFont(font).getWidth(title);
			int mx = width / 2 - tx / 2;
			RenderAssistant.renderText(x + ((width > -1) ? mx : 0), y + ((height > -1) ? height / 4f : 0), title, font);
		} else {
			int tx = FontAssistant.getFont(font).getWidth(title);
			int mx = width / 2 - tx / 2;
			int ty = FontAssistant.getFont(font).getHeight(title);
			int my = height / 2 - ty / 2;
			RenderAssistant.renderText(x + mx / 2, y + ((width > -1) ? my : 0), title, font);
		}
	}
	
	public int getMin() {
		return min;
	}
	
	public int getMax() {
		return max;
	}
	
	public int getStep() {
		return step;
	}
	
	public void setStep(int step) {
		this.step = step;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
}
