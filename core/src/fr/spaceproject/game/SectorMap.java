package fr.spaceproject.game;

import java.util.Vector;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.spaceproject.utils.Coor;
import fr.spaceproject.utils.Vec2f;
import fr.spaceproject.utils.Vec2i;
import fr.spaceproject.vessels.Vessel;

public class SectorMap {
	private int taille;
	private int nbEnnemyVessel;
	private Coor posPlay;
	private Vessel[] ennemyVessel;
	
	
	public SectorMap(int i,Coor pos,int newnbEnnemyVessel){
		taille=i;
		posPlay=pos;
		nbEnnemyVessel=newnbEnnemyVessel;
		ennemyVessel = new Vessel[nbEnnemyVessel];
		createArrayVessel(nbEnnemyVessel);
	}
	
	private void createArrayVessel(int i){
		for (int l=0;l<i;l++){
			ennemyVessel[l] = new Vessel(new Vec2f(-200, -200), new Vec2i(5, 5), new Vec2i(2, 1), true, 0);
			ennemyVessel[l].generate(2);	
		}
	}
	
	public void updateExit(Vessel playerPlayer){
		if (playerPlayer.getPosition().x>taille){
			playerPlayer.setPosition(new Vec2f(-taille+100,0));
			posPlay=new Coor(posPlay.addXY(1,0));
			createArrayVessel(this.nbEnnemyVessel);
		}
		if(playerPlayer.getPosition().x< -taille){
			playerPlayer.setPosition(new Vec2f(taille-100,0));
			posPlay=new Coor(posPlay.addXY(-1, 0));
			createArrayVessel(this.nbEnnemyVessel);
		}
		if (playerPlayer.getPosition().y>taille){
			playerPlayer.setPosition(new Vec2f(0,-taille+100));
			posPlay=new Coor(posPlay.addXY(0,1));
			createArrayVessel(this.nbEnnemyVessel);
		}
		if(playerPlayer.getPosition().y< -taille){
			playerPlayer.setPosition(new Vec2f(0,taille-100));
			posPlay=new Coor(posPlay.addXY(0,-1));
			createArrayVessel(this.nbEnnemyVessel);
		}
	}
	public Coor getCoor(){
		return posPlay;
	}
	
	public void updateadd(Vector<Vessel> vessels){
		for (int l=0;l<this.nbEnnemyVessel;l++)
			vessels.add(ennemyVessel[l]);
	}
	public void updateTime(float fl,Vector<Vessel> vessels){
		for (int l=0;l<this.nbEnnemyVessel;l++)
			ennemyVessel[l].update(fl, vessels);
	}
	public void draw(SpriteBatch display){
		for (int l=0;l<this.nbEnnemyVessel;l++)
			ennemyVessel[l].draw(display);
	}
}
