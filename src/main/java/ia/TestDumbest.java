package main.java.ia;

import javax.swing.SwingUtilities;

import test.java.player.TestIHM;





public class TestDumbest implements Runnable
{
	public static void main(String[] args)
	{
		try					{SwingUtilities.invokeLater(new TestDumbest());}
		catch(Exception e)	{e.printStackTrace(); System.exit(0);}
	}
	public void run()
	{
		new TestIHM(0);
	}
}