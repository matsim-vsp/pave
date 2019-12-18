package org.matsim.drt;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Identifiable;
import org.matsim.api.core.v01.network.Link;
import org.matsim.contrib.drt.schedule.DrtTask;

import java.util.PriorityQueue;

interface Reservation extends Identifiable {

    double getReservationValidityEndTime();

    double getReservationValidityStartTime();

    double getSubmissionTime();

    PriorityQueue<DrtTask> getTasks();

}
