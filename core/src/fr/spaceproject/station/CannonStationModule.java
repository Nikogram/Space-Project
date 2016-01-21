package fr.spaceproject.station;

import java.util.Vector;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.spaceproject.utils.Orientation;
import fr.spaceproject.utils.Sprite;
import fr.spaceproject.utils.TextureManager;
import fr.spaceproject.utils.Vec2f;
import fr.spaceproject.vessels.Vessel;

public class CannonStationModule extends StationModule
{
	private Sprite cannonSprite;
	
	public CannonStationModule(int level, Vec2f position, Orientation orientation, TextureManager textureManager)
	{
		super(1, level, position, orientation, textureManager);
		cannonSprite = new Sprite(new Vec2f(), new Vec2f(), textureManager.getTexture("CannonVesselModule"));
	}
	
	@Override
	public void update(float lastFrameTime, int[] factionsAgressivity, Vector<Vessel> vessels)
	{
		super.update(lastFrameTime, factionsAgressivity, vessels);
	}
	
	@Override
	public void draw(SpriteBatch display)
	{
		super.draw(display);
		cannonSprite.draw(display);
	}
}
