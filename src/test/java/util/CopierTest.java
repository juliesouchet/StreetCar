package test.java.util;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.LinkedList;

import main.java.util.Copier;

import org.junit.Test;

public class CopierTest {

	@Test
	public void doubleDeepCopyTest() {
		ArrayList<LinkedList<String>> arrayoflinked = new ArrayList<LinkedList<String>>();
		arrayoflinked.add(new LinkedList<String>());
		arrayoflinked.get(0).add("0 - 0");
		arrayoflinked.get(0).add("0 - 1");
		arrayoflinked.add(new LinkedList<String>());
		arrayoflinked.get(1).add("1 - 1");
		arrayoflinked.get(1).add("1 - 1");
		

		ArrayList<LinkedList<String>> copy = new Copier<LinkedList<String>>().copyList(arrayoflinked);
		assertEquals(arrayoflinked.size(), copy.size());
		
		for(int i = 0; i < arrayoflinked.size(); i++)
		{
			LinkedList<String> list = arrayoflinked.get(i);
			LinkedList<String> copyList = copy.get(i);
			assertEquals(list.size(), copyList.size());
			for(int j = 0; j < list.size(); j++)
			{
				assertEquals(list.get(j), copyList.get(j));
			}
		}
	}
	
	@Test
	public void tripleDeepCopyTest() {
		ArrayList<LinkedList<LinkedList<String>>> arrayOfLinkedOfLinked = new ArrayList<LinkedList<LinkedList<String>>>();
		for(int i = 0; i < 3; i++)
		{
			arrayOfLinkedOfLinked.add(new LinkedList<LinkedList<String>>());
			for(int j = 0; j < 3; j++)
			{
				arrayOfLinkedOfLinked.get(i).add(new LinkedList<String>());
				for(int k = 0; k < 3; k++)
				{
					arrayOfLinkedOfLinked.get(i).get(j).add(i + " - " + j + " - " + k);
				}
			}
		}
		

		ArrayList<LinkedList<LinkedList<String>>> copy = new Copier<LinkedList<LinkedList<String>>>().copyList(arrayOfLinkedOfLinked);
		assertEquals(arrayOfLinkedOfLinked.size(), copy.size());
		
		for(int i = 0; i < arrayOfLinkedOfLinked.size(); i++)
		{
			LinkedList<LinkedList<String>> list = arrayOfLinkedOfLinked.get(i);
			LinkedList<LinkedList<String>> copyList = copy.get(i);
			assertEquals(list.size(), copyList.size());
			for(int j = 0; j < list.size(); j++)
			{
				LinkedList<String> subList = list.get(j);
				LinkedList<String> subCopyList = copyList.get(j);
				assertEquals(subList.size(), subCopyList.size());
				for(int k = 0; k < subList.size(); k++)
				{
					assertEquals(subList.get(k), subCopyList.get(k));
				}
			}
		}
	}

}
