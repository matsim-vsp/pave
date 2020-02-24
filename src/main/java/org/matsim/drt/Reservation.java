package org.matsim.drt;

import org.matsim.api.core.v01.Identifiable;
import org.matsim.contrib.dvrp.schedule.Task;

import java.util.PriorityQueue;

interface Reservation extends Identifiable {

    double getReservationValidityEndTime();

    double getReservationValidityStartTime();

    double getSubmissionTime();

    PriorityQueue<Task> getTasks();

}
