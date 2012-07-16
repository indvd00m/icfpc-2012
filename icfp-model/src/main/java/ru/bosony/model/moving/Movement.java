package ru.bosony.model.moving;

import ru.bosony.model.io.TextRepresentable;

/**
 * @author indvdum (gotoindvdum[at]gmail[dot]com) <br>
 *         14.07.2012 0:40:35
 * 
 */
public enum Movement implements TextRepresentable {
	
	LEFT("L"),
	UP("U"),
	RIGHT("R"),
	DOWN("D"),
	WAIT("W"),
	RAZOR("S"),
	ABORT("A");

	private String	text;

	private Movement(String text) {
		this.text = text;
	}

	@Override
	public String toText() {
		return text;
	}

	public static Movement fromText(String text) {
		for (Movement content : values()) {
			if (text != null && text.equals(content.toText()))
				return content;
		}
		return null;
	}

}
