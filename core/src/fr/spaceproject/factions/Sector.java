package fr.spaceproject.factions;


public class Sector {

	private int alignement;
	private boolean isInWar;
	private float timeWarBegin;

	public Sector() {
		this.alignement = 0;
		this.isInWar = false;
	}
	
	public Sector(int Party) {
		this.alignement = Party;
	}
	
	public int getAlignement() {
		return alignement;
	}
	
	public void setWar() {
		this.isInWar = true;
		this.timeWarBegin = System.currentTimeMillis();
	}
	
	public void setNewAlignement(int party) {
		this.alignement = party;
	}
	
}
