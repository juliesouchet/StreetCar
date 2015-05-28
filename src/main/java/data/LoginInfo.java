package main.java.data;

import main.java.util.CloneableInterface;






public class LoginInfo implements CloneableInterface<LoginInfo>
{
// --------------------------------------------
// Attributes:
// --------------------------------------------
	public static final LoginInfo	initialFirstCell	= new LoginInfo(false, null, true, true, -1);
	public static final LoginInfo	initialNonFirstCell	= new LoginInfo(false, null, false, true, -1);

	private boolean		isClosed;
	private String		playerName;
	private boolean		isHost;
	private boolean		isHuman;
	private int			iaLevel;

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
		this.iaLevel	= iaLevel;
	}
	private LoginInfo(){}
	public LoginInfo getClone()
	{
		LoginInfo res = new LoginInfo();

		res.isClosed	= this.isClosed;
		res.playerName	= (this.playerName == null) ? null : new String(this.playerName);
		res.isHost		= this.isHost;
		res.isHuman		= this.isHuman;
		res.iaLevel		= this.iaLevel;
		return res;
	}

// --------------------------------------------
// Local Methodes:
// --------------------------------------------
	public static LoginInfo[] getInitialLoggedPlayerTable()
	{
		LoginInfo[]	res = new LoginInfo[Data.maxNbrPlayer];

		res[0] = initialFirstCell.getClone();
		for (int i=1; i<Data.maxNbrPlayer; i++) res[i] = initialNonFirstCell.getClone();
		return res;
	}
}