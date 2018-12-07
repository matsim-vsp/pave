package ovgu.utilities;

import java.util.Scanner;

import ovgu.data.entity.RouteElement;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class DistanceMatrix {

	public float[][] distanceMatrix = new float [401][401];

	public void readDistanceMatrix() throws FileNotFoundException {
		
		Scanner scanIn = null;
		int Rowc = 0;
		String InputLine = "";
		switch (Settings.deliveryArea) {
    	case 0: scanIn = new Scanner (new BufferedReader (new FileReader("scenarios/ovgu" + "/Hamburg_Matrix.csv")));
    			break;
    	case 1: scanIn = new Scanner (new BufferedReader (new FileReader("scenarios/ovgu" + "/Muenchen_Matrix.csv")));
				break;
    	case 2: scanIn = new Scanner (new BufferedReader (new FileReader("scenarios/ovgu" + "/Ruhrgebiet_Matrix.csv")));
    			break;
		}
	
		while(scanIn.hasNextLine()){
			InputLine = scanIn.nextLine();
			String[] InArray = InputLine.split(",");
			for(int x=0; x<401 && Rowc<401; x++ ){
				distanceMatrix[Rowc][x] = Float.parseFloat(InArray[x]);
			}
			Rowc++;
		}
		scanIn.close();
	}

	 public float [][] getDistanceMatrix() {
         return this.distanceMatrix; 
     }
	 
	 public void setDistanceMatrix(float[][]distanceMatrix) {
			this.distanceMatrix = distanceMatrix;
		}
		
	 public float getTravelTime(RouteElement from, RouteElement to){	 
		return Settings.regionMultiplier*this.getDistanceMatrix()[from.getPositionDistanceMatrix()][to.getPositionDistanceMatrix()];
	}	

}
