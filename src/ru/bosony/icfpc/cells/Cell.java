package ru.bosony.icfpc.cells;

public class Cell
{
	
	private CellTypes type;
	private char ASCII;
	
	public Cell(CellTypes type, char ASCII)
	{
		this.type = type;
		this.ASCII = ASCII;
	}
	
	public Cell(Cell cell)
	{
		this.type = cell.type;
		this.ASCII = cell.ASCII;
	}
	
	public CellTypes getType()
	{
		return type;
	}
	public void setType(CellTypes type)
	{
		this.type = type;
	}
	
	public char getASCII()
	{
		return ASCII;
	}
	public void setASCII(char aSCII)
	{
		ASCII = aSCII;
	}
	
}
