package main.java.util;

import java.awt.Color;







public class Util
{
	public static Color parseColor(String color)
	{
		if		(color.equals("red"))		return Color.red;
		else if	(color.equals("yellow"))	return Color.yellow;
		else if	(color.equals("green"))		return Color.green;
		else if	(color.equals("blue"))		return Color.blue;
		else if	(color.equals("white"))		return Color.white;
		else if	(color.equals("black"))		return Color.black;
		else	throw new RuntimeException("Unknown color: " + color);
	}
}