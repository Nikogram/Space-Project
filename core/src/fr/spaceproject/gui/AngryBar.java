package fr.spaceproject.gui;

import com.badlogic.gdx.Gdx;
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
		bar = new Sprite[state.getNbTeam()-1];
		for (int i=0;i<state.getNbTeam()-1;i++){
			bar[i]=new Sprite(placePlayer.getAdd(Gdx.graphics.getWidth()/2-100+25*i,Gdx.graphics.getHeight()/2-150+state.getAgressivity(i+1)/4),new Vec2f(state.getAgressivity(i+1)/2,20),textureManager.getTexture("ColorFaction"+(i+1)));
			}
	}
	public void update(Vec2f placePlayer,Geopolitics state,TextureManager textureManager){
		this.textureManager = textureManager;
		bar = new Sprite[state.getNbTeam()-1];
		for (int i=0;i<state.getNbTeam()-1;i++){
			bar[i]=new Sprite(placePlayer.getAdd(Gdx.graphics.getWidth()/2-212+25*i,Gdx.graphics.getHeight()/2-66+state.getAgressivity(i+1)/4),new Vec2f(state.getAgressivity(i+1)/2,20),textureManager.getTexture("ColorFaction"+(i+1)));
		}
	}
	public void draw(SpriteBatch display){
		for (int i=0;i<4;i++){
			bar[i].draw(display);
		}
	}
}
