package main.java.data;






public class LoginInfo// implements CloneableInterface<LoginInfo>
{
// --------------------------------------------
// Attributes:
// --------------------------------------------
	private boolean		closed;
	private boolean		isHuman;

// --------------------------------------------
// Builder:
// --------------------------------------------
/*	public LoginInfo getClone()
	{

	}
*/
// --------------------------------------------
// Local Methodes:
// --------------------------------------------
	public static LoginInfo[] getInitialLoggedPlayerTable()
	{
		LoginInfo[]	res = new LoginInfo[Data.maxNbrPlayer];

		
		res[0] = new LoginInfo();
		res[0] .closed = false;
		res[0] .isHuman	= false;
		
		for (int i=1; i<Data.maxNbrPlayer; i++)
		{
			
		}
		return res;
	}
}