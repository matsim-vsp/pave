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

package privateAV.events;

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
        delegate.addCustomEventMapper(FreightTourCompletedEvent.EVENT_TYPE, getFreightTourCompletedEventMapper());
        delegate.addCustomEventMapper(FreightTourRequestRejectedEvent.EVENT_TYPE, getFreightTourRequestDeniedEventMapper());
        delegate.addCustomEventMapper(FreightTourScheduledEvent.EVENT_TYPE, getFreightTourScheduledEventMapper());
        delegate.addCustomEventMapper(PFAVOwnerWaitsForVehicleEvent.EVENT_TYPE, getPFAVOwnerWaitsEventMapper());
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        delegate.characters(ch, start, length);
    }

    private EventsReaderXMLv1.CustomEventMapper<FreightTourCompletedEvent> getFreightTourCompletedEventMapper() {
        return new EventsReaderXMLv1.CustomEventMapper<FreightTourCompletedEvent>() {
            @Override
            public FreightTourCompletedEvent apply(GenericEvent event) {

                Map<String, String> attributes = event.getAttributes();

                double time = Double.parseDouble(attributes.get(FreightTourCompletedEvent.ATTRIBUTE_TIME));
                Id<DvrpVehicle> vid = Id.create(attributes.get(FreightTourCompletedEvent.ATTRIBUTE_VEHICLE), DvrpVehicle.class);

                return new FreightTourCompletedEvent(vid, time);
            }
        };
    }

    private EventsReaderXMLv1.CustomEventMapper<FreightTourRequestRejectedEvent> getFreightTourRequestDeniedEventMapper() {
        return new EventsReaderXMLv1.CustomEventMapper<FreightTourRequestRejectedEvent>() {
            @Override
            public FreightTourRequestRejectedEvent apply(GenericEvent event) {

                Map<String, String> attributes = event.getAttributes();

                double time = Double.parseDouble(attributes.get(FreightTourRequestRejectedEvent.ATTRIBUTE_TIME));
                Id<DvrpVehicle> vid = Id.create(attributes.get(FreightTourRequestRejectedEvent.ATTRIBUTE_VEHICLE), DvrpVehicle.class);
                Id<Link> requestLink = Id.createLinkId(attributes.get(FreightTourRequestRejectedEvent.ATTRIBUTE_REQUEST_LINK));
                Id<Link> returnLink = Id.createLinkId(attributes.get(FreightTourRequestRejectedEvent.ATTRIBUTE_MUST_RETURN_LINK));
                double returnTime = Double.parseDouble(attributes.get(FreightTourRequestRejectedEvent.ATTRIBUTE_MUST_RETURN_TIME));

                return new FreightTourRequestRejectedEvent(vid, requestLink, time, returnLink, returnTime);
            }
        };
    }

    private EventsReaderXMLv1.CustomEventMapper<FreightTourScheduledEvent> getFreightTourScheduledEventMapper() {
        return new EventsReaderXMLv1.CustomEventMapper<FreightTourScheduledEvent>() {
            @Override
            public FreightTourScheduledEvent apply(GenericEvent event) {

                Map<String, String> attributes = event.getAttributes();

                double time = Double.parseDouble(attributes.get(FreightTourScheduledEvent.ATTRIBUTE_TIME));
                Id<DvrpVehicle> vid = Id.create(attributes.get(FreightTourScheduledEvent.ATTRIBUTE_VEHICLE), DvrpVehicle.class);
                Id<Link> requestLink = Id.createLinkId(attributes.get(FreightTourScheduledEvent.ATTRIBUTE_REQUEST_LINK));
                double returnTime = Double.parseDouble(attributes.get(FreightTourScheduledEvent.ATTRIBUTE_MUST_RETURN_TIME));
                double tourDuration = Double.parseDouble(attributes.get(FreightTourScheduledEvent.ATTRIBUTE_FREIGHT_TOUR_DURATION));
                double tourLength = Double.parseDouble(attributes.get(FreightTourScheduledEvent.ATTRIBUTE_FREIGHT_TOUR_DISTANCE));

                return new FreightTourScheduledEvent(time, vid, requestLink, returnTime, tourDuration, tourLength);
            }
        };
    }

    private EventsReaderXMLv1.CustomEventMapper<PFAVOwnerWaitsForVehicleEvent> getPFAVOwnerWaitsEventMapper() {
        return new EventsReaderXMLv1.CustomEventMapper<PFAVOwnerWaitsForVehicleEvent>() {
            @Override
            public PFAVOwnerWaitsForVehicleEvent apply(GenericEvent event) {

                Map<String, String> attributes = event.getAttributes();

                double time = Double.parseDouble(attributes.get(PFAVOwnerWaitsForVehicleEvent.ATTRIBUTE_TIME));
                Id<DvrpVehicle> vid = Id.create(attributes.get(PFAVOwnerWaitsForVehicleEvent.ATTRIBUTE_VEHICLE), DvrpVehicle.class);
                Id<Person> owner = Id.createPersonId(attributes.get(PFAVOwnerWaitsForVehicleEvent.ATTRIBUTE_OWNER));

                return new PFAVOwnerWaitsForVehicleEvent(time, vid, owner);
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
