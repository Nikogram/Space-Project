package fr.spaceproject.factions;

import java.util.Map;

import fr.spaceproject.utils.Vec2f;

public class WarMap {
	
	public Map<Vec2f,Sector> World;
	
	public WarMap(){
		this.World.put(new Vec2f(0,0),new Sector());
		this.World.put(new Vec2f(-1,-1),new Sector(1));
		this.World.put(new Vec2f(-1,1),new Sector(2));
		this.World.put(new Vec2f(1,1),new Sector(3));
		this.World.put(new Vec2f(1,-1),new Sector(4));
	}
	
	public void Warbegin(int team){
		
	}
	
	
}

