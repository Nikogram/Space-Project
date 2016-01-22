package fr.spaceproject.station;

import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.spaceproject.utils.Explosion;
import fr.spaceproject.utils.Orientation;
import fr.spaceproject.utils.Sprite;
import fr.spaceproject.utils.TextureManager;
import fr.spaceproject.utils.Vec2f;
import fr.spaceproject.vessels.Vessel;

public class BrokenStationModule extends StationModule
{
	private Explosion explosion;
	
	public BrokenStationModule(int level, Vec2f position, Orientation orientation, TextureManager textureManager)
	{
		super(-1, level, position, orientation, textureManager);
		
		explosion = new Explosion(position, false, textureManager);
	}
	
	@Override
	public void update(float lastFrameTime, int faction, int[] factionsAgressivity, Vector<Vessel> vessels)
	{
		super.update(lastFrameTime, faction, factionsAgressivity, vessels);
		explosion.update(lastFrameTime);
	}
	
	@Override
	public void draw(SpriteBatch display)
	{
		super.draw(display);
		explosion.draw(display);      
	}
}
