package fr.spaceproject.factions;

public class Faction {
	private int team;
	private int totalTerritories;
	
	
	public Faction(){
		this.team=0;
		this.totalTerritories=0;
	}
	
	public Faction(int teamNb,int totalNb){
		this.team=teamNb;
		this.totalTerritories=totalNb;
	}
	
	public void setTeam(int i){
		team=i;
	}
	
	
	
	public int getNumber(){
		return team;
	}
	
	public int getTerritories(){
		return totalTerritories;
	}
	
	public void loseTerritorie(){
		this.totalTerritories -=1;
	}
	
	public void winTerritorie(){
		this.totalTerritories +=1;
	}
}
