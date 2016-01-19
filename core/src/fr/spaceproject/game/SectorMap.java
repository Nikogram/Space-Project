package fr.spaceproject.game;

import java.util.Vector;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.spaceproject.factions.Geopolitics;
import fr.spaceproject.factions.WarMap;
import fr.spaceproject.station.Station;
import fr.spaceproject.utils.Coor;
import fr.spaceproject.utils.TextureManager;
import fr.spaceproject.utils.Vec2f;
import fr.spaceproject.utils.Vec2i;
import fr.spaceproject.vessels.Vessel;

public class SectorMap {
	private int taille;
	
	private Coor posPlay;
	private int alignement;
	private int[] alignementplayer;
	private TextureManager textureManager;
	

	private int nbEnnemyVessel;
	private Vector<Vessel> vessels;
	private Vessel playerVessel;
	
	private Station station;
	
	public SectorMap(int i,Coor pos,int newnbEnnemyVessel, TextureManager textureManager,Geopolitics politic,WarMap map){
		this.textureManager = textureManager;
		taille=i;
		alignementplayer=politic.getAgressivitys();
		posPlay=pos;
		nbEnnemyVessel=newnbEnnemyVessel;
		vessels = new Vector<Vessel>();
		playerVessel = new Vessel(new Vec2f(0, 0), new Vec2i(3, 3), new Vec2i(1, 1), false, 0, textureManager);
		playerVessel.generate(3);
		vessels.add(playerVessel);
		createArrayObjects(nbEnnemyVessel,playerVessel,map,pos);
	}
	
	private void createArrayObjects(int i,Vessel playerPlayer,WarMap map,Coor pos){
		alignement=map.appartCoor(pos.toStrings());
		vessels = new Vector<Vessel>();
		vessels.add(playerVessel);
		nbEnnemyVessel=i;
		station = new Station(new Vec2f(-1000, 0), new Vec2i(10, 5), 1, textureManager);
		for (int l=1;l<i+1;l++){
			vessels.add(new Vessel(new Vec2f((float)(Math.random() * 2000 - 1000), (float)(Math.random() * 2000 - 1000)), new Vec2i(3, 3), new Vec2i(1, 1), true, 0, textureManager));
			if (alignementplayer[alignement]>50)
				vessels.get(l).generate(1);	
			else
				vessels.get(l).generate(3);	

		}
	}
	
	public void updateExit(Vessel playerPlayer,WarMap map){
		if (playerPlayer.getPosition().x>taille){
			playerPlayer.setPosition(new Vec2f(-taille+100,playerPlayer.getPosition().y));
			posPlay=new Coor(posPlay.addXY(1,0));
			createArrayObjects(map.appartCoor(posPlay.toStrings()),playerVessel,map,posPlay);
		}
		if(playerPlayer.getPosition().x< -taille){
			playerPlayer.setPosition(new Vec2f(taille-100,playerPlayer.getPosition().y));
			posPlay=new Coor(posPlay.addXY(-1, 0));
			createArrayObjects(map.appartCoor(posPlay.toStrings()),playerVessel,map,posPlay);
		}
		if (playerPlayer.getPosition().y>taille){
			playerPlayer.setPosition(new Vec2f(playerPlayer.getPosition().x,-taille+100));
			posPlay=new Coor(posPlay.addXY(0,1));
			createArrayObjects(map.appartCoor(posPlay.toStrings()),playerVessel,map,posPlay);
		}
		if(playerPlayer.getPosition().y< -taille){
			playerPlayer.setPosition(new Vec2f(playerPlayer.getPosition().x,taille-100));
			posPlay=new Coor(posPlay.addXY(0,-1));
			createArrayObjects(map.appartCoor(posPlay.toStrings()),playerVessel,map,posPlay);
		}
	}
	public Coor getCoor(){
		return posPlay;
	}
	public int getTaille(){
		return taille;
	}
	
	public void update(float fl){ 
		vessels.get(0).update(fl, vessels, station);
		for (int l=1;l<vessels.size();l++){
			if (vessels.get(l).getIsDestroyed())
				vessels.remove(l);
			}
		for (int l=1;l<vessels.size();l++)
			vessels.get(l).update(fl, vessels, station);
	}
	public void draw(SpriteBatch display){
		
		for (int l=0;l<vessels.size();l++)
			vessels.get(l).draw(display);
		station.draw(display);
	}
	public int nbEnnemyVessel(){
		return vessels.size();
	}
	public Vector<Vessel> getVector(){;
		return vessels;
	}
	public Vessel getPlayer(){
		return vessels.get(0);
	}
	public Station getStation(){
		return station;
	}
}
