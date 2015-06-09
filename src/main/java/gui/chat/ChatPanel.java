package main.java.gui.chat;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.Hashtable;

import javax.swing.JScrollPane;

import main.java.gui.components.ImagePanel;
import main.java.gui.util.Resources;

@SuppressWarnings("serial")
public class ChatPanel extends JScrollPane implements ComponentListener, ChatListener {
	
	// Static 
	
	private static Hashtable<TextAttribute, Object> TEXT_ATTRIBUTES = null;
	private static FontRenderContext FONT_RENDER = null;
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
    	FONT_RENDER = new  FontRenderContext(new AffineTransform(), true, true);
    }
	
	// Properties
	
	private Chat chat = null;
	private Color senderColor = null;
	private BufferedImage buffer = null;
	private Graphics2D graphics = null;
	private ImagePanel imagePanel = null;
	private int chatWidth = 300;
	
	// Constructors
	
	public ChatPanel() {
		super();
		
		Color backColor = new Color(0xFFEDDE);
    	this.setBackground(backColor);
    	this.imagePanel = new ImagePanel();
    	this.imagePanel.setBackground(backColor);
    	this.setViewportView(this.imagePanel);
		this.addComponentListener(this);
		
	}
	
	// Getters
	
	public Chat getChat() {
		return this.chat;
	}
	
	public Color getSenderColor() {
		return this.senderColor;
	}
	
	// Setters
	
	public void setChat(Chat chat) {
		if (this.chat != null) {
			this.chat.removeChatListener(this);
		}
		this.chat = chat;
		if (this.chat != null) {
			this.chat.addChatListener(this);
		}
	}
	
	public void setSenderColor(Color senderColor) {
		this.senderColor = senderColor;
	}
	
	// Buffers
	
	public void reloadBuffer() {
		if (this.chat == null || this.senderColor == null) {
			return;
		}
        
		int height = 1;
		for (int i = 0; i < this.chat.getNumberOfMessages(); i++) {
			String text = chat.getMessageAtIndex(i).getText();
	        AttributedString attrText = new AttributedString(text, TEXT_ATTRIBUTES);
			Rectangle messageRect = this.computeMessageRect(attrText); 
			height += messageRect.height;
		}
		height = Math.max(height, this.getHeight());
		
		this.buffer = new BufferedImage(this.chatWidth, height, BufferedImage.TYPE_INT_RGB);
		this.graphics = this.buffer.createGraphics();

        this.graphics.setColor(new Color(0xFFEDDE));
        this.graphics.fillRect(0, 0, this.chatWidth, height);
		
        int originY = 0;
		for (int i = 0; i < chat.getNumberOfMessages() ; i++) {
			ChatMessage message = this.chat.getMessageAtIndex(i);
			String text = message.getText();
			
	        AttributedString attrText = new AttributedString(text, TEXT_ATTRIBUTES);
	        Color color = message.getSenderColor();
	        BufferedImage image = this.getAvatarForColor(color);
	        boolean isSender = message.getSenderColor().equals(this.senderColor);
	        color = new Color(color.getRed(), color.getBlue(), color.getGreen(), 80);
	        
	        Rectangle rect = this.computeMessageRect(attrText);
	        rect.y += originY;
	        originY += rect.height;
	        
	        this.drawBubble(color, rect, isSender);
	        this.drawText(attrText, rect, isSender);
	        this.drawAvatar(image, rect, isSender);
		}
		
		Dimension bufferSize = new Dimension(buffer.getWidth(), buffer.getHeight());
		this.imagePanel.setImage(this.buffer);
		this.imagePanel.setSize(bufferSize);
		this.imagePanel.setPreferredSize(bufferSize);
	}
	
	// Drawings  
    
    private Rectangle computeMessageRect(AttributedString attrText) {
    	AttributedCharacterIterator paragraph = attrText.getIterator();
    	int paragraphStart = paragraph.getBeginIndex();
    	int paragraphEnd = paragraph.getEndIndex();
    	LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(paragraph, FONT_RENDER);
    	
        float drawPosY = 0;
        float width = this.chatWidth - AVATAR_SIZE - 2 * (BUBBLE_MARGIN_X + TEXT_MARGIN_X);
        
        lineMeasurer.setPosition(paragraphStart);
        while (lineMeasurer.getPosition() < paragraphEnd) {
        	TextLayout layout = lineMeasurer.nextLayout(width);
            drawPosY += layout.getAscent() + layout.getDescent() + layout.getLeading();
        }
        
    	Rectangle rect = new Rectangle();
    	rect.width = this.chatWidth;
    	rect.height = (int) (drawPosY + 2 * (BUBBLE_MARGIN_Y + TEXT_MARGIN_Y));
        return rect;
    }
    
    public void drawAvatar(BufferedImage image, Rectangle rect, Boolean isSender) {
    	Graphics2D g2d = this.graphics;
    	if (isSender) {
    		g2d.drawImage(image,
    					  rect.x + rect.width - AVATAR_SIZE - BUBBLE_MARGIN_X,
    			          rect.y + BUBBLE_MARGIN_Y,
    			          AVATAR_SIZE,
    			          AVATAR_SIZE,
    			          null);
    	} else {
    		g2d.drawImage(image, 
    				      rect.x + BUBBLE_MARGIN_X,
    			          rect.y + BUBBLE_MARGIN_Y,
    			          AVATAR_SIZE,
    			          AVATAR_SIZE,
    			          null);
    	}
    }
    
    public void drawBubble(Color color, Rectangle rect, boolean isSender) {
    	Graphics2D g2d = this.graphics;
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
    
    public void drawText(AttributedString text, Rectangle rect, boolean isSender) {
    	AttributedCharacterIterator paragraph = text.getIterator();
    	int paragraphStart = paragraph.getBeginIndex();
    	int paragraphEnd = paragraph.getEndIndex();
    	LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(paragraph, FONT_RENDER);
    	
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
            layout.draw(this.graphics, x + drawPosX, y + drawPosY);
            drawPosY += layout.getDescent() + layout.getLeading();
        }
    }

    private BufferedImage getAvatarForColor(Color avatarColor) {
    	if (avatarColor == null) {
			return null;
		} else if (avatarColor.equals(Color.RED)) {
			return Resources.imageNamed("avatar_red");
		} else if (avatarColor.equals(Color.YELLOW)) {
			return Resources.imageNamed("avatar_orange");
		} else if (avatarColor.equals(Color.GREEN)) {
			return Resources.imageNamed("avatar_green");
		} else if (avatarColor.equals(Color.BLUE)) {
			return Resources.imageNamed("avatar_blue");
		} else if (avatarColor.equals(Color.WHITE)) {
			return Resources.imageNamed("avatar_white");
		} else if (avatarColor.equals(Color.BLACK)) {
			return Resources.imageNamed("avatar_black");
		} else {
			return Resources.imageNamed("avatar_not_found");
		}
    }
    
    // ComponentListener
    
	public void componentResized(ComponentEvent e) {
		this.chatWidth = Math.min(300, this.getWidth() - 30);
    	this.reloadBuffer();
	}

	public void componentMoved(ComponentEvent e) {
		this.chatWidth = Math.min(300, this.getWidth() - 30);
    	this.reloadBuffer();
	}
	
	public void componentShown(ComponentEvent e) {}
	public void componentHidden(ComponentEvent e) {}

	// ChatListener
	
	public void chatMessageDidAdd(Chat chat, ChatMessage message) {
		this.reloadBuffer();
		Point p = new Point(0, this.buffer.getHeight());
        this.getViewport().setViewPosition(p);
	}
    
}
