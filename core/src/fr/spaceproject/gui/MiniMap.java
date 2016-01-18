package fr.spaceproject.gui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.spaceproject.factions.WarMap;
import fr.spaceproject.game.SectorMap;
import fr.spaceproject.utils.Coor;
import fr.spaceproject.utils.Sprite;
import fr.spaceproject.utils.TextureManager;
import fr.spaceproject.utils.Vec2f;
import fr.spaceproject.utils.Vec2i;

public class MiniMap {
	private Sprite image;
	private int[] imageAppart;
	private Vec2f[] imageCoor;
	private TextureManager textureManager;
	private Sprite font;
	private int taille;
	
	public MiniMap(Vec2f placePlayer,SectorMap zone,TextureManager textureManager){
		this.textureManager = textureManager;
		taille =zone.getTaille();
		font=new Sprite(placePlayer.getAdd(550,-300),new Vec2f(0,0),textureManager.getTexture("MiniMap"));
		//image=new Sprite[zone.nbEnnemyVessel()+1];
		imageAppart= new int[zone.nbEnnemyVessel()+1];
		imageCoor=new Vec2f[zone.nbEnnemyVessel()+1];
		image=new Sprite(placePlayer.getAdd(550,-300),new Vec2f(0,0),textureManager.getTexture("MyVesselMiniMap"));
		/*for (int i=1;i<zone.nbEnnemyVessel()+1;i++){
			image[i]=new Sprite(,new Vec2f(0,0),textureManager.getTexture("EnnemiMiniMap"));
		}*/
	}
	
	private Vec2f Conversion(Vec2f playerPosition){
		Vec2i coordMiddle=new Vec2i((int)((float)150/(taille*2)*playerPosition.x),(int)((float)150/(taille*2)*playerPosition.y));
		return playerPosition.getAdd(550,-300).getAdd(coordMiddle.x,coordMiddle.y);
	}
	
	public void update(Vec2f placePlayer,WarMap map){
		font=new Sprite(placePlayer.getAdd(550,-300),new Vec2f(0,0),textureManager.getTexture("MiniMap"));
		image=new Sprite(Conversion(placePlayer),new Vec2f(0,0),textureManager.getTexture("MyVesselMiniMap"));
		
	}
	public void draw(SpriteBatch display){
		font.draw(display);
		image.draw(display);
	}
	
}

