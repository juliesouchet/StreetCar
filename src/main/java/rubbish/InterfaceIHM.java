package main.java.rubbish;

import main.java.data.Data;





public interface InterfaceIHM
{
	public void refresh(Data data);
	public void excludePlayer();
	public void refreshMessages(String playerName, String message);
}