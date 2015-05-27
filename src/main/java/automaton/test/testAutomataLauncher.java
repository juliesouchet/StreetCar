package main.java.automaton.test;

import javax.swing.SwingUtilities;





public class testAutomataLauncher implements Runnable
{
	public static void main(String[] args)
	{
		try					{SwingUtilities.invokeLater(new testAutomataLauncher());}
		catch(Exception e)	{e.printStackTrace(); System.exit(0);}
	}
	public void run()
	{
		new PlayerAutomata(0);
	}

}