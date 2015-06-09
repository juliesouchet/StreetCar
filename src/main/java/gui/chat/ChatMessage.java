package main.java.gui.chat;

import java.awt.Color;

public class ChatMessage {
	
	// Properties
	
	private Color senderColor = null;
	private String text = null;
	
	// Constructors
	
	public ChatMessage(Color senderColor, String text) {
		this.senderColor = senderColor;
		this.text = text;
	}
	
	// Setters / getters
	
	public Color getSenderColor() {
		return this.senderColor;
	}
	
	public String getText() {
		return this.text;
	}
	
}
