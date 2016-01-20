package fr.spaceproject.gui;

import java.util.Vector;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.spaceproject.utils.Sprite;
import fr.spaceproject.utils.TextureManager;
import fr.spaceproject.utils.Vec2f;
import fr.spaceproject.utils.Vec2i;
import fr.spaceproject.vessels.Vessel;


public class VesselState {
	
	private Sprite[][] playerImage;
	
	public VesselState(Vessel playerVessel,TextureManager textureManager){
		playerImage =new Sprite[playerVessel.getSize().x][playerVessel.getSize().y];
		for (int i=0;i<playerVessel.getSize().x;i++)
			for (int j=0;j<playerVessel.getSize().y;j++)
				if (playerVessel.getModuleType(new Vec2i(i,j))>-1)
					playerImage[i][j] =new Sprite(playerVessel.getPosition().getAdd(-550+21*3,+300-21*(3-j)),new Vec2f(0,0),textureManager.getTexture(ImageModule(playerVessel.getModuleType(new Vec2i(i,j)))));
	}
	
	public void Update(Vessel playerVessel,TextureManager textureManager){
		playerImage =new Sprite[playerVessel.getSize().x][playerVessel.getSize().y];
		for (int i=0;i<playerVessel.getSize().x;i++)
			for (int j=0;j<playerVessel.getSize().y;j++)
				if (playerVessel.getModuleType(new Vec2i(i,j))>-1)
					playerImage[i][j] =new Sprite(playerVessel.getPosition().getAdd(-550+21*i,+300-21*(3-j)),new Vec2f(0,0),textureManager.getTexture(ImageModule(playerVessel.getModuleType(new Vec2i(i,j)))));
	}
	
	public void draw(SpriteBatch display){
		for (int i=0;i<playerImage.length;i++)
			for (int j=0;j<playerImage[0].length;j++)
				if (playerImage[i][j]!=null)
					playerImage[i][j].draw(display);
	}
	
	
	
	/*private Color Colormodule(){
		return (0,1,1,1);
	}*/
	
	private String ImageModule(int i){
		return "ColorFaction0";
	}
}
