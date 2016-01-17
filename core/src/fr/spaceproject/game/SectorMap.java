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
			posPlay=Coor(posPlay.addXY(1, 0));
		}
	}
}
