package ru.bosony.model.cellscontents;

import java.util.HashSet;
import java.util.Set;

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
	Wall("#"),
	TrampolineA("A"),
	TrampolineB("B"),
	TrampolineC("C"),
	TrampolineD("D"),
	TrampolineE("E"),
	TrampolineF("F"),
	TrampolineG("G"),
	TrampolineH("H"),
	TrampolineI("I"),
	Target1("1"),
	Target2("2"),
	Target3("3"),
	Target4("4"),
	Target5("5"),
	Target6("6"),
	Target7("7"),
	Target8("8"),
	Target9("9");

	private String					text;
	private CellContent				trampolineTarget	= null;
	private static Set<CellContent>	trampolines			= new HashSet<CellContent>();
	private static Set<CellContent>	targets				= new HashSet<CellContent>();
	static {
		trampolines.add(TrampolineA);
		trampolines.add(TrampolineB);
		trampolines.add(TrampolineC);
		trampolines.add(TrampolineD);
		trampolines.add(TrampolineE);
		trampolines.add(TrampolineF);
		trampolines.add(TrampolineG);
		trampolines.add(TrampolineH);
		trampolines.add(TrampolineI);
		targets.add(Target1);
		targets.add(Target2);
		targets.add(Target3);
		targets.add(Target4);
		targets.add(Target5);
		targets.add(Target6);
		targets.add(Target7);
		targets.add(Target8);
		targets.add(Target9);
	}

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

	public CellContent getTrampolineTarget() {
		return trampolineTarget;
	}

	public void setTrampolineTarget(CellContent trampolineTarget) {
		if (!trampolines.contains(this) || trampolineTarget != null && !targets.contains(trampolineTarget))
			throw new RuntimeException("Only references from Trampolines to Targets allowed");
		this.trampolineTarget = trampolineTarget;
	}

	public static Set<CellContent> getTrampolines() {
		return trampolines;
	}

	public static Set<CellContent> getTargets() {
		return targets;
	}

}
