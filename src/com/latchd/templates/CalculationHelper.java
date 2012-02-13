package com.latchd.templates;

public class CalculationHelper {

	public static float CalcWeightForReps(int reps,float onerepmax){
		float result = onerepmax;
		
		double conv = Math.ceil(onerepmax / ((0.03333 * reps) + 1));
		//Log.d("Calculation","Actual 5 Rep is "+conv);
		result = (int) Math.ceil(conv/5)*5;
		
		return result;
	}
	
	public static float CalcWeightForPct(double pct,float onerepmax){
		float result = onerepmax;
		
		double conv = Math.ceil(onerepmax * pct);
		//Log.d("Calculation","Actual 5 Rep is "+conv);
		result = (int) Math.ceil(conv/5)*5;
		
		return result;
	}
	
	public static float CalcWeightForPct(double pct,float onerepmax, boolean useBarWeight){
		float result = onerepmax;
		
		double conv = Math.ceil(onerepmax * pct);
		
		if(useBarWeight){
			result = (int) Math.ceil(conv/5)*5;
		}else{
			result = (int) conv;
		}
		
		return result;
	}
	
	public static int CalcWeightForPct(double pct,double onerepmax){
		int result;
		
		double conv = Math.ceil(onerepmax * pct);
		result = (int) Math.ceil(conv/5)*5;
		
		return result;
	}
	
	public static int GetNearestBarWeight(float weight){
		return (int) Math.ceil(weight/5)*5;
	}
	
}
