package main.java.data;

import java.io.Serializable;

import main.java.util.CloneableInterface;






public class LoginInfo implements Serializable, CloneableInterface<LoginInfo>
{
// --------------------------------------------
// Attributes:
// --------------------------------------------
	public static final long		serialVersionUID	= 1735179230089796614L;
	public static final LoginInfo[]	initialLoginTable	= {	new LoginInfo(false,	null,	true,	true,	-1),
															new LoginInfo(false,	null,	false,	true,	-1),
															new LoginInfo(false,	null,	false,	false,	1),
															new LoginInfo(false,	null,	true,	false,	1), // TODO parameter n°3 = false
															new LoginInfo(false,	null,	false,	false,	2),
															new LoginInfo(false,	null,	false,	false,	3)};

	private boolean		isClosed;
	private String		playerName;
	private boolean		isHost;
	private boolean		isHuman;
	private int			aiLevel	= -1;

// --------------------------------------------
// Builder:
// --------------------------------------------
	public LoginInfo(boolean isClosed, String playerName, boolean isHost, boolean isHuman, int iaLevel)
	{
		this.isClosed	= isClosed;
		this.playerName	= (playerName == null) ? null : new String(playerName);
		this.isHost		= isHost;
		this.isHost		= isHost;
		this.isHuman	= isHuman;
		this.aiLevel	= iaLevel;
	}
	private LoginInfo(){}
	public LoginInfo getClone()
	{
		LoginInfo res = new LoginInfo();

		res.isClosed	= this.isClosed;
		res.playerName	= (this.playerName == null) ? null : new String(this.playerName);
		res.isHost		= this.isHost;
		res.isHuman		= this.isHuman;
		res.aiLevel		= this.aiLevel;
		return res;
	}

// --------------------------------------------
// Getter / setter:
// --------------------------------------------
	public void		setIsClosed(boolean isClosed)	{this.isClosed = isClosed;}
	public void		setFreeCell()					{this.playerName = null;}
	public boolean	isClosed()						{return this.isClosed;}
	public String	getPlayerName()					{return (this.playerName == null) ? null : new String(this.playerName);}
	public boolean	isHost()						{return this.isHost;}
	public boolean	isHuman()						{return this.isHuman;}
	public int		getAiLevel()					{return this.aiLevel;}
	public boolean	isOccupiedCell()				{return (this.playerName != null);}

// --------------------------------------------
// Local Methodes:
// --------------------------------------------
	public static LoginInfo[] getInitialLoggedPlayerTable()
	{
		LoginInfo[]	res = new LoginInfo[Data.maxNbrPlayer];

		for (int i=0; i<Data.maxNbrPlayer; i++) res[i] = initialLoginTable[i].getClone();
		return res;
	}
	public String toString()
	{
		String res = "";

		res += "LoginInfo:";
		res += "\n\t- isClosed\t:"		+ this.isClosed;
		res += "\n\t- playerName\t:"	+ this.playerName;
		res += "\n\t- isHost\t:"		+ this.isHost;
		res += "\n\t- isHuman\t:"		+ this.isHuman;
		res += "\n\t- AI_level\t:"		+ this.aiLevel;
		return res;
	}
}