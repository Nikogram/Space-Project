package fr.spaceproject.gui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.spaceproject.factions.Geopolitics;
import fr.spaceproject.utils.Sprite;
import fr.spaceproject.utils.TextureManager;
import fr.spaceproject.utils.Vec2f;

public class AngryBar {
	private Sprite[] bar;
	private TextureManager textureManager;
	
	public AngryBar(Vec2f placePlayer,Geopolitics state,TextureManager textureManager){
		this.textureManager = textureManager;
		bar = new Sprite[4];
		for (int i=0;i<4;i++){
			bar[i]=new Sprite(placePlayer.getAdd(700+25*i,300),new Vec2f(20,10),textureManager.getTexture("ColorFaction"+i));
		}
	}
	public void update(Vec2f placePlayer,Geopolitics state,TextureManager textureManager){
		this.textureManager = textureManager;
		bar = new Sprite[4];
		for (int i=0;i<4;i++){
			bar[i]=new Sprite(placePlayer.getAdd(700+25*i,250),new Vec2f(state.getAgressivity(i+1)/4,20),textureManager.getTexture("ColorFaction"+i));
		}
	}
	public void draw(SpriteBatch display){
		for (int i=0;i<4;i++){
			bar[i].draw(display);
		}
	}
}
