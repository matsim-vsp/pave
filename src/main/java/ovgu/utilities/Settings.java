package ovgu.utilities;

public class Settings{
	
	//Main
	public static final int deliveryArea = 0; // Hamburg = 0, M�nchen = 1, Ruhrgebiet = 2
	// yyyy replace by enum
	enum DeliveryArea { Hamburg, Muenchen, Ruhrgebiet } ;

	public static final float regionMultiplier = 1f;
	
	public static final int timeWindowDistribution = 0; //Random = 0, AYN = 1

	public static final int expSize = 2;
	public static final int algorithm  = 1; //Greedy = 0, LMNS = 1, ALNS = 2
	public static final int numberOfVehicles = 2;
	public static final int numberOfCustomers = 100;
	public static final int numberOfInstances = 1;
		
	//ALNS 
	public static final int improvementEvent = 100;
	public static final int iterations = 25000;
	public static final int waitingForImprovment = 25000;
	public static final double temperatureControlParameter = 0.05;
	public static final double coolingRate = 0.99975;
	
	//LMNS 
	public static final int  maxTimeMultiplyer = 10;
	public static final long maxTime = maxTimeMultiplyer*1000000;
	public static final int acceptanceCriteria = 1;
	public static final double deteriorationDegree = 0.4;

	//RemovalHeurstics
	public static final int removeLarge1 = 35;
	public static final int removeLarge2 = 65;
	public static final double removeSmall1 = 0.1;
	public static final double removeSmall2 = 0.2;
	public static final float shawDistance = 3f;
	public static final float shawTime = 6f;
}

