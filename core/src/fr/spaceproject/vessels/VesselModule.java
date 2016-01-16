package fr.spaceproject.vessels;

import java.util.Vector;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.spaceproject.utils.*;

class VesselModule
{
	protected int type;
	protected int level;
	protected float energy;
	protected Sprite sprite;
	protected Orientation orientation;
	
	
	public VesselModule(int type, int level, Orientation orientation)
	{
		this.type = type;
		this.level = level;
		energy = getMaxEnergy();
		sprite = new Sprite(new Vec2f(), new Vec2f(), getTextureFileName());
		sprite.angle = orientation.ordinal() * 90;
		this.orientation = orientation;
	}
	
	public String getTextureFileName()
	{
		if (type == 1)	// Cockpit
			return "CockpitVesselModule.png";
		else if (type == 2)	// Engine
			return "EngineVesselModule.png";
		else	// Simple
			return "SimpleVesselModule.png";
	}
	
	public float getMaxEnergy()
	{
		return 100 + 20 * (level - 1);
	}
	
	public void update(float lastFrameTime, Sprite vesselSprite, Vec2i moduleRelativePosition, Vector<VesselAction> actions)
	{
		sprite.position = vesselSprite.getRotatedPosition(new Vec2f(20 * moduleRelativePosition.x, 20 * moduleRelativePosition.y), vesselSprite.angle);
		sprite.speed = vesselSprite.speed;
		sprite.angle = vesselSprite.angle + orientation.ordinal() * 90;
	}
	
	public void draw(SpriteBatch display)
	{
		if (type >= 0)
			sprite.draw(display);
	}
}
