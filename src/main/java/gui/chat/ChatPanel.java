package main.java.gui.chat;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.Hashtable;

import main.java.gui.components.Panel;

@SuppressWarnings("serial")
public class ChatPanel extends Panel {
	
	// Static 
	
	private static Hashtable<TextAttribute, Object> TEXT_ATTRIBUTES = null;
	private static int AVATAR_SIZE = 30;
	private static int TEXT_MARGIN_X = 10;
	private static int TEXT_MARGIN_Y = 5;
	private static int BUBBLE_MARGIN_X = 10;
	private static int BUBBLE_MARGIN_Y = 4;
	
    static {
    	TEXT_ATTRIBUTES = new Hashtable<TextAttribute, Object>();
    	TEXT_ATTRIBUTES.put(TextAttribute.FAMILY, "Serif");
    	TEXT_ATTRIBUTES.put(TextAttribute.SIZE, new Float(18.0));
    	TEXT_ATTRIBUTES.put(TextAttribute.FOREGROUND, Color.BLACK);
    }
	
	// Properties
	
	private Chat chat = null;
	private String sender = null;
	
	// Constructors
	
	public ChatPanel() {
		super();
		
		this.setBackground(Color.WHITE);
		this.chat = new Chat();
		this.chat.addMessage(null, "Test1", "Lorem ipsum dolor sit amet, consectetur adipiscing elit.");
		this.chat.addMessage(null, "Test2", "Message 2");
		this.chat.addMessage(null, "Test3", "Nunc accumsan tincidunt dui eget hendrerit. Sed eget lacus velit. Donec laoreet venenatis suscipit. Morbi gravida justo nibh, at vestibulum nisl blandit et. Fusce posuere pellentesque nulla nec luctus. Nullam molestie condimentum dui, quis venenatis leo iaculis vel.");
		this.chat.addMessage(null, "Test4", "Nullam a orci nec enim suscipit consequat. Nunc a malesuada neque, sit amet facilisis est. Maecenas rhoncus lobortis libero sed posuere. ");
		this.sender = "Test3";
	}
	
	// Getters
	
	public Chat getChat() {
		return this.chat;
	}
	
	public String getSender() {
		return this.sender;
	}
	
	// Setters
	
	public void setChat(Chat chat) {
		this.chat = chat;
		this.revalidate();
	}
	
	public void setSender(String sender) {
		this.sender = sender;
		this.revalidate();
	}
	
	// Drawings  
 
    public void paintComponent(Graphics g) {
    	super.paintComponent(g);
    	
        if (this.chat == null)  return;

        Graphics2D g2d = (Graphics2D)g;
        int originY = 0;
		for (int i = 0; i < chat.getNumberOfMessages() ; i++) {
			ChatMessage message = this.chat.getMessageAtIndex(i);
			String text = message.getText();
			
	        AttributedString attrText = new AttributedString(text, TEXT_ATTRIBUTES);
	        BufferedImage image = message.getAvatarImage();
	        Color color = Color.RED;
	        boolean isSender = message.getSender().equals(this.sender);
	        
	        Rectangle rect = this.computeMessageRect(g2d, attrText);
	        rect.y += originY;
	        originY += rect.height;
	        
	        this.drawBubble(g2d, color, rect, isSender);
	        this.drawText(g2d, attrText, rect, isSender);
	        this.drawAvatar(g2d, image, rect, isSender);
		}
    }
    
    private Rectangle computeMessageRect(Graphics2D g2d, AttributedString attrText) {
    	AttributedCharacterIterator paragraph = attrText.getIterator();
    	int paragraphStart = paragraph.getBeginIndex();
    	int paragraphEnd = paragraph.getEndIndex();
    	FontRenderContext frc = g2d.getFontRenderContext();
    	LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(paragraph, frc);
    	
        float drawPosY = 0;
        float width = this.getWidth() - AVATAR_SIZE - 2 * (BUBBLE_MARGIN_X + TEXT_MARGIN_X);
        
        lineMeasurer.setPosition(paragraphStart);
        while (lineMeasurer.getPosition() < paragraphEnd) {
        	TextLayout layout = lineMeasurer.nextLayout(width);
            drawPosY += layout.getAscent() + layout.getDescent() + layout.getLeading();
        }
        
    	Rectangle rect = new Rectangle();
    	rect.width = this.getWidth();
    	rect.height = (int) (drawPosY + 2 * (BUBBLE_MARGIN_Y + TEXT_MARGIN_Y));
        return rect;
    }
    
    public void drawAvatar(Graphics2D g2d, BufferedImage image, Rectangle rect, Boolean isSender) {
    	if (isSender) {
    		g2d.setColor(Color.BLUE);
    		g2d.fillRect(rect.x + rect.width - AVATAR_SIZE - BUBBLE_MARGIN_X,
    			         rect.y + BUBBLE_MARGIN_Y,
    			         AVATAR_SIZE,
    			         AVATAR_SIZE);
    	} else {
    		g2d.setColor(Color.BLUE);
    		g2d.fillRect(rect.x + BUBBLE_MARGIN_X,
    			         rect.y + BUBBLE_MARGIN_Y,
    			         AVATAR_SIZE,
    			         AVATAR_SIZE);
    	}
    }
    
    public void drawBubble(Graphics2D g2d, Color color, Rectangle rect, boolean isSender) {
    	GeneralPath path =  new GeneralPath(GeneralPath.WIND_EVEN_ODD);
    	int x, y;
    	if (isSender) {
    		x = rect.x + BUBBLE_MARGIN_X;
    		y = rect.y + BUBBLE_MARGIN_Y;
    		path.moveTo(x, y);
    		x = rect.x + rect.width - 2 * BUBBLE_MARGIN_X - AVATAR_SIZE;
    		path.lineTo(x, y);
    		y = rect.y + rect.height - BUBBLE_MARGIN_Y;
    		path.lineTo(x, y);
    		x = rect.x + BUBBLE_MARGIN_X;
    		path.lineTo(x, y);
    		path.closePath();
    	} else {
    		x = rect.x + AVATAR_SIZE + 2 * BUBBLE_MARGIN_X;
    		y = rect.y + BUBBLE_MARGIN_Y;
    		path.moveTo(x, y);
    		x = rect.x + rect.width - BUBBLE_MARGIN_X;
    		path.lineTo(x, y);
    		y = rect.y + rect.height - BUBBLE_MARGIN_Y;
    		path.lineTo(x, y);
    		x = rect.x + AVATAR_SIZE + 2 * BUBBLE_MARGIN_X;
    		path.lineTo(x, y);
    		path.closePath();
    	}
    	
		g2d.setColor(color);
    	g2d.fill(path);
    }
    
    public void drawText(Graphics2D g2d, AttributedString text, Rectangle rect, boolean isSender) {
    	AttributedCharacterIterator paragraph = text.getIterator();
    	int paragraphStart = paragraph.getBeginIndex();
    	int paragraphEnd = paragraph.getEndIndex();
    	FontRenderContext frc = g2d.getFontRenderContext();
    	LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(paragraph, frc);
    	
    	int x, y, width;
    	if (isSender) {
       		x = rect.x + BUBBLE_MARGIN_X + TEXT_MARGIN_X;
    	} else {
       		x = rect.x + AVATAR_SIZE + 2 * BUBBLE_MARGIN_X + TEXT_MARGIN_X;
    	}
   		y = rect.y + BUBBLE_MARGIN_Y + TEXT_MARGIN_Y;
    	width = rect.width - AVATAR_SIZE - 2 * (BUBBLE_MARGIN_X + TEXT_MARGIN_X);
    	
        float drawPosY = 0;
        lineMeasurer.setPosition(paragraphStart);
        while (lineMeasurer.getPosition() < paragraphEnd) {
        	TextLayout layout = lineMeasurer.nextLayout(width);
            float drawPosX = layout.isLeftToRight() ? 0 : width - layout.getAdvance();
            drawPosY += layout.getAscent();
            layout.draw(g2d, x + drawPosX, y + drawPosY);
            drawPosY += layout.getDescent() + layout.getLeading();
        }
    }
    
}
