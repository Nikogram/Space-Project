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
	
	private Background background;
	
	public SectorMap(int i,Coor pos,int newnbEnnemyVessel, TextureManager textureManager,Geopolitics politic,WarMap map){
		this.textureManager = textureManager;
		taille=i;
		posPlay=pos;
		nbEnnemyVessel=newnbEnnemyVessel;
		vessels = new Vector<Vessel>();
		playerVessel = new Vessel(new Vec2f(0, 0), new Vec2i(3, 3), new Vec2i(1, 1), false, 0, new Vec2f(2*taille, 2*taille), textureManager);
		playerVessel.generate(2);
		vessels.add(playerVessel);
		createArrayObjects(nbEnnemyVessel,playerVessel,map,pos,politic);
		background = new Background(new Vec2f(taille, taille), textureManager);
	}
	
	private void createArrayObjects(int i,Vessel playerPlayer,WarMap map,Coor pos,Geopolitics state){
		alignement=map.appartCoor(pos.toStrings());
		alignementplayer=state.getAgressivitys();
		vessels = new Vector<Vessel>();
		vessels.add(playerVessel);
		nbEnnemyVessel=i;
		station = new Station(new Vec2f(-1000, 0), new Vec2i(10, 5), alignement, textureManager);
		/*for (int l=1;l<i+1;l++){
			vessels.add(new Vessel(new Vec2f((float)(Math.random() * 2 * taille - taille), (float)(Math.random() * 2 * taille - taille)), new Vec2i(3, 3), new Vec2i(1, 1), true, map.appartCoor(pos.toStrings()), new Vec2f(2*taille, 2*taille), textureManager));
			if (vessels.get(l).getFaction() == 2)
				vessels.get(l).generate(2);	
			else
				vessels.get(l).generate(3);	

		}*/
	}
	
	public void updateExit(Vessel playerPlayer,WarMap map,Geopolitics state){
		if (playerPlayer.getPosition().x>taille){
			playerPlayer.setPosition(new Vec2f(-taille+100,playerPlayer.getPosition().y));
			posPlay=new Coor(posPlay.addXY(1,0));
			createArrayObjects(map.appartCoor(posPlay.toStrings()),playerVessel,map,posPlay,state);
		}
		if(playerPlayer.getPosition().x< -taille){
			playerPlayer.setPosition(new Vec2f(taille-100,playerPlayer.getPosition().y));
			posPlay=new Coor(posPlay.addXY(-1, 0));
			createArrayObjects(map.appartCoor(posPlay.toStrings()),playerVessel,map,posPlay,state);
		}
		if (playerPlayer.getPosition().y>taille){
			playerPlayer.setPosition(new Vec2f(playerPlayer.getPosition().x,-taille+100));
			posPlay=new Coor(posPlay.addXY(0,1));
			createArrayObjects(map.appartCoor(posPlay.toStrings()),playerVessel,map,posPlay,state);
		}
		if(playerPlayer.getPosition().y< -taille){
			playerPlayer.setPosition(new Vec2f(playerPlayer.getPosition().x,taille-100));
			posPlay=new Coor(posPlay.addXY(0,-1));
			createArrayObjects(map.appartCoor(posPlay.toStrings()),playerVessel,map,posPlay,state);
		}
	}
	public Coor getCoor(){
		return posPlay;
	}
	public int getTaille(){
		return taille;
	}
	
	public void update(float fl,Geopolitics state){ 
		for (int l=1;l<vessels.size();l++){
				for (int j=0;j<vessels.get(l).getAttackingVessel().size();j++){
					if ( vessels.get(l).getAttackingVessel().get(j).getFaction()==0 && !vessels.get(l).isDestroyed())
						state.addAgressivity(vessels.get(l).getFaction(),1);
					if (vessels.get(l).getAttackingVessel().get(j).getFaction()==0 && vessels.get(l).isDestroyed()){
						for (int i=1;i<state.getNbTeam();i++){
							if (i!=vessels.get(l).getFaction())
								state.decAgressivity(i,5);
							else {
								state.addAgressivity(i,10);
							}
						}
					}
			
				}
		}
		for (int l=1;l<vessels.size();l++){
			if (vessels.get(l).isDestroyed() && !vessels.get(l).isExplosing()){
				vessels.remove(l);
			}
		}
		for (int l=0;l<vessels.size();l++){
			vessels.get(l).clearAttackingVessel();
		station.clearAttackingVessel();
		}
		
		for (int l=0;l<vessels.size();l++)
			vessels.get(l).update(fl, vessels, station, alignementplayer);
		station.update(fl, alignementplayer, vessels);
}
	
	public void draw(SpriteBatch display){
		background.draw(display, playerVessel.getPosition());
		station.drawBackground(display);
		for (int l=0;l<vessels.size();l++)
			vessels.get(l).drawBackground(display);
		for (int l=0;l<vessels.size();l++)
			vessels.get(l).draw(display);
		station.draw(display);
		for (int l=0;l<vessels.size();l++)
			vessels.get(l).drawForeground(display);
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
