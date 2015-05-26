package main.java.automaton;

import javax.swing.SwingUtilities;

import test.java.player.TestIA;





public class TestDumbest implements Runnable
{
	public static void main(String[] args)
	{
		try					{SwingUtilities.invokeLater(new TestDumbest());}
		catch(Exception e)	{e.printStackTrace(); System.exit(0);}
	}
	public void run()
	{
		new TestIA(0);
	}
}