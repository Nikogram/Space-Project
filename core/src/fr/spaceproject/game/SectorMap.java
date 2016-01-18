package fr.spaceproject.game;

import fr.spaceproject.utils.Coor;
import fr.spaceproject.utils.Vec2f;

public class SectorMap {
	private int taille;
	private Coor posPlay;
	
	public SectorMap(int i,Coor pos){
		taille=i;
		posPlay=pos;
	}
	
	public void update(Vec2f placePlayer){
		if (placePlayer.x>taille){
			placePlayer.x=-taille;
			posPlay=new Coor(posPlay.addXY(1,0));
		}
		if(placePlayer.x< -taille){
			placePlayer.x=taille;
			posPlay=new Coor(posPlay.addXY(-1, 0));
		}
		if (placePlayer.y>taille){
			placePlayer.y=-taille;
			posPlay=new Coor(posPlay.addXY(0,1));
		}
		if(placePlayer.y< -taille){
			placePlayer.y=taille;
			posPlay=new Coor(posPlay.addXY(0,-1));
		}
	}
	public Coor getCoor(){
		return posPlay;
	}
}
