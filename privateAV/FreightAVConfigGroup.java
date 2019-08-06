package privateAV;

import java.util.Map;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import org.matsim.core.config.ReflectiveConfigGroup;

public final class FreightAVConfigGroup extends ReflectiveConfigGroup {

	public static final String MODE = "mode";

	public static final String PFAV_TYPE = "pfavType";

	public static final String FREIGHTTOUR_LATEST_START = "freightTourLatestStart";
	static final String FREIGHTTOUR_LATEST_START_EXP = "represents the latest start time of a freight tour. that means, the retool task "
			+ "of a freight tour has to start before FREIGHTTOUR_LATEST_START. in the manager, the path to the "
			+ "depot is calculated. if the above mentioned condition is not fulfilled the freight tour will not "
			+ "be dispatched to the vehicle (with reason: not enough time to perform the tour";

	public static final String FREIGHTTOUR_EARLIEST_START = "freightTourEarliestStart";
	static final String FREIGHTTOUR_EARLIEST_START_EXP = "represents the earliest start time of a freight tour. that means, the retool task of "
			+ "a freight tour must not start before FREIGHTTOUR_EARLIEST_START. in the manager, the path to the depot is calculated. "
			+ "if needed, a stay task at the depot is inserted so that the PFAV waits in case.";

	public static final String PFAV_RETOOL_TIME = "pfavReToolTime";
	static final String PFAV_RETOOL_TIME_EXP = "the amount of time that is needed to rebuild/retool the vehicle. "
			+ "that means how much time is needed to change the module on top of the car body";

	public static final String TIME_BUFFER = "timeBuffer";
	static final String TIME_BUFFER_EXP = "the amount of time that the vehicle wants to arrive before it's owner ends the activity";

	public static final String FREIGHT_DEMAND_SAMPLE_SIZE = "freightDemandSampleSize";
	static final String FREIGHT_DEMAND_SAMPLE_SIZE_EXP = "can be used to cut down the amount of freight tours on the manager's "
			+ "to do list for the iteration. The set of freight tours remains constant until freight tour calculation "
			+ "is run for the next time, see TOURPLANNING_INTERVAL.";

	public static final String TOURPLANNING_INTERVAL = "tourPlanningInterval";
	static final String TOURPLANNING_INTERVAL_EXP = "the freight contrib will be run before every iteration where iterationNumber "
			+ "% TOURPLANNING_INTERVAL == 0- \n if TOURPLANNING_INTERVAL is set to 0 or any negative integer, "
			+ "the freight contrib will run only before iteration 0.";

	public static final String RUN_TOUR_PLANNING_BEFORE_FIRST_ITERATION = "runTourPlanningBeforeFirstIteration";
	static final String RUN_TOUR_PLANNING_BEFORE_FIRST_ITERATION_EXP = "defines whether the freight tour manager triggers the JSprit run in iteration 0";

	public static final String NR_OF_JSPRIT_ITERATIONS = "nrOfJspritIterations";
	static final String NR_OF_JSPRIT_ITERATIONS_EXP = "defines the number of iterations in the tour planning algorithm, see implementation of {@link FreightTourPlanning}";

	public static final String ALLOW_EMPTY_TOUR_LISTS_FOR_DEPOTS = "allowEmptyTourListsForDepots";
	static final String ALLOW_EMPTY_TOUR_LISTS_FOR_DEPOTS_EXP = "this is a switch, which determines whether the manager is holding depots in it's "
			+ "map that have no tour (anymore) to serve. /n set this to true for usecases in which you look only at a subset of depots. "
			+ "in order to have a correct dispatch, you need to have /n a) one carrier per depot /n b) all depots in the initial carrier file "
			+ "/n c) only those carriers that you want to look at to contain services /n set this to false if you want to enable long dispatch distances. "
			+ "setting this to false means that, over day time, the manager's set of depot shrinks. consequently, "
			+ "the spatial dispatch algorithm allows longer distance's to depot's as it considers the closest three depots that have a "
			+ "tour left to serve. For example: the closest two depots to a vehicle that has requested a tour are already fully "
			+ "served /have no tour left to serve...";

	public static final String MAX_BEELINE_DISTANCE_TO_DEPOT = "maxBeelineDistanceToDepot";
	static final String MAX_BEELINE_DISTANCE_TO_DEPOT_EXP = "the maximum distance to a depot that is accepted. that means, a vehicle that is "
			+ "further away (at the moment of the request) than MAX_BEELINE_DISTANCE_TO_DEPOT from a depot, will not be dispatched to a starting "
			+ "from that depot.";

	public static final String MAX_TRAVELTIME_TO_DEPOT = "maxTravelTimeToDepot";
	static final String MAX_TRAVELTIME_TO_DEPOT_EXP = "the maximum travel time to a depot that is accepted. that means, a vehicle for "
			+ "which the access drive takes longer than MAX_TRAVELTIME_TO_DEPOT will not "
			+ "be dispatched to a starting from that depot.";

	public static final String AMOUNT_OF_DEPOTS_TO_CONSIDER = "amountOfDepotsToConsider";
	static final String AMOUNT_OF_DEPOTS_TO_CONSIDER_EXP = "defines the number of depots which are considered for the dispatch of a freight tour to a PFAV. "
			+ "in the dispatch algorithm, the closest AMOUNT_OF_DEPOTS_TO_CONSIDER to the PFAV'S request location are searched, based on beeline distance. "
			+ "/n if you only look at one depot out of a bigger set of depot, you should set this value to 1 and set ALLOW_EMPTY_TOUR_LISTS_FOR_DEPOTS true";

	public static final String CONSIDER_SERVICE_TIMEWINDOWS_FOR_DISPATCH = "considerServiceTimeWindowsForDispatch";
	static final String CONSIDER_SERVICE_TIMEWINDOWS_FOR_DISPATCH_EXP = "this boolean determines whether the implementation of {@link FreightTourManagerListBased} "
			+ "looks at the latest start of the last service in a tour and compares it with the approximated arrival time (computed by travel "
			+ "time to depot + planned tour duration til last service (derived out of freight tour))";

	public static final String RE_ROUTE_TOURS = "reRouteTours";
	static final String RE_ROUTE_TOURS_EXP = "determines whether the drives within the freight tour are reRouted when the freight tour is dispatched";

	public static final String ALLOW_MULTIPLE_TOURS_IN_A_ROW = "allowMultipleToursInaRow";
	static final String ALLOW_MULTIPLE_TOURS_IN_A_ROW_EXP = "when true, the vehicle will immediately return to it's owner's activity location and wait there. "
			+ "if false, it might perform another freight tour after having performed the last PFAVServiceTask.";

	@NotBlank
	private String mode = "pfav";

	@NotBlank
	private String pfavType = "PFAV";

	@Positive
	private double freightTourLatestStart = 18 * 3600;

	@Positive
	private double freightTourEarliestStart = 8 * 3600;

	@Positive
	private double pfavReToolTime = 15 * 60;

	@Positive
	private double timeBuffer = 5 * 60;

	@DecimalMin("1.0")
	private double freightDemandSampleSize = 1.;

	@Positive
	private int tourPlanningInterval = 1;

	private boolean runTourPlanningBeforeFirstIteration = false;

	@Positive
	private int nrOfJspritIterations = 300;

	private boolean allowEmptyTourListsForDepots = true;

	@Positive
	private long maxBeelineDistanceToDepot = 10000;

	@Positive
	private double maxTravelTimeToDepot = 20 * 60;

	@Positive
	private int amountOfDepotsToConsider = 3;

	private boolean considerServiceTimeWindowsForDispatch = false;

	private boolean reRouteTours = false;

	private boolean allowMultipleToursInaRow = true;

	/**
	 * @return the mode
	 */
	@StringGetter(MODE)
	public String getMode() {
		return mode;
	}

	/**
	 * @param mode
	 *            the mode to set
	 */
	@StringSetter(MODE)
	public void setMode(String mode) {
		this.mode = mode;
	}

	/**
	 * @return the pfavType
	 */
	@StringGetter(PFAV_TYPE)
	public String getPfavType() {
		return pfavType;
	}

	/**
	 * @param pfavType
	 *            the pfavType to set
	 */
	@StringSetter(PFAV_TYPE)
	public void setPfavType(String pfavType) {
		this.pfavType = pfavType;
	}

	/**
	 * @return the freightTourLatestStart
	 */
	@StringGetter(FREIGHTTOUR_LATEST_START)
	public double getFreightTourLatestStart() {
		return freightTourLatestStart;
	}

	/**
	 * @param freightTourLatestStart
	 *            the freightTourLatestStart to set
	 */
	@StringSetter(FREIGHTTOUR_LATEST_START)
	public void setFreightTourLatestStart(double freightTourLatestStart) {
		this.freightTourLatestStart = freightTourLatestStart;
	}

	/**
	 * @return the freightTourEarliestStart
	 */
	@StringGetter(FREIGHTTOUR_EARLIEST_START)
	public double getFreightTourEarliestStart() {
		return freightTourEarliestStart;
	}

	/**
	 * @param freightTourEarliestStart
	 *            the freightTourEarliestStart to set
	 */
	@StringSetter(FREIGHTTOUR_EARLIEST_START)
	public void setFreightTourEarliestStart(double freightTourEarliestStart) {
		this.freightTourEarliestStart = freightTourEarliestStart;
	}

	/**
	 * @return the pfavReToolTime
	 */
	@StringGetter(PFAV_RETOOL_TIME)
	public double getPfavReToolTime() {
		return pfavReToolTime;
	}

	/**
	 * @param pfavReToolTime
	 *            the pfavReToolTime to set
	 */
	@StringSetter(PFAV_RETOOL_TIME)
	public void setPfavReToolTime(double pfavReToolTime) {
		this.pfavReToolTime = pfavReToolTime;
	}

	/**
	 * @return the timeBuffer
	 */
	@StringGetter(TIME_BUFFER)
	public double getTimeBuffer() {
		return timeBuffer;
	}

	/**
	 * @param timeBuffer
	 *            the timeBuffer to set
	 */
	@StringSetter(TIME_BUFFER)
	public void setTimeBuffer(double timeBuffer) {
		this.timeBuffer = timeBuffer;
	}

	/**
	 * @return the freightDemandSampleSize
	 */
	@StringGetter(FREIGHT_DEMAND_SAMPLE_SIZE)
	public double getFreightDemandSampleSize() {
		return freightDemandSampleSize;
	}

	/**
	 * @param freightDemandSampleSize
	 *            the freightDemandSampleSize to set
	 */
	@StringSetter(FREIGHT_DEMAND_SAMPLE_SIZE)
	public void setFreightDemandSampleSize(double freightDemandSampleSize) {
		this.freightDemandSampleSize = freightDemandSampleSize;
	}

	/**
	 * @return the tourPlanningInterval
	 */
	@StringGetter(TOURPLANNING_INTERVAL)
	public int getTourPlanningInterval() {
		return tourPlanningInterval;
	}

	/**
	 * @param tourPlanningInterval
	 *            the tourPlanningInterval to set
	 */
	@StringSetter(TOURPLANNING_INTERVAL)
	public void setTourPlanningInterval(int tourPlanningInterval) {
		this.tourPlanningInterval = tourPlanningInterval;
	}

	/**
	 * @return the runTourPlanningBeforeFirstIteration
	 */
	@StringGetter(RUN_TOUR_PLANNING_BEFORE_FIRST_ITERATION)
	public boolean isRunTourPlanningBeforeFirstIteration() {
		return runTourPlanningBeforeFirstIteration;
	}

	/**
	 * @param runTourPlanningBeforeFirstIteration
	 *            the runTourPlanningBeforeFirstIteration to set
	 */
	@StringSetter(RUN_TOUR_PLANNING_BEFORE_FIRST_ITERATION)
	public void setRunTourPlanningBeforeFirstIteration(boolean runTourPlanningBeforeFirstIteration) {
		this.runTourPlanningBeforeFirstIteration = runTourPlanningBeforeFirstIteration;
	}

	/**
	 * @return the nrOfJspritIterations
	 */
	@StringGetter(NR_OF_JSPRIT_ITERATIONS)
	public int getNrOfJspritIterations() {
		return nrOfJspritIterations;
	}

	/**
	 * @param nrOfJspritIterations
	 *            the nrOfJspritIterations to set
	 */
	@StringSetter(NR_OF_JSPRIT_ITERATIONS)
	public void setNrOfJspritIterations(int nrOfJspritIterations) {
		this.nrOfJspritIterations = nrOfJspritIterations;
	}

	/**
	 * @return the allowEmptyTourListsForDepots
	 */
	@StringGetter(ALLOW_EMPTY_TOUR_LISTS_FOR_DEPOTS)
	public boolean isAllowEmptyTourListsForDepots() {
		return allowEmptyTourListsForDepots;
	}

	/**
	 * @param allowEmptyTourListsForDepots
	 *            the allowEmptyTourListsForDepots to set
	 */
	@StringSetter(ALLOW_EMPTY_TOUR_LISTS_FOR_DEPOTS)
	public void setAllowEmptyTourListsForDepots(boolean allowEmptyTourListsForDepots) {
		this.allowEmptyTourListsForDepots = allowEmptyTourListsForDepots;
	}

	/**
	 * @return the maxBeelineDistanceToDepot
	 */
	@StringGetter(MAX_BEELINE_DISTANCE_TO_DEPOT)
	public long getMaxBeelineDistanceToDepot() {
		return maxBeelineDistanceToDepot;
	}

	/**
	 * @param maxBeelineDistanceToDepot
	 *            the maxBeelineDistanceToDepot to set
	 */
	@StringSetter(MAX_BEELINE_DISTANCE_TO_DEPOT)
	public void setMaxBeelineDistanceToDepot(long maxBeelineDistanceToDepot) {
		this.maxBeelineDistanceToDepot = maxBeelineDistanceToDepot;
	}

	/**
	 * @return the maxTravelTimeToDepot
	 */
	@StringGetter(MAX_TRAVELTIME_TO_DEPOT)
	public double getMaxTravelTimeToDepot() {
		return maxTravelTimeToDepot;
	}

	/**
	 * @param maxTravelTimeToDepot
	 *            the maxTravelTimeToDepot to set
	 */
	@StringSetter(MAX_TRAVELTIME_TO_DEPOT)
	public void setMaxTravelTimeToDepot(double maxTravelTimeToDepot) {
		this.maxTravelTimeToDepot = maxTravelTimeToDepot;
	}

	/**
	 * @return the amountOfDepotsToConsider
	 */
	@StringGetter(AMOUNT_OF_DEPOTS_TO_CONSIDER)
	public int getAmountOfDepotsToConsider() {
		return amountOfDepotsToConsider;
	}

	/**
	 * @param amountOfDepotsToConsider
	 *            the amountOfDepotsToConsider to set
	 */
	@StringSetter(AMOUNT_OF_DEPOTS_TO_CONSIDER)
	public void setAmountOfDepotsToConsider(int amountOfDepotsToConsider) {
		this.amountOfDepotsToConsider = amountOfDepotsToConsider;
	}

	/**
	 * @return the considerServiceTimeWindowsForDispatch
	 */
	@StringGetter(CONSIDER_SERVICE_TIMEWINDOWS_FOR_DISPATCH)
	public boolean isConsiderServiceTimeWindowsForDispatch() {
		return considerServiceTimeWindowsForDispatch;
	}

	/**
	 * @param considerServiceTimeWindowsForDispatch
	 *            the considerServiceTimeWindowsForDispatch to set
	 */
	@StringSetter(CONSIDER_SERVICE_TIMEWINDOWS_FOR_DISPATCH)
	public void setConsiderServiceTimeWindowsForDispatch(boolean considerServiceTimeWindowsForDispatch) {
		this.considerServiceTimeWindowsForDispatch = considerServiceTimeWindowsForDispatch;
	}

	/**
	 * @return the reRouteTours
	 */
	@StringGetter(RE_ROUTE_TOURS)
	public boolean isReRouteTours() {
		return reRouteTours;
	}

	/**
	 * @param reRouteTours
	 *            the reRouteTours to set
	 */
	@StringSetter(RE_ROUTE_TOURS)
	public void setReRouteTours(boolean reRouteTours) {
		this.reRouteTours = reRouteTours;
	}

	/**
	 * @return the allowMultipleToursInaRow
	 */
	@StringGetter(ALLOW_MULTIPLE_TOURS_IN_A_ROW)
	public boolean isAllowMultipleToursInaRow() {
		return allowMultipleToursInaRow;
	}

	/**
	 * @param allowMultipleToursInaRow
	 *            the allowMultipleToursInaRow to set
	 */
	@StringSetter(ALLOW_MULTIPLE_TOURS_IN_A_ROW)
	public void setAllowMultipleToursInaRow(boolean allowMultipleToursInaRow) {
		this.allowMultipleToursInaRow = allowMultipleToursInaRow;
	}

	@Override
	public Map<String, String> getComments() {
		
		Map<String, String> map = super.getComments();
		map.put(FREIGHTTOUR_LATEST_START, FREIGHTTOUR_LATEST_START_EXP);
		map.put(FREIGHTTOUR_EARLIEST_START, FREIGHTTOUR_EARLIEST_START_EXP);
		map.put(PFAV_RETOOL_TIME, PFAV_RETOOL_TIME_EXP);
		map.put(TIME_BUFFER, TIME_BUFFER_EXP);
		map.put(FREIGHT_DEMAND_SAMPLE_SIZE, FREIGHT_DEMAND_SAMPLE_SIZE_EXP);
		map.put(TOURPLANNING_INTERVAL, TOURPLANNING_INTERVAL_EXP);
		map.put(RUN_TOUR_PLANNING_BEFORE_FIRST_ITERATION, RUN_TOUR_PLANNING_BEFORE_FIRST_ITERATION_EXP);
		map.put(NR_OF_JSPRIT_ITERATIONS, NR_OF_JSPRIT_ITERATIONS_EXP);
		map.put(ALLOW_EMPTY_TOUR_LISTS_FOR_DEPOTS, ALLOW_EMPTY_TOUR_LISTS_FOR_DEPOTS_EXP);
		map.put(MAX_BEELINE_DISTANCE_TO_DEPOT, MAX_BEELINE_DISTANCE_TO_DEPOT_EXP);
		map.put(MAX_TRAVELTIME_TO_DEPOT, MAX_TRAVELTIME_TO_DEPOT_EXP);
		map.put(AMOUNT_OF_DEPOTS_TO_CONSIDER, AMOUNT_OF_DEPOTS_TO_CONSIDER_EXP);
		map.put(CONSIDER_SERVICE_TIMEWINDOWS_FOR_DISPATCH, CONSIDER_SERVICE_TIMEWINDOWS_FOR_DISPATCH_EXP);
		map.put(RE_ROUTE_TOURS, RE_ROUTE_TOURS_EXP);
		map.put(ALLOW_MULTIPLE_TOURS_IN_A_ROW, ALLOW_MULTIPLE_TOURS_IN_A_ROW_EXP);
		
		return map;
	}
	
	public FreightAVConfigGroup(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

}
