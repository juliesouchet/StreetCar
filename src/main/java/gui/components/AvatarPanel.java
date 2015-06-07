package main.java.gui.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.java.gui.util.Resources;

@SuppressWarnings("serial")
public class AvatarPanel extends Panel{

	Color avatarColor = null;
	BufferedImage bufferedImage;
	
	public AvatarPanel() {
		super();
	}
	
	public AvatarPanel(Color avatarColor) {
		this();
		this.avatarColor = avatarColor;
		chooseAvatar();
	}
	
	public void chooseAvatar() {
		if (avatarColor.equals(Color.RED)) {
			bufferedImage = Resources.imageNamed("avatar_red");
		} else if (avatarColor.equals(Color.YELLOW)) {
			bufferedImage = Resources.imageNamed("avatar_orange");
		} else if (avatarColor.equals(Color.GREEN)) {
			bufferedImage = Resources.imageNamed("avatar_green");
		} else if (avatarColor.equals(Color.BLUE)) {
			bufferedImage = Resources.imageNamed("avatar_blue");
		} else if (avatarColor.equals(Color.WHITE)) {
			bufferedImage = Resources.imageNamed("avatar_white");
		} else if (avatarColor.equals(Color.BLACK)) {
			bufferedImage = Resources.imageNamed("avatar_black");
		} else {
			bufferedImage = Resources.imageNamed("avatar_not_found");
		}		
	}
	
	public Color getColor() {
		return this.avatarColor;
	}
	
	public void setColor(Color avatarColor) {
		this.avatarColor = avatarColor;
		chooseAvatar();
		this.repaint();
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (bufferedImage != null) {
			g.drawImage(bufferedImage, 0, 0, this.getWidth(), this.getHeight(), null);
		}		
	}
	
	
}
