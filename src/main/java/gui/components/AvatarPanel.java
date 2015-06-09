package main.java.gui.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
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
		if (avatarColor == null) {
			this.bufferedImage = null;
			this.setBackground(Color.GRAY);
		} else if (avatarColor.equals(Color.RED)) {
			this.bufferedImage = Resources.imageNamed("avatar_red");
			this.setBackground(Color.GRAY);
		} else if (avatarColor.equals(Color.YELLOW)) {
			this.bufferedImage = Resources.imageNamed("avatar_orange");
			this.setBackground(Color.GRAY);
		} else if (avatarColor.equals(Color.GREEN)) {
			this.bufferedImage = Resources.imageNamed("avatar_green");
			this.setBackground(Color.GRAY);
		} else if (avatarColor.equals(Color.BLUE)) {
			this.bufferedImage = Resources.imageNamed("avatar_blue");
			this.setBackground(Color.GRAY);
		} else if (avatarColor.equals(Color.WHITE)) {
			this.bufferedImage = Resources.imageNamed("avatar_white");
			this.setBackground(Color.GRAY);
		} else if (avatarColor.equals(Color.BLACK)) {
			this.bufferedImage = Resources.imageNamed("avatar_black");
			this.setBackground(Color.GRAY);
		} else {
			this.bufferedImage = Resources.imageNamed("avatar_not_found");
			this.setBackground(Color.GRAY);
		}
		this.repaint();
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
			Graphics2D g2d = (Graphics2D)g;
			
			int w, h;
            w = bufferedImage.getWidth();
            h = bufferedImage.getHeight();

            do {
                if (w > this.getWidth()) {
                    w /= 2;
                    if (w < this.getWidth()) {
                        w = this.getWidth();
                    }
                }

                if (h > this.getHeight()) {
                    h /= 2;
                    if (h < getHeight()) {
                        h = getHeight();
                    }
                 }

                BufferedImage tmp = new BufferedImage(Math.max(w, 1), Math.max(h, 1), BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = tmp.createGraphics();
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2.drawImage(bufferedImage, 0, 0, w, h, null);
                g2.dispose();

                bufferedImage = tmp;
            } while (w != this.getWidth() || h != this.getHeight());


            g2d.drawImage(bufferedImage, 0, 0, this.getWidth(), this.getHeight(), null);
		}		
	}
	
	
}
