package ru.bosony.icfpc;

import java.nio.file.FileVisitOption;
import java.util.LinkedHashMap;
import java.util.Map;

import ru.bosony.icfpc.cells.Cell;

/**
 * Шахта
 * 
 * @author akhmetovlz
 *
 */
public class Mine
{
	private Map<Character, Cell> cellPrototypes = new LinkedHashMap<Character, Cell>();
	
	private Cell cells[][][];
	private int index = 0;
	
	public Mine(int n, int m, String content)
	{
		initCellPrototypes();
		
		cells = new Cell[2][n][m];
		
		int index = 0;
		
		for(int i=0; i<n; ++i)
		for(int j=0; j<m; ++j)
		{
			cells[index][i][j] = cellPrototypes.get(content.charAt(index++));
		}
	}
	
	private Cell[][] firstBuffer()
	{
		return cells[index];
	}
	
	private Cell[][] secondBuffer()
	{
		return cells[index==0 ? 1 : 0];
	}
	
	private void swapBuffers()
	{
		index = index==0 ? 1 : 0;
	}
	
	private void initCellPrototypes()
	{
		for(Cell c : Cell.values())
		{
			cellPrototypes.put(c.getASCII(), c);
		}
	}	

	public void print()
	{
		for(int i=0; i<cells.length; ++i)
		{
			for(int j=0; j<cells[i].length; ++j)
			{
				System.out.print(firstBuffer()[i][j]);
			}
			System.out.println();
		}
	}
	
	public void updateAfterMove()
	{
		for(int i=0; i<cells.length; ++i)
		for(int j=0; j<cells[i].length; ++j)
		{
			secondBuffer()[i][j] = firstBuffer()[i][j];
		}
		
		for(int i=1; i<cells.length-1; ++i)
		for(int j=cells[i].length-1; j>1; --j)
		{
			switch(firstBuffer()[i][j])
			{
				case ROCK:
				{
					if(firstBuffer()[i][j-1] == Cell.EMPTY)
					{
						secondBuffer()[i][j] = Cell.EMPTY;
					}
				}
				break;
			}
		}
	}
	
}
