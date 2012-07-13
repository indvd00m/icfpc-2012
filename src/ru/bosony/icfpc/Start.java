package ru.bosony.icfpc;

public class Start
{
	public static void main(String[] args)
	{
		String content = "################***...R......##***... ...*..##\\\\\\... ..\\\\\\.##...... ...*..##..     .. ...##.... .... ...##.... .... ...##.. .       ..##..*. .. .....##.... .. .....##.\\.. .......*##.............##.........   .##############L#";
		Mine mine = new Mine(15, 15, content);
		mine.print();
	}
}
