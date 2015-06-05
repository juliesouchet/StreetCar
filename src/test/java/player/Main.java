package test.java.player;

import javax.swing.SwingUtilities;





public class Main implements Runnable
{
	public static void main(String[] args)
	{
		try					{SwingUtilities.invokeLater(new Main());}
		catch(Exception e)	{e.printStackTrace(); System.exit(0);}
	}
	public void run()
	{
		new Test_IHM();
	}

}