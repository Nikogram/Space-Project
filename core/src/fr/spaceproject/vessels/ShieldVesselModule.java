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

public class ShieldVesselModule extends VesselModule
{
	public ShieldVesselModule(int type, int level, Orientation orientation, TextureManager textureManager)
	{
		super(type, level, orientation, textureManager);
		setEnergy(getMaxEnergy());
	}
	
	@Override
	public float getMaxEnergy()
	{
		return 100 + 50 * (getLevel() - 1);
	}
	
	@Override
	public void update(float lastFrameTime, Sprite vesselSprite, Vec2i moduleRelativePosition, Vector<VesselAction> actions)
	{
		super.update(lastFrameTime, vesselSprite, moduleRelativePosition, actions);
	}
}
