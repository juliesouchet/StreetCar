package main.java.player;

import javax.swing.SwingUtilities;

import test.java.player.TestIHM;





public class Main implements Runnable
{
	public static void main(String[] args)
	{
		try					{SwingUtilities.invokeLater(new Main());}
		catch(Exception e)	{e.printStackTrace(); System.exit(0);}
	}
	public void run()
	{
		new TestIHM();
	}

}