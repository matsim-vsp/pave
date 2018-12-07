package ovgu.timewindows;


public class TimeWindow {
	
	int id;
	float begin = 0;
	float end = 0;

	
	public TimeWindow(){
		
	}


	public TimeWindow(int id, float begin, float end) {
		
		this.id = id;
		this.begin = begin;
		this.end = end;

	}
		
	
	public float getBegin() {
		return begin;
	}

	
	public void setBegin(float begin) {
		this.begin = begin;
	}
	
	public float getEnd() {
		return end;
	}

	
	public void setEnd(float end) {
		this.end = end;
	}

}
