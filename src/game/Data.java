package game;

public class Data
{
// --------------------------------------------
// Attributs:
// --------------------------------------------
	private Cell[][]	board;

// --------------------------------------------
// Builder:
// --------------------------------------------
	public Data(int width, int height)
	{
		this.board = new Cell[3][3];
	}

// --------------------------------------------
// Local Methode:
// --------------------------------------------
	public int getWidth()	{return this.board.length;}
	public int getHeight()	{return this.board[0].length;}

}