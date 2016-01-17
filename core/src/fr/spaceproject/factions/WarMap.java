package fr.spaceproject.factions;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import fr.spaceproject.utils.Coor;

public class WarMap {
	private int discoverSector;
	private Map<String,Sector> World;
	
	public WarMap(){
		World =new LinkedHashMap<String,Sector>();
		for (int j=2;j>-3;j--){
			for (int i=-2;i<3;i++){
					this.World.put((new Coor(i,j)).toStrings(),new Sector());
			}
		}
		World.get("-2 -2").setNewAlignement(1);
		World.get("-2 2").setNewAlignement(2);
		World.get("2 2").setNewAlignement(3);
		World.get("2 -2").setNewAlignement(4);
		this.discoverSector =25;
	}
	
	public int appartCoor(String str){
		return this.World.get(str).getAlignement();
	}
	
	public void warBegin(Geopolitics state){
		for (int i=1;i<state.length();i++){
			warTurn(state.getFaction(i),state);
		}
	}
	
	private void warTurn(Faction team,Geopolitics state){
		Iterator<Entry<String, Sector>> it = World.entrySet().iterator();
		while (it.hasNext()){
			Entry<String, Sector> value = it.next();
			if (value.getValue().getAlignement()==team.getNumber()){
				battle(new Coor(value.getKey()),1,0,team,state);
				battle(new Coor(value.getKey()),-1,0,team,state);
				battle(new Coor(value.getKey()),0,-1,team,state);
				battle(new Coor(value.getKey()),0,1,team,state);
			}
		}
	}
	
	private void battle(Coor notimp,int x,int y,Faction team,Geopolitics state){
		String verif =notimp.addXY(x,y);
		if (World.containsKey(verif) && World.get(verif).getAlignement()!=team.getNumber() && Math.random()<Math.pow((1.0-((float)team.getTerritories()/this.discoverSector)),7)*0.80){
			if (Math.random()<0.2){//0.5-0.5*((float)(state.getFaction(World.get(verif).getAlignement()).getTerritories()-team.getTerritories()))/(state.getFaction(World.get(verif).getAlignement()).getTerritories()+team.getTerritories()))
				team.winTerritorie();
				state.getFaction(World.get(verif).getAlignement()).loseTerritorie();
				World.get(verif).setNewAlignement(team.getNumber());
			
			}
		}
	}
}

