package main.java.gui.menus;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import main.java.data.Data;
import main.java.gui.application.GameController;
import main.java.gui.components.Button;
import main.java.gui.components.ImagePanel;
import main.java.gui.util.Resources;
import main.java.player.PlayerIHM;

@SuppressWarnings("serial")
public class RulesMenuPanel extends MenuPanel {

	// Properties
	
	private ImagePanel imagePanel;
	private Button previousImageButton;
	private Button nextImageButton;
	private ArrayList<BufferedImage> ruleImages;
	private int currentImageIndex;
	
	// Constructors
	
	public RulesMenuPanel(GameController gc) {
		super(gc);
		this.setupPanel();
		this.setupImagePanel();
		this.setupButtons();
		this.loadImages();
	}
	
	private void setupPanel() {
    	this.setSize(new Dimension(700, 500));
    	this.setMenuTitle("Rules", null);
	}
	
	private void setupImagePanel() {
		this.imagePanel = new ImagePanel();
		this.imagePanel.setBounds(80, 60, 540, 340);
		this.add(this.imagePanel);
	}
	
	private void setupButtons() {
		this.previousImageButton = new Button("Previous", null);
		this.previousImageButton.addAction(this, "previousImage");
		this.previousImageButton.setBounds(50, 430, 150, 40);
    	this.add(this.previousImageButton);
    	
    	this.nextImageButton = new Button("Next", null);
    	this.nextImageButton.addAction(this, "nextImage");
    	this.nextImageButton.setBounds(500, 430, 150, 40);
    	this.add(this.nextImageButton);
    	
		Button cancelButton = new Button("Back to main menu", null);
		cancelButton.addAction(this, "leaveGame");
		cancelButton.setBounds(275, 430, 150, 40);
    	this.add(cancelButton);
	}
	
	private void loadImages() {
		this.ruleImages = new ArrayList<BufferedImage>();
		for (int i = 1; ; i++) {
			BufferedImage image = Resources.localizedImageNamed("rules_" + i);
			if (image != null) {
				this.ruleImages.add(image);
			} else {
				break;
			}
		}
		this.imagePanel.setImage(this.ruleImages.get(0));
	}
	
	// Setters / getter
	
	public int getImagesCount() {
		return this.ruleImages.size();
	}
	
	public BufferedImage getCurrentImage() {
		return this.ruleImages.get(this.currentImageIndex);
	}
	
	public int getCurrentImageIndex() {
		return this.currentImageIndex;
	}
	
	public void setCurrentImageIndex(int index) {
		if (index == this.currentImageIndex ||
		    index >= this.ruleImages.size() ||
		    index < 0) {
			return;
		}

		this.currentImageIndex = index;
		this.imagePanel.setImage(this.getCurrentImage());
	}
	
	// Actions
	
	public void previousImage() {
		this.setCurrentImageIndex(this.currentImageIndex-1);
	}
	
	public void nextImage() {
		this.setCurrentImageIndex(this.currentImageIndex+1);
	}
	
	public void leaveGame() {
		GameController mfc = (GameController)this.getFrameController();
		mfc.showWelcomeMenuPanel();
	}

	@Override
	public void refreshMenu(PlayerIHM player, Data data) { }
}
