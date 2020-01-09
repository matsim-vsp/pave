package org.matsim.scenarioCreation;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.freight.carrier.Carrier;
import org.matsim.contrib.freight.carrier.CarrierImpl;
import org.matsim.contrib.freight.carrier.CarrierPlan;
import org.matsim.contrib.freight.carrier.CarrierPlanXmlWriterV2;
import org.matsim.contrib.freight.carrier.CarrierService;
import org.matsim.contrib.freight.carrier.CarrierVehicle;
import org.matsim.contrib.freight.carrier.Carriers;
import org.matsim.contrib.freight.carrier.ScheduledTour;
import org.matsim.contrib.freight.carrier.TimeWindow;
import org.matsim.contrib.freight.carrier.Tour;
import org.matsim.contrib.freight.carrier.Tour.Builder;
import org.matsim.contrib.freight.carrier.Tour.Leg;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.network.filter.NetworkFilterManager;
import org.matsim.core.network.io.MatsimNetworkReader;
import org.matsim.core.utils.geometry.CoordinateTransformation;
import org.matsim.core.utils.geometry.transformations.TransformationFactory;
import org.matsim.vehicles.Vehicle;
import org.matsim.vehicles.VehicleType;

public class ConvertParcelData2CarrierPlans {

	private static final String INPUT_RAW_DATA_FILE = "D:/svn/shared-svn/studies/countries/de/berlin_hermes/HERMES_RawData_Geocoded.csv";

	//output will be written to scenarios/berlin/input/[OUTPUT_CARRIERS_FILE_NAME]
	//however everything inside scenarios/berlin/input is currently ignored by git (see .gitignore)
	private static final String OUTPUT_CARRIERS_FILE_NAME = "parcelDemand_convertedRawData.xml";
	static String networkPath = "https://svn.vsp.tu-berlin.de/repos/public-svn/matsim/scenarios/countries/de/berlin/berlin-v5.5-10pct/input/berlin-v5.5-network.xml.gz";
	static int latitudeColumn;
	static int longitideColumn;
	static int weightColumn;
	static int sizeColumn;
	static int returnPacketColumn;
	static int carrierColumn;
	static int tourNumberColumn;
	static int volumeColumn;
	static int time;
	static double prevLat = 0;
	static double prevLong = 0;
	static List<ScheduledTour> scheduledTourList = new ArrayList<ScheduledTour>();
	static List<CarrierService> services = new ArrayList<CarrierService>();

	public static void main(String[] args) {
		readDataFile();

	}

	private static void readDataFile() {

		String line = "";
		BufferedReader br;
		String carrierName = null;
		Carrier carrier = null;
		Carriers carriers = new Carriers();
		int tourNumber = 0;
		int previousTourNumber = 0;

		Network newNetOnlyCar = getFilteredBerlinNetwork();

		try {
			br = new BufferedReader(new FileReader(INPUT_RAW_DATA_FILE));
			String firstRow = br.readLine();
			String[] columnNames = firstRow.split(",");

			decideColumns(columnNames);

			int n = 0;
			while ((line = br.readLine()) != null) {
				String[] column = line.split(",");
				String compareCarrier = column[carrierColumn];
				tourNumber = Integer.valueOf(column[tourNumberColumn]);

				if (previousTourNumber == 0 || previousTourNumber != tourNumber) {
					if (previousTourNumber != 0) {
						Iterator<CarrierService> serviceItr = services.iterator();
						Builder tourBuilder = Tour.Builder.newInstance();
						Leg leg = tourBuilder.createLeg();
						tourBuilder.scheduleStart(null).addLeg(leg);
						while (serviceItr.hasNext()) {
							CarrierService service = serviceItr.next();
							tourBuilder.scheduleService(service);
							tourBuilder.addLeg(leg);
						}
						tourBuilder.scheduleEnd(null);
						Tour tour = tourBuilder.build();
						CarrierVehicle carrierVehicle = carrier.getCarrierCapabilities().getCarrierVehicles().get(Id.create(carrierName + "_vehicle", CarrierVehicle.class));
						ScheduledTour scheduledTour = ScheduledTour.newInstance(tour,
								carrierVehicle,
								0);
						scheduledTourList.add(scheduledTour);
					}
					services = new ArrayList<CarrierService>();
					previousTourNumber = tourNumber;
				} else {
					previousTourNumber = tourNumber;
				}

//				double latitude = (isNumeric(column[latitudeColumn])) ? Double.valueOf(column[latitudeColumn])
//						: prevLat;
//				double longitude = (isNumeric(column[longitideColumn])) ? Double.valueOf(column[longitideColumn])
//						: prevLong;
				double latitude = (isNumeric(column[latitudeColumn])) ? Double.valueOf(column[latitudeColumn])
						: Double.MIN_VALUE;
				double longitude = (isNumeric(column[longitideColumn])) ? Double.valueOf(column[longitideColumn])
						: Double.MIN_VALUE;
				int size = (isNumeric(column[sizeColumn])) ? Integer.valueOf(column[sizeColumn]) : 0;

				if (carrierName == null || !carrierName.equals(compareCarrier)) {
					if (carrierName != null) {
						CarrierPlan plan = new CarrierPlan(carrier, scheduledTourList);
						carrier.getPlans().add(plan);
						carrier.setSelectedPlan(plan);
						scheduledTourList = new ArrayList<ScheduledTour>();
					}
					carrierName = column[carrierColumn];
					carrier = CarrierImpl.newInstance(Id.create(carrierName, Carrier.class));
					CarrierVehicle vehicle = createCarrierVehicle(null,
							carrierName + "_vehicle");
					carrier.getCarrierCapabilities().getCarrierVehicles()
							.put(Id.create(carrierName + "_vehicle", Vehicle.class), vehicle);
					carriers.addCarrier(carrier);

					n = 0;
				} else {
					carrierName = column[carrierColumn];
				}
				CarrierService carrierService = createMatsimService(carrierName + n,
						getNetworkLinkId(newNetOnlyCar, latitude, longitude), size);
				carrierService.getAttributes().putAttribute("weight", column[weightColumn]);
				carrierService.getAttributes().putAttribute("return_packet", column[returnPacketColumn]);
				carrierService.getAttributes().putAttribute("volume", column[volumeColumn]);
				carrierService.getAttributes().putAttribute("tour_id", column[tourNumberColumn]);
				
				carrier.getServices().put(carrierService.getId(), carrierService);
				services.add(carrierService);

				n++;
			}
			String outputDir = "scenarios/berlin/input/";
			File outputDirFile = new File(outputDir);
			outputDirFile.mkdirs();
			CarrierPlanXmlWriterV2 planWriter = new CarrierPlanXmlWriterV2(carriers);
			planWriter.write(outputDir + OUTPUT_CARRIERS_FILE_NAME);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static Id<Link> getNetworkLinkId(Network network, double latitude, double longitude) {
//		prevLat = latitude + 0.05;
//		prevLong = longitude + 0.05;
		if(latitude == Double.MIN_VALUE || longitude == Double.MIN_VALUE){
			return null;
		}
		Coord coord = getTransformedCoordinates(latitude, longitude);
		Link link = NetworkUtils.getNearestLink(network, coord);
		Id<Link> linkid = link.getId();

		return linkid;
	}

	private static CarrierService createMatsimService(String id, Id<Link> linkId, int size) {
		return CarrierService.Builder.newInstance(Id.create(id, CarrierService.class), linkId).setCapacityDemand(size)
				.setServiceDuration(31.0).setServiceStartTimeWindow(TimeWindow.newInstance(0.0, 24 * 3600.0)).build();
	}

	private static Network getFilteredBerlinNetwork() {

		Network network = NetworkUtils.createNetwork();
		new MatsimNetworkReader(network).readFile(networkPath);

		NetworkFilterManager mng = new NetworkFilterManager(network);
		mng.addLinkFilter(l -> {
			return l.getAllowedModes().contains("car");
		});
		Network newNetOnlyCar = mng.applyFilters();

		return newNetOnlyCar;
	}

	private static Coord getTransformedCoordinates(double latitude, double longitude) {

		Coord coord = new Coord(longitude, latitude);
		CoordinateTransformation transformator = TransformationFactory
				.getCoordinateTransformation(TransformationFactory.WGS84, "EPSG:31464");
		coord = transformator.transform(coord);

		return coord;
	}

	private static boolean isNumeric(String strNum) {
		if (strNum == null) {
			return false;
		}
		try {
			Double.parseDouble(strNum);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	private static double formatTime(String dateTime) throws ParseException {

		String toParse = dateTime;
		Date date = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse(toParse);
		String time = new SimpleDateFormat("HH.mm").format(date);
		double formattedTime = Double.valueOf(time);
		return formattedTime;
	}

	public static CarrierVehicle createCarrierVehicle(Id<Link> locationId, String id) {
		return CarrierVehicle.Builder.newInstance(Id.create(id, org.matsim.vehicles.Vehicle.class), null)
				.setEarliestStart(0.0).setLatestEnd(36000.0).setTypeId(Id.create("HERMESE", VehicleType.class)).build();
	}

	private static void decideColumns(String[] columnNames) {

		int i = 0;
		while (i < columnNames.length) {
			String name = columnNames[i];
			switch (name) {

			case "latitude":
				latitudeColumn = i;
				break;

			case "longitude":
				longitideColumn = i;
				break;

			case "ANZAHL_SDG":
				sizeColumn = i;
				break;

			case "GEWICHT in g":
				weightColumn = i;
				break;

			case "ANZAHL_DAVON_RT":
				returnPacketColumn = i;
				break;

			case "ORG_EINHEITSNAME":
				carrierColumn = i;
				break;

			case "STH_SCAN_ZEITPUNKT":
				time = i;
				break;

			case "TGT_TOUR_NUMMER":
				tourNumberColumn = i;
				break;
				
			case "VOLUMEN in deziLiter":
				volumeColumn = i;
				break;
			}
			i++;
		}
	}

}
