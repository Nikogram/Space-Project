package fr.spaceproject.gui;

import java.util.Vector;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.spaceproject.factions.WarMap;
import fr.spaceproject.game.SectorMap;
import fr.spaceproject.utils.Coor;
import fr.spaceproject.utils.Sprite;
import fr.spaceproject.utils.TextureManager;
import fr.spaceproject.utils.Vec2f;
import fr.spaceproject.utils.Vec2i;
import fr.spaceproject.vessels.Vessel;
import fr.spaceproject.vessels.station.Station;

public class MiniMap {
	private Sprite[] image;
	private int[] imageAppart;
	private TextureManager textureManager;
	private Sprite font;
	private Sprite[][] stationImage;
	private int taille;
	
	public MiniMap(Vec2f placePlayer,SectorMap zone,Station station,TextureManager textureManager){
		this.textureManager = textureManager;
		taille =zone.getTaille();
		font=new Sprite(placePlayer.getAdd(550,-300),new Vec2f(0,0),textureManager.getTexture("MiniMap"));
		stationImage = CompleteStation(placePlayer,station);
		image=new Sprite[zone.nbEnnemyVessel()];
		imageAppart= new int[zone.nbEnnemyVessel()];
		image[0]=new Sprite(placePlayer.getAdd(550,-300),new Vec2f(0,0),textureManager.getTexture("MyVesselMiniMap"));
	}
	
	private Sprite[][] CompleteStation(Vec2f playerPosition,Station station){
		Sprite[][] stationInt =new Sprite[station.getSize().x][station.getSize().y];
		for (int i=0;i<station.getSize().x;i++)
			for (int j=0;j<station.getSize().y;j++){
				if (station.moduleIsShown(new Vec2i(i,j)))
					stationInt[i][j]=new Sprite(Conversion(playerPosition,station.getPosition().getAdd((i+0.66f-(float)station.getSize().x/2)*140,(j+0.80f-(float)station.getSize().y/2)*140)),new Vec2f(0,0),textureManager.getTexture("StationUselessMiniMap"));
			}
		return stationInt;
	}
	
	private Vec2f Conversion(Vec2f playerPosition,Vec2f entitiPosition){
		Vec2i coordMiddle=new Vec2i((int)((float)150/(taille*2)*entitiPosition.x),(int)((float)150/(taille*2)*entitiPosition.y));
		return playerPosition.getAdd(550,-300).getAdd(coordMiddle.x,coordMiddle.y);
	}
	
	
	public void update(SectorMap zone,Vector<Vessel> vessels,Station station){
		image=new Sprite[vessels.size()];
		font=new Sprite(vessels.get(0).getPosition().getAdd(550,-300),new Vec2f(0,0),textureManager.getTexture("MiniMap"));
		stationImage =new Sprite[station.getSize().x][station.getSize().y];
		stationImage = CompleteStation(vessels.get(0).getPosition(),station);
		image[0]=new Sprite(Conversion(vessels.get(0).getPosition(),vessels.get(0).getPosition()),new Vec2f(0,0),textureManager.getTexture("MyVesselMiniMap"));
		for (int i=1;i<vessels.size();i++){
			image[i]=new Sprite(Conversion(vessels.get(0).getPosition(),vessels.get(i).getPosition()),new Vec2f(0,0),textureManager.getTexture("EnnemiMiniMap"));
		}
	}
	public void draw(SpriteBatch display){
		font.draw(display);
		for (int i=0;i<stationImage.length;i++)
			for (int j=0;j<stationImage[0].length;j++)
				if (stationImage[i][j] != null)
				stationImage[i][j].draw(display);
		for (int i=0;i<image.length;i++){
		image[i].draw(display);
		}
	}
	
}

