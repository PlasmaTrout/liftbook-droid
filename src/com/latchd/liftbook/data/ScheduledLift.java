package com.latchd.liftbook.data;



public class ScheduledLift {

	private String date;
	private int sets;
	private int reps;
	private float weight;
	private LiftType type;
	private String name;
	private boolean generateWarmup;
	private int week;
	
	public ScheduledLift(String date,String name, int sets, int reps, float weight){
		this.date = date;
		this.sets = sets;
		this.reps = reps;
		this.weight = weight;
		this.name = name;
		this.type = LiftType.Main;
	}
	
	public ScheduledLift(String date,String name, int sets, int reps, float weight,LiftType type){
		this.date = date;
		this.sets = sets;
		this.reps = reps;
		this.weight = weight;
		this.type = type;
		this.name = name;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	public String getDate() {
		return date;
	}
	public void setSets(int sets) {
		this.sets = sets;
	}
	public int getSets() {
		return sets;
	}
	public void setReps(int reps) {
		this.reps = reps;
	}
	public int getReps() {
		return reps;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public float getWeight() {
		return weight;
	}
	public void setGenerateWarmup(boolean generateWarmup) {
		this.generateWarmup = generateWarmup;
	}
	public boolean isGenerateWarmup() {
		return generateWarmup;
	}

	public void setType(LiftType type) {
		this.type = type;
	}

	public LiftType getType() {
		return type;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setWeek(int week) {
		this.week = week;
	}

	public int getWeek() {
		return week;
	}
	
	
	
}
