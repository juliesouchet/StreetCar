package test.java.player;

import main.java.data.Data;
import main.java.rubbish.InterfaceIHM;





public class TestDumbestIHM implements InterfaceIHM
{
// --------------------------------------------
// Attributs:
// --------------------------------------------
	private DataViewerFrame frame;

// --------------------------------------------
// Builder:
// --------------------------------------------
	public TestDumbestIHM()
	{
		// Game data viewer
		this.frame = new DataViewerFrame(null);
		this.frame.setVisible(true);
	}

// --------------------------------------------
// Local methods:
// --------------------------------------------
	public void refresh(Data data)
	{
		if (this.frame != null) this.frame.setGameData(data);
	}
	public void excludePlayer()
	{
		System.out.println("------------------------------------");
		System.out.println("excludePlayer");
		System.out.println("Not managed yet");
	}
}