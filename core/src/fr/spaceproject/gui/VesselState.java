package fr.spaceproject.gui;

import java.util.Vector;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.spaceproject.utils.Sprite;
import fr.spaceproject.utils.TextureManager;
import fr.spaceproject.utils.Vec2f;
import fr.spaceproject.utils.Vec2i;
import fr.spaceproject.vessels.Vessel;


public class VesselState {
	
	private Sprite[][] playerImage;
	private int[][] types;
	
	public VesselState(Vessel playerVessel,TextureManager textureManager){
		playerImage =new Sprite[playerVessel.getSize().x][playerVessel.getSize().y];
		types=new int[playerVessel.getSize().x][playerVessel.getSize().y];
		for (int i=0;i<playerVessel.getSize().x;i++)
			for (int j=0;j<playerVessel.getSize().y;j++)
				if (playerVessel.getModuleType(new Vec2i(i,j))>-1){
					types[i][j]=playerVessel.getModuleType(new Vec2i(i,j));
					playerImage[i][j] =new Sprite(playerVessel.getPosition().getAdd(-550+31*i,+350-31*(3-j)),new Vec2f(0,0),textureManager.getTexture("PlayerVesselState"+playerVessel.getModuleType(new Vec2i(i,j))));
					playerImage[i][j].setColor(new Color(0,1,0,1));
				}
				else
					types[i][j]=-2;
				
	}
	
	public void Update(Vessel playerVessel,TextureManager textureManager){
		playerImage =new Sprite[playerVessel.getSize().x][playerVessel.getSize().y];
		for (int i=0;i<playerVessel.getSize().x;i++)
			for (int j=0;j<playerVessel.getSize().y;j++){
				if (playerVessel.getModuleType(new Vec2i(i,j))==types[i][j] && types[i][j]>-1){
					playerImage[i][j] =new Sprite(playerVessel.getPosition().getAdd(-550+31*i,+350-31*(3-j)),new Vec2f(0,0),textureManager.getTexture("PlayerVesselState"+playerVessel.getModuleType(new Vec2i(i,j))));
					playerImage[i][j].setColor(ColorModule(playerVessel,i,j));
				}
				if (playerVessel.getModuleType(new Vec2i(i,j))!=types[i][j]){
					playerImage[i][j] =new Sprite(playerVessel.getPosition().getAdd(-550+31*i,+350-31*(3-j)),new Vec2f(0,0),textureManager.getTexture("PlayerVesselState"+types[i][j]));
					playerImage[i][j].setColor(new Color(0.2f,0.2f,0.2f,1));
				}
			}
	}
	
	public void draw(SpriteBatch display){
		for (int i=0;i<playerImage.length;i++)
			for (int j=0;j<playerImage[0].length;j++)
				if (playerImage[i][j]!=null)
					playerImage[i][j].draw(display);
	}
	
	
	
	private Color ColorModule(Vessel playerVessel,int i,int j){
		if (playerVessel.getModuleEnergy(new Vec2i(i,j), true)<0.25f*playerVessel.getModuleMaxEnergy(new Vec2i(i,j)))
			return new Color(1,0,0,1);
		else if (playerVessel.getModuleEnergy(new Vec2i(i,j), true)<0.5f*playerVessel.getModuleMaxEnergy(new Vec2i(i,j)))
			return new Color(1,1,0,1);
		else 
			return new Color(0,1,0,1);
	}
}
