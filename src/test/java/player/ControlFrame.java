package test.java.player;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import main.java.player.PlayerIHM;






public class ControlFrame
{
	// Attributes
	private static final int	width	= 100;
	private static final int	Height	= 200;

	private JFrame				frame;
	private PlayerIHM			playerIHM;
	// Builder
	public ControlFrame(PlayerIHM playerIHM)
	{
		this.playerIHM = playerIHM;

		this.frame	= new JFrame("BLABLABLA");
		this.frame.setSize(width, Height);
		this.frame.setLayout(new GridLayout(2, 1));
		JButton drawTileButton = new JButton("Draw Tile");
		JButton validateButton = new JButton("Validate");
		drawTileButton.addActionListener(new DrawTileListener());
		validateButton.addActionListener(new ValidateListener());
		this.frame.add(drawTileButton);
		this.frame.add(validateButton);

		this.frame.setVisible(true);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	
	
	
	
	private class DrawTileListener implements ActionListener
	{
		public void actionPerformed(ActionEvent arg0)
		{
			try	{playerIHM.drawTile(1);}
			catch (Exception e){e.printStackTrace();}
		}
	}
	private class ValidateListener implements ActionListener
	{
		public void actionPerformed(ActionEvent arg0)
		{
			try	{playerIHM.validate();}
			catch (Exception e){e.printStackTrace();}
		}
	}
}