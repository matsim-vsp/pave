package org.matsim.ovgu.berlin.evaluation.simulation;

import org.matsim.ovgu.berlin.Settings;
import org.matsim.ovgu.berlin.evaluation.EvBufferSetup;
import org.matsim.ovgu.berlin.evaluation.EvBufferVariant;
import org.matsim.ovgu.berlin.evaluation.EvTour;
import org.matsim.ovgu.berlin.simulation.FreightOnlyMatsim;

public class Simulation {

	public static void run(EvTour tour, String timeWindowMethod) {

		for (EvBufferVariant variant : tour.evBufferVariants) {
			for (EvBufferSetup buffer : variant.buffers) {
				buffer.readRunSettings();
				Settings settings = buffer.runSettings;
				settings.directory += timeWindowMethod + "/";
				settings.timeWindowMethod = timeWindowMethod;
//				if (Files.notExists(Path.of(settings.directory)))
					new FreightOnlyMatsim(settings);
			}
		}
	}

}
