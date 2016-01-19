package fr.spaceproject.factions;

public class Geopolitics {
	
	private Faction[] state;
	private int length;
	private int[] agresivity;// 100 tres agressif 0 tres amical
	
	public Geopolitics(int i){
		agresivity=new int[i];
		for (int j=0;j<i;j++){
			agresivity[j]=25*j;
		}
		state=new Faction[i];
		length=i;
		for (int j=1;j<i;j++){
			state[j]=new Faction(j,1);
		}
			state[0]=new Faction(0,0);
	}
	public Faction getFaction(int i){
		return state[i];
	}
	public int length(){
		return length;
	}
	public int getAgressivity(int team){
		return agresivity[team];
	}

	public int[] getAgressivitys(){
		return agresivity;
	}
	
	public void addAgressivity(int team,int add){
		if (add<100-agresivity[team])
			agresivity[team]+=add;
		else
			agresivity[team]=100;
	}
	
	public void decAgressivity(int team,int dec){
		if (dec>-agresivity[team])
			agresivity[team]-=dec;
		else
			agresivity[team]=0;
	}
}
