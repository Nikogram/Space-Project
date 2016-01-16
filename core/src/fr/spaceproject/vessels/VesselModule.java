package fr.spaceproject.vessels;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.spaceproject.utils.*;

class VesselModule
{
	protected int type;
	protected int level;
	protected float energy;
	protected Sprite sprite;	
	
	public VesselModule(int type, int level)
	{
		this.type = type;
		this.level = level;
		energy = getMaxEnergy();
		sprite = new Sprite(new Vec2f(), new Vec2f(), getTextureFileName());
	}
	
	public VesselModule(int type, int level, float energy)
	{
		this.type = type;
		this.level = level;
		this.energy = energy;
	}
	
	public int getType()
	{
		return type;
	}
	
	public String getTextureFileName()
	{
		if (type == 1)	// Cockpit
			return "SimpleVesselModule.png";
		else if (type == 2)	// Engine
			return "EngineVesselModule.png";
		else	// Simple
			return "SimpleVesselModule.png";
	}
	
	public int getLevel()
	{
		return level;
	}
	
	public float getEnergy()
	{
		return energy;
	}
	
	public float getMaxEnergy()
	{
		return 100 + 20 * level;
	}
	
	public void update(Sprite vesselSprite, Vec2i moduleRelativePosition)
	{
		sprite.position = vesselSprite.getRotatedPosition(new Vec2f(20 * moduleRelativePosition.x, 20 * moduleRelativePosition.y), vesselSprite.angle);
		sprite.speed = vesselSprite.speed;
		sprite.angle = vesselSprite.angle;
	}
	
	public void draw(SpriteBatch display)
	{
		if (type >= 0)
			sprite.draw(display);
	}
}
