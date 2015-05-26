package main.java.gui.board;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.Hashtable;

import main.java.gui.components.Panel;

@SuppressWarnings("serial")
public class ChatPanel extends Panel {
	
	// Static 
	
	private static Hashtable<TextAttribute, Object> attributes = null;
	 
    static {
        ChatPanel.attributes = new Hashtable<TextAttribute, Object>();
        attributes.put(TextAttribute.FAMILY, "Serif");
        attributes.put(TextAttribute.SIZE, new Float(18.0));
        attributes.put(TextAttribute.FOREGROUND, Color.BLACK);
    }
	
	// Properties
	
	private Chat chat;
	
	// Constructors
	
	public ChatPanel() {
		super();
		
		this.chat = new Chat();
		this.chat.addMessage(null, "Test1", "Message 1");
		this.chat.addMessage(null, "Test2", "Message 2");
		this.chat.addMessage(null, "Test3", "Message 3");
		this.chat.addMessage(null, "Test4", "Message 4");
	}
	
	// Setters / getters
	
	public Chat getChat() {
		return this.chat;
	}
	
	public void setChat(Chat chat) {
		this.chat = chat;
		this.revalidate();
	}
	
	// Drawings  
 
    public void paintComponent(Graphics g) {
    	super.paintComponent(g);
    	
        Graphics2D g2d = (Graphics2D)g;
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
 
        if (this.chat == null) {
        	return;
        }

        Rectangle textRect = new Rectangle();
        int nbMessages = this.chat.getNumberOfMessages();
		for (int i = nbMessages-1; i >= 0; i--) {
			ChatMessage message = this.chat.getMessageAtIndex(i);
			String text = message.getText();
			
	        AttributedString attrText = new AttributedString(text, ChatPanel.attributes);
	        textRect.x = 40;
	        textRect.width = this.getWidth() - 40;
	        textRect.height = (int)this.computeTextHeight(g2d, attrText, textRect.width);
	        
	        this.drawBubble(g2d, textRect);
	        this.drawText(g2d, attrText, textRect);

	        textRect.y += textRect.height + 4;
		}
        
    }
    
    public float computeTextHeight(Graphics2D g2d, AttributedString text, float width) {
    	AttributedCharacterIterator paragraph = text.getIterator();
    	int paragraphStart = paragraph.getBeginIndex();
    	int paragraphEnd = paragraph.getEndIndex();
    	FontRenderContext frc = g2d.getFontRenderContext();
    	LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(paragraph, frc);
    	
        float drawPosY = 0;
        lineMeasurer.setPosition(paragraphStart);
        while (lineMeasurer.getPosition() < paragraphEnd) {
            TextLayout layout = lineMeasurer.nextLayout(width);
            drawPosY += layout.getAscent() + layout.getDescent() + layout.getLeading();
        }
        
        return drawPosY;
    }
    
    public void drawBubble(Graphics2D g2d, Rectangle rect) {
    	g2d.setColor(Color.YELLOW);
    	g2d.fillRect(rect.x, rect.y, rect.width, rect.height);
    }
    
    public void drawText(Graphics2D g2d, AttributedString text, Rectangle rect) {
    	AttributedCharacterIterator paragraph = text.getIterator();
    	int paragraphStart = paragraph.getBeginIndex();
    	int paragraphEnd = paragraph.getEndIndex();
    	FontRenderContext frc = g2d.getFontRenderContext();
    	LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(paragraph, frc);
 
        float drawPosY = 0;
        lineMeasurer.setPosition(paragraphStart);
        while (lineMeasurer.getPosition() < paragraphEnd) {
            TextLayout layout = lineMeasurer.nextLayout(rect.width);
            float drawPosX = layout.isLeftToRight() ? 0 : rect.width - layout.getAdvance();
            drawPosY += layout.getAscent();
            
            layout.draw(g2d, rect.x + drawPosX, rect.y + drawPosY);
            drawPosY += layout.getDescent() + layout.getLeading();
        }
    }
    
}
