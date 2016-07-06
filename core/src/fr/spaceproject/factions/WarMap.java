package fr.spaceproject.factions;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;

import fr.spaceproject.utils.Coor;
import fr.spaceproject.utils.Time;

public class WarMap {
	
	private int discoverSector;
	private Map<String,Sector> World;
	private float timeSinceLastWar;
	
	public Sector getZone(String Coor){
		return World.get(Coor);
	}
	
	public WarMap(int x,int y){
		World =new LinkedHashMap<String,Sector>();
		int[][] disposition =dispo(x,y);
		for (int j=-(int)y/2;(int)j<y/2;j++){
			for (int i=-(int)x/2;i<(int)x/2;i++){
					this.World.put((new Coor(i,j)).toStrings(),new Sector(disposition[i+(int)x/2][j+(int)y/2]));
			}
		}
		this.discoverSector =100;
		timeSinceLastWar = 0;
	}
	
	
	private int[][] dispo(int x,int y){
		int abs,ord;
		int[][] disposition = new int[x][y];
		//System.out.println(disposition[0][0]);
		for (int i=1;i<5;i++){
			do{
				abs=(int)(Math.random()*x);
				ord=(int)(Math.random()*y);
			}while(disposition[abs][ord]!=0);
			disposition[abs][ord]=i;
		}
		return disposition;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	public void update(Time time,Geopolitics state,String posPlayer){
		if (time.getTime()-timeSinceLastWar>30 || time.getTime()<0.1){
			timeSinceLastWar=time.getTime();
			warBegin(state,time,posPlayer);
		}
		Iterator<Entry<String, Sector>> it = World.entrySet().iterator();
		while (it.hasNext()){
			Entry<String, Sector> value = it.next();
			if (!value.getKey().equals(posPlayer) && value.getValue().isInWar() && (time.getTime()-value.getValue().TimeSinceInWar())>29){
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
			this.World.put(str,new Sector((int)(4*Math.random()+1)));
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
		if (!verif.equals(posPlayer) && World.containsKey(verif) && World.get(verif).getAlignement()!=team.getNumber() && !World.get(verif).isInWar() && Math.random()<Math.pow((1.0-((float)team.getTerritories()/this.discoverSector)),7)*0.80){
			//System.out.println(verif);
			World.get(verif).setWar(time,team.getNumber());
		}
	}
}

