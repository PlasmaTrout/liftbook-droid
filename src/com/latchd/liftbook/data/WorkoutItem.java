package com.latchd.liftbook.data;

public class WorkoutItem {
	
	private int rowId;
	private String date;
	private String name;
	private int sets;
	private int reps;
	private float weight;
	private float oneRepMax;
	private float todayOneRepMax;
	private float tonnage;
	private boolean isLift;
	private float miles;
	private float pace;
	private float mph;
	private String protocol;
	private boolean isCompleted;
	private int hours;
	private int minutes;
	private int seconds;
	
	public void setRowId(int rowId) {
		this.rowId = rowId;
	}
	public int getRowId() {
		return rowId;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getDate() {
		return date;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setOneRepMax(float oneRepMax) {
		this.oneRepMax = oneRepMax;
	}
	public float getOneRepMax() {
		return oneRepMax;
	}
	public void setTodayOneRepMax(float todayOneRepMax) {
		this.todayOneRepMax = todayOneRepMax;
	}
	public float getTodayOneRepMax() {
		return todayOneRepMax;
	}
	public void setTonnage(float tonnage) {
		this.tonnage = tonnage;
	}
	public float getTonnage() {
		return tonnage;
	}
	public void setLift(boolean isLift) {
		this.isLift = isLift;
	}
	public boolean isLift() {
		return isLift;
	}
	public void setMiles(float miles) {
		this.miles = miles;
	}
	public float getMiles() {
		return miles;
	}
	public void setPace(float pace) {
		this.pace = pace;
	}
	public float getPace() {
		return pace;
	}
	public void setMph(float mph) {
		this.mph = mph;
	}
	public float getMph() {
		return mph;
	}
	public void setSets(int sets) {
		this.sets = sets;
	}
	public int getSets() {
		return sets;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setCompleted(boolean isCompleted) {
		this.isCompleted = isCompleted;
	}
	public boolean isCompleted() {
		return isCompleted;
	}
	public void setReps(int reps) {
		this.reps = reps;
	}
	public int getReps() {
		return reps;
	}
	public void setWeight(float weight) {
		this.weight = weight;
	}
	public float getWeight() {
		return weight;
	}
	
	public int GetEpleyOneRepMax(){
		double result =  ((this.getWeight()*this.getReps())*0.03333)+this.getWeight();
		return ((int) result/5*5);
	}
	public void setHours(int hours) {
		this.hours = hours;
	}
	public int getHours() {
		return hours;
	}
	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}
	public int getMinutes() {
		return minutes;
	}
	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}
	public int getSeconds() {
		return seconds;
	}
	
}
