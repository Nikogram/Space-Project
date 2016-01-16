package fr.spaceproject.factions;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import fr.spaceproject.utils.Vec2i;

public class WarMap {
	
	private Map<String,Sector> World;
	
	public WarMap(){
		World =new LinkedHashMap<String,Sector>();
		this.World.put("0 0",new Sector());
		this.World.put("-1 -1",new Sector(1));
		this.World.put("-1 1",new Sector(2));
		this.World.put("1 1",new Sector(3));
		this.World.put("1 -1",new Sector(4));
		this.World.put("1 0",new Sector());
		this.World.put("-1 0",new Sector());
		this.World.put("0 -1",new Sector());
		this.World.put("0 1",new Sector());
	}
	
	public int appartCoor(String str){
		return this.World.get(str).getAlignement();
	}
	
	
	/*public void Warbegin(int team){
		Iterator<Entry<String, Sector>> it = World.entrySet().iterator();
		while (it.hasNext()){
			Entry<String, Sector> value = it.next();
				if (value.getValue().getAlignement()==team){
					if (World.containsKey(value.getKey().getAdd(-1,0)) && ){
						
					}
				}
		}
	}*/
}

