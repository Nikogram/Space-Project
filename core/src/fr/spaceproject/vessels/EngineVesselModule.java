package fr.spaceproject.vessels;

import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.spaceproject.utils.Orientation;
import fr.spaceproject.utils.Sprite;
import fr.spaceproject.utils.TextureManager;
import fr.spaceproject.utils.Vec2f;
import fr.spaceproject.utils.Vec2i;

public class EngineVesselModule extends VesselModule
{
	protected Sprite flamesSprite;
	protected boolean flamesAreShown;
	protected float flamesIntensity;
	protected float time;
	
	
	public EngineVesselModule(VesselModuleType type, int level, Orientation orientation, TextureManager textureManager)
	{
		super(type, level, orientation, textureManager);
		flamesSprite = new Sprite(new Vec2f(), new Vec2f(), textureManager.getTexture("FlamesEngineVesselModule"));
		flamesAreShown = false;
	}
	
	public float getPower()
	{
		return 1 + 0.5f * (getLevel() - 1);
	}
	
	@Override
	public void update(float lastFrameTime, Sprite vesselSprite, Vec2i moduleRelativePosition, Vector<VesselAction> actions)
	{
		super.update(lastFrameTime, vesselSprite, moduleRelativePosition, actions);
		
		time += lastFrameTime;
		
		flamesAreShown = false;
		
		for (int i = 0; i < actions.size(); ++i)
		{
			if (actions.get(i).equals(VesselAction.MoveForward) && getOrientation().equals(Orientation.Down))
				flamesAreShown = true;
			else if (actions.get(i).equals(VesselAction.MoveBackward) && getOrientation().equals(Orientation.Up))
				flamesAreShown = true;
			else if (actions.get(i).equals(VesselAction.MoveLeft) && getOrientation().equals(Orientation.Right))
				flamesAreShown = true;
			else if (actions.get(i).equals(VesselAction.MoveRight) && getOrientation().equals(Orientation.Left))
				flamesAreShown = true;
		}
		
		flamesSprite.setPosition(getSprite().getRotatedPosition(new Vec2f(0, -getSprite().getSize().y / 2), getSprite().getAngle() - 180));
		flamesSprite.setAngle(getSprite().getAngle() - 180);
		
		if (flamesAreShown)
			flamesIntensity += lastFrameTime * 7;
		else
			flamesIntensity -= lastFrameTime * 7;
		
		if (flamesIntensity < 0)
			flamesIntensity = 0;
		else if (flamesIntensity > 1)
			flamesIntensity = 1;
		
		float flamesIntensityOffset = (float)Math.abs(Math.sin(100 * time));
		
		if (flamesIntensity * flamesIntensityOffset <= 0)
			flamesSprite.setAlpha(0);
		else if (flamesIntensity * flamesIntensityOffset >= 1)
			flamesSprite.setAlpha(1);
		else
			flamesSprite.setAlpha(flamesIntensity * flamesIntensityOffset);
	}
	
	@Override
	public void draw(SpriteBatch display)
	{
		super.draw(display);
		flamesSprite.draw(display);
	}
}
