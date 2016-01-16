package fr.spaceproject.factions;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import fr.spaceproject.utils.Coor;

public class WarMap {
	
	private Map<String,Sector> World;
	
	public WarMap(){
		World =new LinkedHashMap<String,Sector>();
		this.World.put((new Coor(0,0)).toStrings(),new Sector());
		this.World.put((new Coor(-1,-1)).toStrings(),new Sector(1));
		this.World.put((new Coor(-1,1)).toStrings(),new Sector(2));
		this.World.put((new Coor(1,1)).toStrings(),new Sector(3));
		this.World.put((new Coor(1,-1)).toStrings(),new Sector(4));
		this.World.put((new Coor(1,0)).toStrings(),new Sector());
		this.World.put((new Coor(-1,0)).toStrings(),new Sector());
		this.World.put((new Coor(0,-1)).toStrings(),new Sector());
		this.World.put((new Coor(0,1)).toStrings(),new Sector());
	}
	
	public int appartCoor(String str){
		return this.World.get(str).getAlignement();
	}
	
	
	public void warBegin(int team){
		Iterator<Entry<String, Sector>> it = World.entrySet().iterator();
		while (it.hasNext()){
			Entry<String, Sector> value = it.next();
			if (value.getValue().getAlignement()==team){
				battle(new Coor(value.getKey()),1,0,team);
				battle(new Coor(value.getKey()),-1,0,team);
				battle(new Coor(value.getKey()),0,-1,team);
				battle(new Coor(value.getKey()),0,1,team);
			}
		}
	}
	
	private void battle(Coor notimp,int x,int y,int team){
		String verif =notimp.addXY(x,y);
		if (World.containsKey(verif) && World.get(verif).getAlignement()!=team && Math.random()<0.2){
			if (Math.random()<0.5){
			World.get(verif).setNewAlignement(team);
			}
		}
	}
}

