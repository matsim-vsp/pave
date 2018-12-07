package ovgu.timewindows;

import java.util.ArrayList;

public class TimeWindowList {

	ArrayList<TimeWindow> timeWindow = new ArrayList<TimeWindow>();
	
	public TimeWindowList(){
		
	}
	
	public TimeWindowList(ArrayList<TimeWindow> timeWindow) {
		this.timeWindow = timeWindow;
	}
	
	public void addTimeWindow(ArrayList<TimeWindow> timeWindow){
		

		int id = 1;
		for (int i = 600; i <= 1260; i = i + 120) {
			TimeWindow tw1 = new TimeWindow(id, i, i + 120);
			timeWindow.add(tw1);
			id++; 
		}
	}
	
	public ArrayList<TimeWindow> getTimeWindow() {
		return timeWindow;
	}
}
