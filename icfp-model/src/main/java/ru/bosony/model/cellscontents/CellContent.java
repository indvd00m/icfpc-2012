package ru.bosony.model.cellscontents;

import ru.bosony.model.io.TextRepresentable;

/**
 * @author indvdum (gotoindvdum[at]gmail[dot]com) <br>
 *         13.07.2012 21:25:49
 * 
 */
public enum CellContent implements TextRepresentable {
	
	ClosedLambdaLift("L"),
	Earth("."),
	Empty(" "),
	Lambda("\\"),
	MiningRobot("R"),
	OpenLambdaLift("O"),
	Rock("*"),
	Wall("#");

	private String	text;

	private CellContent(String text) {
		this.text = text;
	}

	@Override
	public String toText() {
		return text;
	}

	public static CellContent fromText(String text) {
		for (CellContent content : values()) {
			if (text != null && text.equals(content.toText()))
				return content;
		}
		return null;
	}

}
