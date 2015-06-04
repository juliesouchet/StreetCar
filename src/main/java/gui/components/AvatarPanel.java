package main.java.gui.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.java.gui.util.Resources;

@SuppressWarnings("serial")
public class AvatarPanel extends Panel{

	Color avatarColor;
	BufferedImage bufferedImage;
	
	public AvatarPanel(Color avatarColor) {
		super();
		this.avatarColor = avatarColor;
		chooseAvatar();
	}
	
	public void chooseAvatar() {
		if (avatarColor.equals(Color.RED)) {
			bufferedImage = Resources.imageNamed("avatar_red");
		} else if (avatarColor.equals(Color.YELLOW)) {
			bufferedImage = Resources.imageNamed("avatar_yellow");
		} else if (avatarColor.equals(Color.GREEN)) {
			bufferedImage = Resources.imageNamed("avatar_green");
		} else if (avatarColor.equals(Color.BLUE)) {
			bufferedImage = Resources.imageNamed("avatar_blue");
		} else if (avatarColor.equals(Color.WHITE)) {
			bufferedImage = Resources.imageNamed("avatar_white");
		} else if (avatarColor.equals(Color.BLACK)) {
			bufferedImage = Resources.imageNamed("avatar_black");
		} else {
			bufferedImage = Resources.imageNamed("avatar_not_found");;
		}		
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (bufferedImage != null) {
			g.drawImage(bufferedImage, 0, 0, this.getWidth(), this.getHeight(), null);
		}
		
	}
	
	
}
