package fr.spaceproject.utils;

public class Time {
	private float time;
	
	public Time(){
		float time =0;
	}
	
	public void update(float lft){
		time +=lft;
	}
	
	public float getTime(){
		return time;
	}
}
