package main.java.gui.chat;

import java.awt.Color;
import java.util.ArrayList;


public class Chat {
	
	// Properties
	
	private ArrayList<ChatMessage> messages;
	private ArrayList<ChatListener> listeners;
	
	// Constructors
	
	public Chat() {
		this.messages = new ArrayList<ChatMessage>();
		this.listeners = new ArrayList<ChatListener>();
	}
	
	// Setters / getters
	
	public int getNumberOfMessages() {
		return this.messages.size();
	}
	
	public ChatMessage getMessageAtIndex(int index) {
		return this.messages.get(index);
	}
	
	// Manage messages
	
	public void addMessage(ChatMessage message) {
		this.messages.add(message);

		for (ChatListener l : this.listeners) {
			l.chatMessageDidAdd(this, message);
		}
	}
	
	public void addMessage(Color senderColor, String text) {
		ChatMessage message = new ChatMessage(senderColor, text);
		this.addMessage(message);
	}
	
	// Manage listeners
	
	public void addChatListener(ChatListener listener) {
		this.listeners.add(listener);
	}
	
	public void removeChatListener(ChatListener listener) {
		this.listeners.remove(listener);
	}
	
}
