package org.matsim.ovgu.berlin.evaluation.simulation;

import org.matsim.ovgu.berlin.Settings;
import org.matsim.ovgu.berlin.evaluation.model.EvBuffer;
import org.matsim.ovgu.berlin.evaluation.model.EvTour;
import org.matsim.ovgu.berlin.evaluation.model.EvVariant;
import org.matsim.ovgu.berlin.simulation.FreightOnlyMatsim;

public class Simulation {

	public static void run(EvTour tour, String timeWindowMethod) {

		for (EvVariant variant : tour.evBufferVariants) {
			for (EvBuffer buffer : variant.buffers) {
				buffer.load();
				Settings settings = buffer.runSettings;
				settings.directory += timeWindowMethod + "/";
				settings.timeWindowMethod = timeWindowMethod;
//				if (Files.notExists(Path.of(settings.directory)))
					new FreightOnlyMatsim(settings);
			}
		}
	}

}
