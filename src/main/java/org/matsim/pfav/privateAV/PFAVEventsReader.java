/* *********************************************************************** *
 * project: org.matsim.*
 * Controler.java
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2007 by the members listed in the COPYING,        *
 *                   LICENSE and WARRANTY file.                            *
 * email           : info at matsim dot org                                *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *   See also COPYING, LICENSE and WARRANTY file                           *
 *                                                                         *
 * *********************************************************************** */

package privateAV;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.events.GenericEvent;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.population.Person;
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.events.EventsReaderXMLv1;
import org.matsim.core.utils.io.MatsimXmlParser;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.Map;
import java.util.Stack;

public class PFAVEventsReader extends MatsimXmlParser {

    private EventsReaderXMLv1 delegate;

    public PFAVEventsReader(EventsManager events) {
        delegate = new EventsReaderXMLv1(events);
        this.setValidating(false);
        delegate.addCustomEventMapper(EventFreightTourCompleted.EVENT_TYPE, getFreightTourCompletedEventMapper());
        delegate.addCustomEventMapper(EventFreightTourRequestRejected.EVENT_TYPE, getFreightTourRequestDeniedEventMapper());
        delegate.addCustomEventMapper(EventFreightTourScheduled.EVENT_TYPE, getFreightTourScheduledEventMapper());
        delegate.addCustomEventMapper(EventPFAVOwnerWaitsForVehicle.EVENT_TYPE, getPFAVOwnerWaitsEventMapper());
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        delegate.characters(ch, start, length);
    }

    private EventsReaderXMLv1.CustomEventMapper<EventFreightTourCompleted> getFreightTourCompletedEventMapper() {
        return new EventsReaderXMLv1.CustomEventMapper<EventFreightTourCompleted>() {
            @Override
            public EventFreightTourCompleted apply(GenericEvent event) {

                Map<String, String> attributes = event.getAttributes();

                double time = Double.parseDouble(attributes.get(EventFreightTourCompleted.ATTRIBUTE_TIME));
                Id<DvrpVehicle> vid = Id.create(attributes.get(EventFreightTourCompleted.ATTRIBUTE_VEHICLE), DvrpVehicle.class);

                return new EventFreightTourCompleted(vid, time);
            }
        };
    }

    private EventsReaderXMLv1.CustomEventMapper<EventFreightTourRequestRejected> getFreightTourRequestDeniedEventMapper() {
        return new EventsReaderXMLv1.CustomEventMapper<EventFreightTourRequestRejected>() {
            @Override
            public EventFreightTourRequestRejected apply(GenericEvent event) {

                Map<String, String> attributes = event.getAttributes();

                double time = Double.parseDouble(attributes.get(EventFreightTourRequestRejected.ATTRIBUTE_TIME));
                Id<DvrpVehicle> vid = Id.create(attributes.get(EventFreightTourRequestRejected.ATTRIBUTE_VEHICLE), DvrpVehicle.class);
                Id<Link> requestLink = Id.createLinkId(attributes.get(EventFreightTourRequestRejected.ATTRIBUTE_REQUEST_LINK));
                Id<Link> returnLink = Id.createLinkId(attributes.get(EventFreightTourRequestRejected.ATTRIBUTE_MUST_RETURN_LINK));
                double returnTime = Double.parseDouble(attributes.get(EventFreightTourRequestRejected.ATTRIBUTE_MUST_RETURN_TIME));

                return new EventFreightTourRequestRejected(vid, requestLink, time, returnLink, returnTime);
            }
        };
    }

    private EventsReaderXMLv1.CustomEventMapper<EventFreightTourScheduled> getFreightTourScheduledEventMapper() {
        return new EventsReaderXMLv1.CustomEventMapper<EventFreightTourScheduled>() {
            @Override
            public EventFreightTourScheduled apply(GenericEvent event) {

                Map<String, String> attributes = event.getAttributes();

                double time = Double.parseDouble(attributes.get(EventFreightTourScheduled.ATTRIBUTE_TIME));
                Id<DvrpVehicle> vid = Id.create(attributes.get(EventFreightTourScheduled.ATTRIBUTE_VEHICLE), DvrpVehicle.class);
                Id<Link> requestLink = Id.createLinkId(attributes.get(EventFreightTourScheduled.ATTRIBUTE_REQUEST_LINK));
                double returnTime = Double.parseDouble(attributes.get(EventFreightTourScheduled.ATTRIBUTE_MUST_RETURN_TIME));
                double tourDuration = Double.parseDouble(attributes.get(EventFreightTourScheduled.ATTRIBUTE_FREIGHT_TOUR_DURATION));
                double tourLength = Double.parseDouble(attributes.get(EventFreightTourScheduled.ATTRIBUTE_FREIGHT_TOUR_DISTANCE));

                return new EventFreightTourScheduled(time, vid, requestLink, returnTime, tourDuration, tourLength);
            }
        };
    }

    private EventsReaderXMLv1.CustomEventMapper<EventPFAVOwnerWaitsForVehicle> getPFAVOwnerWaitsEventMapper() {
        return new EventsReaderXMLv1.CustomEventMapper<EventPFAVOwnerWaitsForVehicle>() {
            @Override
            public EventPFAVOwnerWaitsForVehicle apply(GenericEvent event) {

                Map<String, String> attributes = event.getAttributes();

                double time = Double.parseDouble(attributes.get(EventPFAVOwnerWaitsForVehicle.ATTRIBUTE_TIME));
                Id<DvrpVehicle> vid = Id.create(attributes.get(EventPFAVOwnerWaitsForVehicle.ATTRIBUTE_VEHICLE), DvrpVehicle.class);
                Id<Person> owner = Id.createPersonId(attributes.get(EventPFAVOwnerWaitsForVehicle.ATTRIBUTE_OWNER));

                return new EventPFAVOwnerWaitsForVehicle(time, vid, owner);
            }
        };
    }

    @Override
    public void startTag(String name, Attributes atts, Stack<String> context) {
        delegate.startTag(name, atts, context);
    }

    @Override
    public void endTag(String name, String content, Stack<String> context) {
        delegate.endTag(name, content, context);
    }
}
