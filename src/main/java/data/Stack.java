package main.java.data;

import java.util.HashMap;







public class Stack
{
// --------------------------------------------
// Attributes:
// --------------------------------------------
	private HashMap<String, StackCell>	stack;
	// TODO: Map: tileName/nbrRemainingTiles
	
	
// --------------------------------------------
// Builder:
// --------------------------------------------
	public Stack()
	{
		this.stack	= new HashMap<String, StackCell>();
	}

// --------------------------------------------
// Setters/getters:
// --------------------------------------------
	// TODO: public int getRemainingSize()

// --------------------------------------------
// Stack cell class:
// Gathers a list of informations corresponding to a tile
// --------------------------------------------
	private class StackCell
	{
		// Attributes
		public Tile	t;
		public int	remaining;

		// Builder
		public StackCell(Tile t)
		{
			this.t			= t;
			this.remaining	= t.getCardinal();
		}
		// Setter
		public boolean equals(StackCell sc)	{return (sc.t.getTileID().equals(t.getTileID()));}
	}
}