package main.java.gui.board;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.Hashtable;

import main.java.data.Tile;
import main.java.gui.util.Resources;

public class TileImage {

	// Private

	static private Hashtable<String, BufferedImage> tilesHashTable = null;

	static {
		tilesHashTable = new Hashtable<String, BufferedImage>();
	}

	// Public

	public static BufferedImage getImage(Tile tile) {
		String tileID = tile.getTileID();
		BufferedImage tileImage = null;

		tileImage = tilesHashTable.get(tileID);
		if (tileImage != null) {
			return tileImage;
		}

		tileImage = Resources.imageNamed(tileID);
		if (tileImage != null) { 
			tilesHashTable.put(tileID, tileImage);
			return tileImage;
		}

		return tileImage;
	}

	public static BufferedImage getRotatedImage(Tile tile) {
		BufferedImage image = TileImage.getImage(tile);

		AffineTransform at = new AffineTransform();
		at.translate(image.getWidth() / 2, image.getHeight() / 2);
		at.rotate(Math.toRadians(tile.getTileDirection().getVal()*90));
		at.translate(-image.getWidth() / 2, -image.getHeight() / 2);

		AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		return op.filter(image, null);
	}

	public static void drawTile(Graphics2D g2d, Tile tile, int x, int y, int width) {
		BufferedImage image = TileImage.getImage(tile);

		AffineTransform at = new AffineTransform();
		at.translate(x + width / 2, y + width / 2);
		at.rotate(Math.toRadians(tile.getTileDirection().getVal()*90));
		at.scale((double)width / (double)image.getWidth(),
				(double)width / (double)image.getHeight());
		at.translate(-image.getWidth() / 2, -image.getHeight() / 2);
		g2d.drawImage(image, at, null);

		if (tile.isStop()) {
			int stopWidth = (int) (0.5 * width);
			BufferedImage bufferedImage;
			bufferedImage = Resources.imageNamed("stop");
			if (bufferedImage != null) {

				if (bufferedImage != null) {
					int w, h;
					w = bufferedImage.getWidth();
					h = bufferedImage.getHeight();
					do {
						if (w > stopWidth) {
							w /= 2;
							if (w < stopWidth) {
								w = stopWidth;
							}
						}
						if (h > stopWidth) {
							h /= 2;
							if (h < stopWidth) {
								h = stopWidth;
							}
						}
						BufferedImage tmp = new BufferedImage(Math.max(w, 1), Math.max(h, 1), BufferedImage.TYPE_INT_ARGB);
						Graphics2D g2 = tmp.createGraphics();
						g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
						g2.drawImage(bufferedImage, 0, 0, w, h, null);
						g2.dispose();
						bufferedImage = tmp;
					} while (w != stopWidth || h != stopWidth);

					g2d.drawImage(bufferedImage, x + (width-stopWidth)/2, y + (width-stopWidth)/2, stopWidth, stopWidth, null);
				}
			}



		}
		/*g2d.setColor(Color.RED);
			g2d.fillOval(x + (width-stopWidth)/2, y + (width-stopWidth)/2, stopWidth, stopWidth);

			g2d.setColor(Color.WHITE);
	        FontMetrics fm = g2d.getFontMetrics();
	        Rectangle2D r = fm.getStringBounds("S", g2d);
	        int textX = (width - (int) r.getWidth()) / 2;
	        int textY = (width - (int) r.getHeight()) / 2 + fm.getAscent();
	        g2d.drawString("S", x+textX, y+textY);*/

	}
}
