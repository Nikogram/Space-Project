package fr.spaceproject.factions;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import fr.spaceproject.utils.Coor;
import fr.spaceproject.utils.Time;

public class WarMap {
	
	private int discoverSector;
	private Map<String,Sector> World;
	private float timeSinceLastWar;
	
	public Sector getZone(String Coor){
		return World.get(Coor);
	}
	
	public WarMap(){
		World =new LinkedHashMap<String,Sector>();
		for (int j=2;j>-3;j--){
			for (int i=-2;i<3;i++){
					this.World.put((new Coor(i,j)).toStrings(),new Sector((int)(4*Math.random())+1));
			}
		}
		World.get("-2 -2").setNewAlignement(1);
		World.get("-2 2").setNewAlignement(2);
		World.get("2 2").setNewAlignement(3);
		World.get("2 -2").setNewAlignement(4);
		this.discoverSector =25;
		timeSinceLastWar = 0;
	}
	
	public void update(Time time,Geopolitics state,String posPlayer){
		if (time.getTime()-timeSinceLastWar>0.25){
			timeSinceLastWar=time.getTime();
			warBegin(state,time,posPlayer);
		}
		Iterator<Entry<String, Sector>> it = World.entrySet().iterator();
		while (it.hasNext()){
			Entry<String, Sector> value = it.next();
			if (value.getKey()!=posPlayer && value.getValue().isInWar() && (time.getTime()-value.getValue().TimeSinceInWar())>0.24){
				value.getValue().setPeace();
				if (Math.random()<0.2){//0.5-0.5*((float)(state.getFaction(World.get(verif).getAlignement()).getTerritories()-team.getTerritories()))/(state.getFaction(World.get(verif).getAlignement()).getTerritories()+team.getTerritories()))
					state.getFaction(value.getValue().getEnnemiAlignement()).winTerritorie();
					state.getFaction(value.getValue().getAlignement()).loseTerritorie();
					value.getValue().setNewAlignement(value.getValue().getEnnemiAlignement());
				
				}
			}
		}
	}
	
	public int appartCoor(String str){
		if (World.containsKey(str))
		return this.World.get(str).getAlignement();
		else{
			this.World.put(str,new Sector());
			return 0;
		}
	}
	
	public void warBegin(Geopolitics state,Time time,String posPlayer){
		for (int i=1;i<state.getNbTeam();i++){
			warTurn(state.getFaction(i),state,time,posPlayer);
		}
	}
	
	private void warTurn(Faction team,Geopolitics state,Time time,String posPlayer){
		Iterator<Entry<String, Sector>> it = World.entrySet().iterator();
		while (it.hasNext()){
			Entry<String, Sector> value = it.next();
			if (value.getValue().getAlignement()==team.getNumber() && !value.getValue().isInWar()){
				battle(new Coor(value.getKey()),1,0,team,state,time,posPlayer);
				battle(new Coor(value.getKey()),-1,0,team,state,time,posPlayer);
				battle(new Coor(value.getKey()),0,-1,team,state,time,posPlayer);
				battle(new Coor(value.getKey()),0,1,team,state,time,posPlayer);
			}
		}
	}
	
	private void battle(Coor notimp,int x,int y,Faction team,Geopolitics state,Time time,String posPlayer){
		String verif =notimp.addXY(x,y);
		if (verif!=posPlayer && World.containsKey(verif) && World.get(verif).getAlignement()!=team.getNumber() && !World.get(verif).isInWar() && Math.random()<Math.pow((1.0-((float)team.getTerritories()/this.discoverSector)),7)*0.80){
			World.get(verif).setWar(time,team.getNumber());
		}
	}
}

