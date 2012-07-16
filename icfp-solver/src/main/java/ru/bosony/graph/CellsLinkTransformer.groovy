package ru.bosony.graph;

import org.apache.commons.collections15.Transformer;

/**
 * @author 	indvdum (gotoindvdum[at]gmail[dot]com) <br>
 * 			16.07.2012 12:05:51
 *
 * @param <CellsLink>
 * @param <Double>
 */
public class CellsLinkTransformer<CellsLink, Double> implements Transformer<CellsLink, Double> {

	@Override
	public Double transform(CellsLink input) {
		return input.getWeight();
	}
}
