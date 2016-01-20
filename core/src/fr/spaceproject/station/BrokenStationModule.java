package fr.spaceproject.station;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.spaceproject.game.Explosion;
import fr.spaceproject.utils.Orientation;
import fr.spaceproject.utils.Sprite;
import fr.spaceproject.utils.TextureManager;
import fr.spaceproject.utils.Vec2f;

public class BrokenStationModule extends StationModule
{
	private Explosion explosion;
	
	public BrokenStationModule(int level, Vec2f position, Orientation orientation, TextureManager textureManager)
	{
		super(-1, level, position, orientation, textureManager);
		
		explosion = new Explosion(position, false, textureManager);
	}
	
	@Override
	public void update(float lastFrameTime)
	{
		super.update(lastFrameTime);
		explosion.update(lastFrameTime);
	}
	
	@Override
	public void draw(SpriteBatch display)
	{
		super.draw(display);
		explosion.draw(display);      
	}
}
