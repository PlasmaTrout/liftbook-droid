package com.latchd.liftbook.data;

public class ScheduleEntry {

	private String date;
	private int liftcount;
	private int runcount;
	private String protocol;
	
	public ScheduleEntry(){
		
	}
	
	public ScheduleEntry(String date, int lifts, int runs, String protocol){
		this.date = date;
		this.liftcount = lifts;
		this.runcount = runs;
		this.protocol = protocol;
	}
	
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setLiftCount(int liftcount) {
		this.liftcount = liftcount;
	}
	
	public int getLiftCount() {
		return liftcount;
	}
	
	public void setRunCount(int runcount) {
		this.runcount = runcount;
	}
	
	public int getRunCount() {
		return runcount;
	}
	
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	
	public String getProtocol() {
		return protocol;
	}
	
}
