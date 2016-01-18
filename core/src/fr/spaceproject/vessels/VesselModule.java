package fr.spaceproject.vessels;

import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.spaceproject.utils.*;

class VesselModule
{
	private int type;
	private int level;
	private float energy;
	private Sprite sprite;
	private Orientation orientation;
	private TextureManager textureManager;
	
	
	public VesselModule(int type, int level, Orientation orientation, TextureManager textureManager)
	{
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
		if (type == 1)	// Cockpit
			return textureManager.getTexture("CockpitVesselModule");
		else if (type == 2)	// Engine
			return textureManager.getTexture("EngineVesselModule");
		else if (type == 3)	// Cannon
			return textureManager.getTexture("CannonVesselModule");
		else if (type == 4)	// Shield
			return textureManager.getTexture("CannonVesselModule");
		else if (type == 5)	// Shield
			return textureManager.getTexture("ShieldVesselModule");
		else if (type == -1)	// Broken
			return textureManager.getTexture("BrokenVesselModule");
		else	// Simple
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
		return energy;
	}
	
	public void setEnergy(float energy)
	{
		this.energy = energy;
	}
	
	public Sprite getSprite()
	{
		return sprite.clone();
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
	
	public Vessel updateCollisions(Vector<Vessel> vessels, Vessel moduleVessel)
	{
		for (int i = 0; i < vessels.size(); ++i)
		{
			for (int x = 0; x < vessels.get(i).modules.length && vessels.get(i) != moduleVessel; ++x)
			{
				for (int y = 0; y < vessels.get(i).modules[x].length; ++y)
				{
					if (type >= 0 && vessels.get(i).modules[x][y].type >= 0 && sprite.isCollidedWithSprite(vessels.get(i).modules[x][y].sprite, new Vec2f()))
					{
						vessels.get(i).modules[x][y].energy -= 10;
						vessels.get(i).collisionSoundIsPlayed = true;
						return vessels.get(i);
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
}
