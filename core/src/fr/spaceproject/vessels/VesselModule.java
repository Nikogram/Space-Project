package fr.spaceproject.vessels;

import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.spaceproject.station.Station;
import fr.spaceproject.utils.*;

public class VesselModule
{
	private int type;
	private int level;
	private float energy;
	private float subEnergy;
	final private Sprite sprite;
	private Orientation orientation;
	private TextureManager textureManager;
	private boolean isEngine;
	private boolean isTouched;
	
	
	public VesselModule(int type, int level, Orientation orientation, TextureManager textureManager)
	{
		this.isEngine = type == 2;
		this.textureManager = textureManager;
		this.type = type;
		this.level = level;
		energy = getMaxEnergy();
		subEnergy = 0;
		sprite = new Sprite(new Vec2f(), new Vec2f(), getTexture());
		sprite.setAngle(orientation.ordinal() * 90);
		this.orientation = orientation;
		isTouched = false;
	}
	
	public VesselModule(int type, int level, Orientation orientation, TextureManager textureManager, boolean isEngine)
	{
		this.isEngine = type == 2 || isEngine;
		this.textureManager = textureManager;
		this.type = type;
		this.level = level;
		energy = getMaxEnergy();
		sprite = new Sprite(new Vec2f(), new Vec2f(), getTexture());
		sprite.setAngle(orientation.ordinal() * 90);
		this.orientation = orientation;
	}
	
	public Texture getTexture()
	{
		// -2 : inexistant
		if (type == 1)	// Cockpit
			return textureManager.getTexture("CockpitVesselModule");
		else if (type == 2)	// Engine
			return textureManager.getTexture("EngineVesselModule");
		else if (type == 3)	// Cannon
			return textureManager.getTexture("CannonVesselModule");
		else if (type == 4)	// Laser
			return textureManager.getTexture("CannonVesselModule");
		else if (type == 5)	// Shield
			return textureManager.getTexture("ShieldVesselModule");
		else if (type == -1 && !isEngine)	// Broken
			return textureManager.getTexture("BrokenVesselModule");
		else if (type == -1)	// Broken engine
			return textureManager.getTexture("BrokenEngineVesselModule");
		else	// 0 : Simple
			return textureManager.getTexture("SimpleVesselModule");
	}
	
	public TextureManager getTextureManager()
	{
		return textureManager;
	}
	
	public int getType()
	{
		return type;
	}
	
	public void setType(int type)
	{
		this.type = type;
	}
	
	public int getLevel()
	{
		return level;
	}
	
	public float getEnergy()
	{
		if (subEnergy <= 0)
			return energy;
		return subEnergy;
	}
	
	public void setEnergy(float energy)
	{
		if (subEnergy <= 0)
			this.energy = energy;
		else
			subEnergy = energy;
	}
	
	public float getEnergy(boolean forceNormalEnergy)
	{
		if (forceNormalEnergy)
			return energy;
		return getEnergy();
	}
	
	public void setEnergy(float energy, boolean forceNormalEnergy)
	{
		if (forceNormalEnergy)
			this.energy = energy;
		else
			setEnergy(energy);
	}
	
	public float getSubEnergy()
	{
		return subEnergy;
	}
	
	public void setSubEnergy(float energy)
	{
		subEnergy = energy;
	}
	
	public Sprite getSprite()
	{
		return sprite.clone();
	}
	
	public Sprite getSprite(boolean copy)
	{
		if (copy)
			return sprite;
		return sprite;
	}
	
	public Vec2f getSpritePosition()
	{
		return sprite.getPosition();
	}
	
	public void setSpritePosition(Vec2f position)
	{
		sprite.setPosition(position);
	}
	
	public void moveSprite(Vec2f movement)
	{
		sprite.move(movement);
	}
	
	public Vec2f getSpriteSize()
	{
		return sprite.getSize();
	}
	
	public void setSpriteSize(Vec2f size)
	{
		sprite.setSize(size);
	}
	
	public float getSpriteAngle()
	{
		return sprite.getAngle();
	}
	
	public void setSpriteAngle(float angle)
	{
		sprite.setAngle(angle);
	}
	
	public Vec2f getSpriteSpeed()
	{
		return sprite.getSpeed();
	}
	
	public void setSpriteSpeed(Vec2f speed)
	{
		sprite.setSpeed(speed);
	}
	
	public Vec2f getSpriteAcceleration()
	{
		return sprite.getAcceleration();
	}
	
	public void setSpriteAcceleration(Vec2f acceleration)
	{
		sprite.setAcceleration(acceleration);
	}
	
	public void updateSpriteSpeed(float lastFrameTime, boolean angleIsTakenAccount)
	{
		sprite.updateSpeed(lastFrameTime, angleIsTakenAccount);
	}
	
	public Orientation getOrientation()
	{
		return Orientation.valueOf(orientation.toString());
	}
	
	public float getMaxEnergy()
	{
		return 100 + 20 * (level - 1);
	}
	
	public boolean isTouched()
	{
		return isTouched;
	}
	
	public void setIsTouched()
	{
		isTouched = true;
	}
	
	public Sprite updateCollisions(Vector<Vessel> vessels, Vessel moduleVessel, Station station, Vector<Vessel> shotVessels)
	{
		isTouched = false;
		
		for (int x = 0; x < station.getSize().x; ++x)
		{
			for (int y = 0; y < station.getSize().y; ++y)
			{
				if (sprite.getPosition().getDistance(station.getModulePosition(new Vec2i(x, y))) < 140 && 
						type >= 0 && station.getModuleType(new Vec2i(x, y)) >= 0 && sprite.isCollidedWithSprite(station.getModuleSprite(new Vec2i(x, y), false), new Vec2f()))
				{
					energy -= sprite.getSpeed().getLength() * 0.1;
					station.setModuleEnergy(new Vec2i(x, y), station.getModuleEnergy(new Vec2i(x, y)) - 30);
					station.addAttackingVessel(moduleVessel);
					return station.getModuleSprite(new Vec2i(x, y));
				}
			}
		}
		
		for (int i = 0; i < vessels.size(); ++i)
		{
			if (sprite.getPosition().getDistance(vessels.get(i).getCenter()) < 100)
			for (int x = 0; x < vessels.get(i).getSize().x && vessels.get(i) != moduleVessel; ++x)
			{
				for (int y = 0; y < vessels.get(i).getSize().y; ++y)
				{
					if (type >= 0 && vessels.get(i).getModuleType(new Vec2i(x, y)) >= 0 && sprite.isCollidedWithSprite(vessels.get(i).getModuleSprite(new Vec2i(x, y), false), new Vec2f()))
					{
						vessels.get(i).setModuleEnergy(new Vec2i(x, y), vessels.get(i).getModuleEnergy(new Vec2i(x, y), true) - 30, true);
						vessels.get(i).addAttackingVessel(moduleVessel);
						return vessels.get(i).getModuleSprite(new Vec2i(x, y));
					}
				}
			}
		}
		
		return null;
	}
	
	public void update(float lastFrameTime, Sprite vesselSprite, Vec2i moduleRelativePosition, Vector<VesselAction> actions)
	{
		sprite.setPosition(vesselSprite.getRotatedPosition(new Vec2f(20 * moduleRelativePosition.x, 20 * moduleRelativePosition.y), vesselSprite.getAngle()));
		sprite.setSpeed(vesselSprite.getSpeed());
		sprite.setAngle(vesselSprite.getAngle() + orientation.ordinal() * 90);
	}
	
	public void draw(SpriteBatch display)
	{
		if (type >= -1)
			sprite.draw(display);
	}
	
	public void drawForeground(SpriteBatch display)
	{
	}
}
