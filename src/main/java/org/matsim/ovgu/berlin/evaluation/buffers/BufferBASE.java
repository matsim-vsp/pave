package org.matsim.ovgu.berlin.evaluation.buffers;

import org.matsim.ovgu.berlin.evaluation.model.EvTour;
import org.matsim.ovgu.berlin.evaluation.model.EvVariant;

public class BufferBASE {

	protected static EvVariant init(String name, double[] expTT, EvTour tour) {
		EvVariant base = new EvVariant("BASE", tour.tourDirectory, tour.tourIdent + "_BASE"+name,
				expTT, tour.linkIDs);
		tour.evBufferVariants.add(base);
		return base;
	}
}
