package ru.bosony.icfpc.cells;

public enum  Cell
{
	ROBOT('R'),
	ROCK('*'),
	EAGHT('.'),
	WALL('#'),
	LAMBDA('\\'),
	CLOSED_LAMBDA_LIFT('L'),
	OPEN_LAMBDA_LIFT('O'),
	EMPTY(' '),
	EARTH('.');
	
	private char ASCII;
	
	private Cell(char ASCII)
	{
		this.ASCII = ASCII;
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
