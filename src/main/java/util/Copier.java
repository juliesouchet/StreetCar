package main.java.util;

import java.awt.Color;
import java.awt.Point;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;






public class Copier <T>
{
	/**=============================================================
	 * @return Creates a deep clone of the input list and each of its elements
	 ===============================================================*/
	@SuppressWarnings("unchecked")
	public LinkedList<T> copyList(LinkedList<T> l)
	{
		LinkedList<T> res = new LinkedList<T>();
		T elem;

		for (T t: l)
		{
			if 		(t == null)							elem = null;
			else if (t instanceof CloneableInterface)	elem = ((CloneableInterface<T>) t).getClone();
			else										elem = getMyJavaClones(t);
			res.add(elem);
		}

		return res;
	}
	/**=============================================================
	 * @return Creates a deep clone of the input list and each of its elements
	 ===============================================================*/
	@SuppressWarnings("unchecked")
	public ArrayList<T> copyList(ArrayList<T> l)
	{
		ArrayList<T> res = new ArrayList<T>();
		T elem;

		for (T t: l)
		{
			if 		(t == null)							elem = null;
			else if (t instanceof CloneableInterface)	elem = ((CloneableInterface<T>) t).getClone();
			else										elem = getMyJavaClones(t);
			res.add(elem);
		}

		return res;
	}
	/**=============================================================
	 * @return Creates a deep clone of the input table and each of its elements
	 ===============================================================*/
	@SuppressWarnings("unchecked")
	public T[] copyTab(T[] tab)
	{
		int	w	= tab.length;
		T[] res	= (T[]) Array.newInstance(tab.getClass().getComponentType(), w);
		T t, elem;

		for (int i=0; i<w; i++)
		{
			t = tab[i];
			if		(t == null)							elem = null;
			else if (t instanceof CloneableInterface)	elem = ((CloneableInterface<T>) t).getClone();
			else										elem = getMyJavaClones(t);
			res[i] = elem;
		}
		return res;
	}
	/**=============================================================
	 * @return Creates a deep clone of the input matrix and each of its elements
	 ===============================================================*/
	@SuppressWarnings("unchecked")
	public T[][] copyMatrix(T[][] matrix)
	{
		int		w	= matrix.length;
		int		h	= matrix[0].length;
		T[][]	res	= (T[][]) Array.newInstance(matrix.getClass().getComponentType(), w);
		T t, elem;

		for (int i=0; i<w; i++)
		{
			res[i] = (T[]) Array.newInstance(matrix[i].getClass().getComponentType(), h);
			for (int j=0; j<h; j++)
			{
				t = matrix[i][j];
				if		(t == null)							elem = null;
				else if (t instanceof CloneableInterface)	elem = ((CloneableInterface<T>) t).getClone();
				else										elem = getMyJavaClones(t);
				res[i][j] = elem;
			}
		}
		return res;
	}
// ---------------------------------------------
// Private methods
// ---------------------------------------------
	/**===================================================
	 * @return a pseudo clone of the input element
	 =====================================================*/
	@SuppressWarnings("unchecked")
	private T getMyJavaClones(T elem)
	{
		if (elem instanceof String)		return (T) new String((String)elem);
		if (elem instanceof Point)		return (T) new Point((Point)elem);
		if (elem instanceof Color)		return (T) new Color(((Color)elem).getRGB());
		if (elem instanceof ArrayList)	return (T) new ArrayList<>((ArrayList<?>)elem);
		if (elem instanceof LinkedList)	return (T) new LinkedList<>((LinkedList<?>)elem);
		else throw new RuntimeException("Cloneable cast unexpected: " + elem.getClass());
	}
}