package fr.spaceproject.factions;

public class Faction {
	private int team;
	private int totalTerritories;
	private String color;
	
	
	public Faction(){
		this.team=0;
		this.totalTerritories=0;
	}
	
	public Faction(int teamNb,int totalNb,String color){
		this.team=teamNb;
		this.totalTerritories=totalNb;
		this.color=color;
	}
	
	public void loseTerritorie(){
		this.totalTerritories -=1;
	}
	
	public void winTerritorie(){
		this.totalTerritories +=1;
	}
}
