package com.vloxlands.ui;

/**
 * @author Dakror
 */
public class Action {
	String text;
	IGuiEvent event;
	
	public Action(String text, IGuiEvent event) {
		this.text = text;
		this.event = event;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public IGuiEvent getEvent() {
		return event;
	}
	
	public void setEvent(IGuiEvent event) {
		this.event = event;
	}
}
