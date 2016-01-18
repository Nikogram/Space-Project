package fr.spaceproject.station;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.spaceproject.utils.Orientation;
import fr.spaceproject.utils.Sprite;
import fr.spaceproject.utils.TextureManager;
import fr.spaceproject.utils.Vec2f;

public class StationModule
{
	private Sprite sprite;
	
	public StationModule(int type, int level, Vec2f position, Orientation orientation, TextureManager textureManager)
	{
		sprite = new Sprite(position, new Vec2f(140, 140), textureManager.getTexture("SimpleStationModule"));
	}
	
	public void draw(SpriteBatch display)
	{
		sprite.draw(display);
	}
}
