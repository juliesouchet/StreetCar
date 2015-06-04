package test.java.game;






public class Test
{

	public static void main(String[] args)
	{
		System.out.println(compte());
	}
	public static int compte()
	{
		int res = 0;

		for (int h1 = 0; h1<5; h1++)											//		For each player's hand tile
		{
			for (int r1=0; r1<4; r1++)														//		For each first tile rotation
			{
				for (int x1=1; x1<13; x1++)												//		For each board cell
				{
					for (int y1=1; y1<13; y1++)
					{
						for (int h2 = h1+1; h2<5; h2++)						//		For each second player's hand tile
						{
								for (int x2=1; x2<13; x2++)								//		For each board cell
								{
									for (int y2=1; y2<13; y2++)
									{
										if ((x1 == x2) && (y1 == y2)) continue;
										for (int r2=0; r2<4; r2++)							//		For each second tile rotation
										{
											res ++;
										}
									}
								}
							}
						}
					}
				}
			}
		return res;
	}
}
