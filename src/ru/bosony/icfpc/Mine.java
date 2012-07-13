package ru.bosony.icfpc;

import java.util.LinkedHashMap;
import java.util.Map;

import ru.bosony.icfpc.cells.Cell;
import ru.bosony.icfpc.cells.CellTypes;

/**
 * Шахта
 * 
 * @author akhmetovlz
 *
 */
public class Mine
{
	private Map<Character, Cell> cellPrototypes = new LinkedHashMap<Character, Cell>();
	
	private Cell cells[][];
	
	public Mine(int n, int m, String content)
	{
		initCellPrototypes();
		
		cells = new Cell[n][m];
		
		int index = 0;
		
		for(int i=0; i<n; ++i)
		for(int j=0; j<m; ++j)
		{
			cells[i][j] = new Cell( cellPrototypes.get(content.charAt(index++)) );
		}
	}
	
	private void initCellPrototypes()
	{
		cellPrototypes.put('*', new Cell(CellTypes.ROCK, '*'));
		cellPrototypes.put('\\', new Cell(CellTypes.LAMBDA, '\\'));
		cellPrototypes.put('#', new Cell(CellTypes.WALL, '#'));
		cellPrototypes.put('R', new Cell(CellTypes.ROBOT, 'R'));
		cellPrototypes.put('L', new Cell(CellTypes.CLOSED_LAMBDA_LIFT, 'L'));
		cellPrototypes.put('O', new Cell(CellTypes.OPEN_LAMBDA_LIFT, 'O'));
		cellPrototypes.put(' ', new Cell(CellTypes.SPACE, ' '));
		cellPrototypes.put('.', new Cell(CellTypes.EARTH, '.'));
	}	

	void print()
	{
		for(int i=0; i<cells.length; ++i)
		{
			for(int j=0; j<cells[i].length; ++j)
			{
				System.out.print(cells[i][j].getASCII());
			}
			System.out.println();
		}
	}
}
