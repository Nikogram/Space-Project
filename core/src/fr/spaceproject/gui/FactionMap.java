package fr.spaceproject.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.spaceproject.factions.WarMap;
import fr.spaceproject.utils.Coor;
import fr.spaceproject.utils.Sprite;
import fr.spaceproject.utils.Vec2f;

public class FactionMap {
	private Sprite[][] carte;
	//private Sprite positionPlayer;
	
	public FactionMap(Vec2f placePlayer,Coor coorPlayer,WarMap map){
		carte = new Sprite[5][5];
		for (int j=4;j>-1;j--){
			for (int i=0;i<5;i++){
				carte[i][j]=new Sprite(placePlayer.add((i-3)*20,(j-3)*20),new Vec2f(0,0),translate(coorPlayer.addXY(i-2,j-2),map));
			}
		}
		//positionPlayer=new Sprite(placePlayer.add(20,20),new Vec2f(0,0),"player.png");
	}
	
	public void update(Vec2f placePlayer,Coor coorPlayer,WarMap map){
		carte = new Sprite[5][5];
		for (int j=4;j>-1;j--){
			for (int i=0;i<5;i++){
				this.carte[i][j]=new Sprite(placePlayer.add((i+25)*21,(j+10)*21),new Vec2f(0,0),translate(coorPlayer.addXY(i-2,j-2),map));
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
		return "ColorFaction"+Integer.toString(map.appartCoor(coorReach))+".png";
	}
	
	
	
	}
