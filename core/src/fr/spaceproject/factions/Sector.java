package fr.spaceproject.factions;

import fr.spaceproject.utils.Time;


public class Sector {

	private int alignement;
	private boolean isInWar;
	private float timethisSectorinWar;
	private int ennemieAlignement;
	private float timethisSectorgoWar;
	
	public Sector() {
		this.alignement = 0;
		this.isInWar = false;
	}
	
	public Sector(int Team) {
		this.alignement = Team;
	}
	
	public float TimeSinceInWar(){
		return timethisSectorinWar;
	}
	
	
	public int getAlignement() {
		return alignement;
	}
	
	public void setWar(Time time,int ennemiTeam) {
		this.isInWar = true;
		this.timethisSectorinWar = time.getTime();
		this.ennemieAlignement = ennemiTeam;
	}
	
	public void setPeace(){
		this.isInWar = false;
	}
	
	public int getEnnemiAlignement(){
		return ennemieAlignement;
	}
	
	
	public void setNewAlignement(int party) {
		this.alignement = party;
	}
	public boolean isInWar(){
		return isInWar;
	}
}
