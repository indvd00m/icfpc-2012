package ru.bosony.model.cellscontents;

import ru.bosony.model.io.TextRepresentable;

/**
 * @author indvdum (gotoindvdum[at]gmail[dot]com) <br>
 *         13.07.2012 21:25:49
 * 
 */
public abstract class CellContent implements TextRepresentable {

	@Override
	public CellContent fromText(String text) {
		for (CellContent content : new CellContent[] {
				new ClosedLambdaLift(),
				new Earth(),
				new Empty(),
				new Lambda(),
				new MiningRobot(),
				new OpenLambdaLift(),
				new Rock(),
				new Wall()
		}) {
			if (text != null && text.equals(content.toText()))
				return content;
		}
		return null;
	}

}
