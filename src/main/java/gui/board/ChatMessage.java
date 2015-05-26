package main.java.gui.board;

import java.awt.image.BufferedImage;

public class ChatMessage {
	
	// Properties
	
	private BufferedImage avatar = null;
	private String sender = null;
	private String text = null;
	
	// Constructors
	
	public ChatMessage(BufferedImage avatar, String sender, String text) {
		this.avatar = avatar;
		this.sender = sender;
		this.text = text;
	}
	
	// Setters / getters
	
	public BufferedImage getAvatarImage() {
		return this.avatar;
	}
	
	public String getSender() {
		return this.sender;
	}
	
	public String getText() {
		return this.text;
	}
	
}
