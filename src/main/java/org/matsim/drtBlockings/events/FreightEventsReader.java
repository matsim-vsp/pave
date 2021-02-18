package org.matsim.drtBlockings.events;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.events.ActivityEndEvent;
import org.matsim.api.core.v01.events.GenericEvent;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.population.Person;
import org.matsim.contrib.freight.carrier.Carrier;
import org.matsim.contrib.freight.carrier.CarrierService;
import org.matsim.contrib.freight.carrier.CarrierVehicle;
import org.matsim.contrib.freight.carrier.Tour;
import org.matsim.contrib.freight.events.LSPFreightLinkEnterEvent;
import org.matsim.contrib.freight.events.LSPServiceEndEvent;
import org.matsim.contrib.freight.events.LSPTourEndEvent;
import org.matsim.contrib.freight.events.LSPTourStartEvent;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.events.MatsimEventsReader;
import org.matsim.core.utils.io.MatsimXmlParser;
import org.matsim.facilities.ActivityFacility;
import org.matsim.facilities.Facility;
import org.matsim.vehicles.Vehicle;
import org.xml.sax.Attributes;

import java.util.Map;
import java.util.Stack;


public class FreightEventsReader extends MatsimXmlParser {

    private FreightEventsReader delegate;

    public FreightEventsReader(EventsManager events) {
        delegate = new FreightEventsReader(events);
        this.setValidating(false);

        delegate.addCustomEventMapper(LSPFreightLinkEnterEvent.EVENT_TYPE, this::lspFreightLinkEnterEvent);
        delegate.addCustomEventMapper(LSPTourStartEvent.EVENT_TYPE, this::lspTourStartEvent);
        delegate.addCustomEventMapper(LSPTourEndEvent.EVENT_TYPE, this::lspTourEndEvent);
        delegate.addCustomEventMapper(LSPServiceEndEvent.EVENT_TYPE, this::lspServiceEndEvent);

    }

    private LSPFreightLinkEnterEvent lspFreightLinkEnterEvent(GenericEvent event) {
        Map<String, String> attributes = event.getAttributes();

        Id<Carrier> carrierId = Id.create(attributes.get(LSPFreightLinkEnterEvent.ATTRIBUTE_CARRIER), Carrier.class);
        Id<Vehicle> vehicleId = Id.create(attributes.get(LSPFreightLinkEnterEvent.ATTRIBUTE_VEHICLE), Vehicle.class);
        Id<Person> driverId = Id.create(attributes.get(LSPFreightLinkEnterEvent.ATTRIBUTE_DRIVER), Person.class);
        Id<Link> linkId = Id.createLinkId(attributes.get(LSPFreightLinkEnterEvent.ATTRIBUTE_LINK));
        double time = Double.parseDouble(attributes.get(LSPFreightLinkEnterEvent.ATTRIBUTE_TIME));
        CarrierVehicle vehicle = CarrierVehicle.newInstance(vehicleId, linkId);

        return new LSPFreightLinkEnterEvent(carrierId, vehicleId, driverId, linkId, time, vehicle);
    }

    private LSPTourStartEvent lspTourStartEvent(GenericEvent event) {
        Map<String, String> attributes = event.getAttributes();

        Id<Vehicle> vehicleId = Id.create(attributes.get(LSPTourStartEvent.ATTRIBUTE_VEHICLE), Vehicle.class);
        Id<Link> linkId = Id.createLinkId(attributes.get(LSPTourStartEvent.ATTRIBUTE_LINK));

        Id<Carrier> carrierId = Id.create(attributes.get(LSPTourStartEvent.ATTRIBUTE_CARRIER), Carrier.class);
        Id<Person> driverId = Id.create(attributes.get(LSPTourStartEvent.ATTRIBUTE_DRIVER), Person.class);
        Tour tour = Tour.Builder.newInstance().build();
        Double time = Double.parseDouble(attributes.get(LSPTourStartEvent.ATTRIBUTE_TIME));
        CarrierVehicle vehicle = CarrierVehicle.newInstance(vehicleId, linkId);

        return new LSPTourStartEvent(carrierId, driverId, tour, time, vehicle);
    }

    private LSPTourEndEvent lspTourEndEvent(GenericEvent event) {
        Map<String, String> attributes = event.getAttributes();

        Id<Vehicle> vehicleId = Id.create(attributes.get(LSPTourEndEvent.ATTRIBUTE_VEHICLE), Vehicle.class);
        Id<Link> linkId = Id.createLinkId(attributes.get(LSPTourEndEvent.ATTRIBUTE_LINK));

        Id<Carrier> carrierId = Id.create(attributes.get(LSPTourEndEvent.ATTRIBUTE_CARRIER), Carrier.class);
        Id<Person> driverId = Id.create(attributes.get(LSPTourEndEvent.ATTRIBUTE_DRIVER), Person.class);
        Tour tour = Tour.Builder.newInstance().build();
        Double time = Double.parseDouble(attributes.get(LSPTourEndEvent.ATTRIBUTE_TIME));
        CarrierVehicle vehicle = CarrierVehicle.newInstance(vehicleId, linkId);

        return new LSPTourEndEvent(carrierId, driverId, tour, time, vehicle);
    }

    private LSPServiceEndEvent lspServiceEndEvent(GenericEvent event) {
        Map<String, String> attributes = event.getAttributes();

        Id<CarrierService> serviceId = Id.create(attributes.get(LSPServiceEndEvent.ATTRIBUTE_SERVICE), CarrierService.class);
        Id<Link> serviceLocationId = Id.createLinkId(attributes.get(LSPServiceEndEvent.ATTRIBUTE_LINK));
        Id<Vehicle> vehicleId = Id.create(attributes.get(LSPServiceEndEvent.ATTRIBUTE_VEHICLE), Vehicle.class);
        Id<ActivityFacility> facilityId = Id.create(attributes.get(ActivityEndEvent.ATTRIBUTE_FACILITY), ActivityFacility.class);
        String actType = attributes.get(ActivityEndEvent.ATTRIBUTE_TYPE);

        Id<Carrier> carrierId = Id.create(attributes.get(LSPServiceEndEvent.ATTRIBUTE_CARRIER), Carrier.class);
        Id<Person> driverId = Id.create(attributes.get(LSPServiceEndEvent.ATTRIBUTE_PERSON), Person.class);
        CarrierService service = CarrierService.Builder.newInstance(serviceId, serviceLocationId).build();
        Double time = Double.parseDouble(attributes.get(LSPServiceEndEvent.ATTRIBUTE_TIME));
        CarrierVehicle vehicle = CarrierVehicle.newInstance(vehicleId, serviceLocationId);
        ActivityEndEvent activityEndEvent = new ActivityEndEvent(time, driverId, serviceLocationId, facilityId, actType);

        return new LSPServiceEndEvent(activityEndEvent, carrierId, driverId, service, time, vehicle);
    }


    @Override
    public void startTag(String name, Attributes atts, Stack<String> context) {
        delegate.startTag(name, atts, context);
    }

    @Override
    public void endTag(String name, String content, Stack<String> context) {
        delegate.endTag(name, content, context);
    }

    public void addCustomEventMapper(String eventType, MatsimEventsReader.CustomEventMapper eventMapper) {
        delegate.addCustomEventMapper(eventType,eventMapper);
    }
}
