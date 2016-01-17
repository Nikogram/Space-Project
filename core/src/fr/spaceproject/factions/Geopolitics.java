package fr.spaceproject.factions;

public class Geopolitics {
	
	private Faction[] state;
	private int length;
	
	public Geopolitics(int i){
		state=new Faction[i];
		length=i;
		for (int j=0;j<i;j++){
			state[j]=new Faction(j,1);
		}
	}
	public Faction getFaction(int i){
		return state[i];
	}
	public int length(){
		return length;
	}
}
