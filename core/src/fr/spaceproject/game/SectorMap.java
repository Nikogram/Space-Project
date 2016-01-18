package fr.spaceproject.game;

import fr.spaceproject.utils.Coor;
import fr.spaceproject.utils.Vec2f;
import fr.spaceproject.vessels.Vessel;

public class SectorMap {
	private int taille;
	private Coor posPlay;
	
	public SectorMap(int i,Coor pos){
		taille=i;
		posPlay=pos;
	}
	
	public void update(Vessel playerPlayer){
		if (playerPlayer.getPosition().x>taille){
			playerPlayer.setPosition(new Vec2f(-taille+100,0));
			posPlay=new Coor(posPlay.addXY(1,0));
		}
		if(playerPlayer.getPosition().x< -taille){
			playerPlayer.setPosition(new Vec2f(taille-100,0));
			posPlay=new Coor(posPlay.addXY(-1, 0));
		}
		if (playerPlayer.getPosition().y>taille){
			playerPlayer.setPosition(new Vec2f(0,-taille+100));
			posPlay=new Coor(posPlay.addXY(0,1));
		}
		if(playerPlayer.getPosition().y< -taille){
			playerPlayer.setPosition(new Vec2f(0,taille-100));
			posPlay=new Coor(posPlay.addXY(0,-1));
		}
	}
	public Coor getCoor(){
		return posPlay;
	}
}
