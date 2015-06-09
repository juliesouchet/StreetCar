package main.java.gui.components;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import main.java.gui.util.Resources;

@SuppressWarnings("serial")
public class LinePanel extends Panel {

	BufferedImage bufferedImage;

	public LinePanel() {
		super();
	}

	public LinePanel(BufferedImage bufferedImage) {
		this();
		this.bufferedImage = bufferedImage;
	}

	public BufferedImage getBufferedImage() {
		return this.bufferedImage;
	}

	public void setBufferedImage(BufferedImage bufferedImage) {
		this.bufferedImage = bufferedImage;
		this.repaint();
	}
	
	public void setLineNumber(int lineNumber) {
		BufferedImage image = Resources.imageNamed("line" + lineNumber);
		if (image == null) {
			image = Resources.imageNamed("line_hidden");
		}
		this.setBufferedImage(image);
	}

	protected void paintComponent(Graphics g) {
		if (bufferedImage != null) {
			//g.drawImage(bufferedImage, 0, 0, this.getWidth(), this.getHeight(), null);
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
}
