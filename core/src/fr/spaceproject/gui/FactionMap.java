package fr.spaceproject.gui;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.spaceproject.factions.WarMap;
import fr.spaceproject.utils.Coor;
import fr.spaceproject.utils.Sprite;
import fr.spaceproject.utils.TextureManager;
import fr.spaceproject.utils.Vec2f;

public class FactionMap {
	private Sprite[][] carte;
	private int[][] appart;
	private TextureManager textureManager;

	public FactionMap(Vec2f placePlayer,Coor coorPlayer,WarMap map, TextureManager textureManager){
		this.textureManager = textureManager;
		carte = new Sprite[5][5];
		appart = new int[5][5];
		for (int j=4;j>-1;j--){
			for (int i=0;i<5;i++){
				appart[i][j] = map.appartCoor(coorPlayer.addXY(i-2,j-2));
				carte[i][j]=new Sprite(placePlayer.getAdd((i+25)*21,(j+10)*21),new Vec2f(0,0),textureManager.getTexture(translate(coorPlayer.addXY(i-2,j-2),map)));
			}
		}
		//positionPlayer=new Sprite(placePlayer.add(20,20),new Vec2f(0,0),AngryBar"player.png");
	}
	
	public void update(Vec2f placePlayer,Coor coorPlayer,WarMap map){
		for (int j=4;j>-1;j--){
			for (int i=0;i<5;i++)
			{
				if (appart[i][j] != map.appartCoor(coorPlayer.addXY(i-2,j-2)))
				{
					appart[i][j] = map.appartCoor(coorPlayer.addXY(i-2,j-2));
					this.carte[i][j].setTexture(textureManager.getTexture(translate(coorPlayer.addXY(i-2,j-2),map)));
				}
				this.carte[i][j].setPosition(placePlayer.getAdd((i+25)*21,(j+10)*21));
			}
		}
		//positionPlayer =new Sprite(placePlayer.add(20,20),new Vec2f(0,0),"player.png");
	}
	public void draw(SpriteBatch display){
		for (int x = 0; x < carte.length; ++x){
			for (int y = 0; y < carte[x].length; ++y)
				carte[x][y].draw(display);
		}
	}
	
	
	private String translate(String coorReach,WarMap map){
		return "ColorFaction"+Integer.toString(map.appartCoor(coorReach));
	}
	
	
	
	}
