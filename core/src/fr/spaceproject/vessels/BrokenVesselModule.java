package fr.spaceproject.vessels;

import java.util.Vector;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.spaceproject.game.Explosion;
import fr.spaceproject.utils.Orientation;
import fr.spaceproject.utils.Sprite;
import fr.spaceproject.utils.TextureManager;
import fr.spaceproject.utils.Vec2f;
import fr.spaceproject.utils.Vec2i;

public class BrokenVesselModule extends VesselModule
{
	private Explosion explosion;
	
	public BrokenVesselModule(int level, Orientation orientation, TextureManager textureManager)
	{
		super(-1, level, orientation, textureManager);
		explosion = new Explosion(new Vec2f(), true, textureManager);
	}
	
	public BrokenVesselModule(int level, Orientation orientation, TextureManager textureManager, boolean isEngine)
	{
		super(-1, level, orientation, textureManager, isEngine);
		explosion = new Explosion(new Vec2f(), true, textureManager);
	}
	
	@Override
	public void update(float lastFrameTime, Sprite vesselSprite, Vec2i moduleRelativePosition, Vector<VesselAction> actions)
	{
		super.update(lastFrameTime, vesselSprite, moduleRelativePosition,actions);
		explosion.setPosition(getSpritePosition());
		explosion.update(lastFrameTime);
	}
	
	@Override
	public void draw(SpriteBatch display)
	{
		super.draw(display);
		explosion.draw(display);      
	}
}
