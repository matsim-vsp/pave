package ovgu.utilities;

public class Settings{
	
	//Main
	public static int deliveryArea = 0; // Hamburg = 0, M�nchen = 1, Ruhrgebiet = 2 
	public static float regionMultiplier = 1f;
	
	public static int timeWindowDistribution = 0; //Random = 0, AYN = 1

	public static int expSize = 2;
	public static int algorithm  = 1; //Greedy = 0, LMNS = 1, ALNS = 2
	public static int numberOfVehicles = 2;
	public static int numberOfCustomers = 100;
	public static int numberOfInstances = 1;
		
	//ALNS 
	public static int improvementEvent = 100;
	public static int iterations = 25000;
	public static int waitingForImprovment = 25000;	
	public static double temperatureControlParameter = 0.05;
	public static double coolingRate = 0.99975;
	
	//LMNS 
	public static int  maxTimeMultiplyer = 10;
	public static long maxTime = maxTimeMultiplyer*1000000;
	public static int acceptanceCriteria = 1;
	public static double deteriorationDegree = 0.4;

	//RemovalHeurstics
	public static int removeLarge1 = 35;
	public static int removeLarge2 = 65;
	public static double removeSmall1 = 0.1;
	public static double removeSmall2 = 0.2;
	public static float shawDistance = 3f;
	public static float shawTime = 6f;
}

