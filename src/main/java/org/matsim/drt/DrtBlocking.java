package org.matsim.drt;

import org.matsim.api.core.v01.Identifiable;
import org.matsim.contrib.dvrp.schedule.Task;

import java.util.PriorityQueue;

interface DrtBlocking extends Identifiable {

    //TODO: decide whether this is necessary
//    Id<Person> getPersonId();

    double getReservationValidityEndTime();

    double getReservationValidityStartTime();

    double getSubmissionTime();

    PriorityQueue<Task> getTasks();

}
