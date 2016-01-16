package fr.spaceproject.factions;

public class Faction {
	private int team;
	private int totalTerritories;
	private int couleur;
	
	
	public Faction(){
		this.team=0;
		this.totalTerritories=0;
	}
	
	public Faction(int teamNb,int totalNb){
		this.team=teamNb;
		this.team=totalNb;
	}
	
	public void loseTerritorie(){
		this.totalTerritories -=1;
	}
	
	public void winTerritorie(){
		this.totalTerritories +=1;
	}
}
