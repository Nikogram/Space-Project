package fr.spaceproject.game;

import java.util.Vector;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.spaceproject.factions.WarMap;
import fr.spaceproject.utils.Coor;
import fr.spaceproject.utils.TextureManager;
import fr.spaceproject.utils.Vec2f;
import fr.spaceproject.utils.Vec2i;
import fr.spaceproject.vessels.Vessel;

public class SectorMap {
	private int taille;
	private int nbEnnemyVessel;
	private Coor posPlay;
	private Vessel[] ennemyVessel;
	private TextureManager textureManager;
	
	
	public SectorMap(int i,Coor pos,int newnbEnnemyVessel, TextureManager textureManager){
		this.textureManager = textureManager;
		taille=i;
		posPlay=pos;
		nbEnnemyVessel=newnbEnnemyVessel;
		ennemyVessel = new Vessel[nbEnnemyVessel];
		createArrayVessel(nbEnnemyVessel);
	}
	
	private void createArrayVessel(int i){
		ennemyVessel = new Vessel[i];
		nbEnnemyVessel=i;
		for (int l=0;l<i;l++){
			ennemyVessel[l] = new Vessel(new Vec2f((float)(Math.random() * 10000 - 5000), (float)(Math.random() * 10000 - 5000)), new Vec2i(5, 5), new Vec2i(2, 1), true, 0, textureManager);
			ennemyVessel[l].generate(3);	
		}
	}
	
	public void updateExit(Vessel playerPlayer,WarMap map){
		if (playerPlayer.getPosition().x>taille){
			playerPlayer.setPosition(new Vec2f(-taille+100,0));
			posPlay=new Coor(posPlay.addXY(1,0));
			createArrayVessel(map.appartCoor(posPlay.toStrings()));
		}
		if(playerPlayer.getPosition().x< -taille){
			playerPlayer.setPosition(new Vec2f(taille-100,0));
			posPlay=new Coor(posPlay.addXY(-1, 0));
			createArrayVessel(map.appartCoor(posPlay.toStrings()));
		}
		if (playerPlayer.getPosition().y>taille){
			playerPlayer.setPosition(new Vec2f(0,-taille+100));
			posPlay=new Coor(posPlay.addXY(0,1));
			createArrayVessel(map.appartCoor(posPlay.toStrings()));
		}
		if(playerPlayer.getPosition().y< -taille){
			playerPlayer.setPosition(new Vec2f(0,taille-100));
			posPlay=new Coor(posPlay.addXY(0,-1));
			createArrayVessel(map.appartCoor(posPlay.toStrings()));
		}
	}
	public Coor getCoor(){
		return posPlay;
	}
	public int getTaille(){
		return taille;
	}
	
	public void updateadd(Vector<Vessel> vessels){
		for (int l=0;l<ennemyVessel.length;l++)
			vessels.add(ennemyVessel[l]);
	}
	public void updateTime(float fl,Vector<Vessel> vessels){
		for (int l=0;l<ennemyVessel.length;l++)
			ennemyVessel[l].update(fl, vessels);
	}
	public void draw(SpriteBatch display){
		for (int l=0;l<ennemyVessel.length;l++)
			ennemyVessel[l].draw(display);
	}
	public int nbEnnemyVessel(){
		return nbEnnemyVessel;
	}
}
