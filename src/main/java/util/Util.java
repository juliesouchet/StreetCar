package main.java.util;

import java.awt.Color;
import java.awt.Point;







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
	
	/**
	 * Echange les cases d'un tableau.
	 * @param tab Le tableau à traiter
	 * @param i L'indice du premier élément (doit appartenir aux indices accessibles du tableau)
	 * @param j L'indice du deuxième élément (doit appartenir aux indices accessibles du tableau)
	 */
	public static void swapTab(Object[] tab, int i, int j)
	{
		Object tmp = tab [i];
		tab[i] = tab[j];
		tab[j] = tmp;
	}

	public static int manhathanDistance(Point p0, Point p1)
	{
		int a = Math.abs(p0.x - p1.x);
		int b = Math.abs(p0.y - p1.y);
		return a + b;
	}
}