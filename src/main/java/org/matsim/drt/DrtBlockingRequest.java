package org.matsim.drt;

import org.matsim.api.core.v01.Identifiable;
import org.matsim.contrib.dvrp.optimizer.Request;
import org.matsim.contrib.dvrp.schedule.Task;

import java.util.PriorityQueue;

interface DrtBlockingRequest extends Request {

    //TODO: decide whether this is necessary. If yes implement HasPersonId interface
//    Id<Person> getPersonId();

    double getEndTime();

    double getStartTime();

    PriorityQueue<Task> getTasks();

}
