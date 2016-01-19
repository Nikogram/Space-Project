package fr.spaceproject.station;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.spaceproject.utils.Orientation;
import fr.spaceproject.utils.Sprite;
import fr.spaceproject.utils.TextureManager;
import fr.spaceproject.utils.Vec2f;

public class StationModule
{
	private TextureManager textureManager;
	private Sprite sprite;
	private int type;
	private int level;
	private float energy;
	
	public StationModule(int type, int level, Vec2f position, Orientation orientation, TextureManager textureManager)
	{
		this.textureManager = textureManager;
		sprite = new Sprite(position, new Vec2f(140, 140), getTexture());
		this.type = type;
		this.level = level;
		energy = getMaxEnergy();
	}
	
	public Texture getTexture()
	{
		if (type == 1)	// Simple
			return textureManager.getTexture("SimpleStationModule");
		else	// Simple
			return textureManager.getTexture("SimpleStationModule");
	}
	
	public float getMaxEnergy()
	{
		return 5000 + 1000 * (level - 1);
	}
	
	public int getType()
	{
		return type;
	}
	
	public int getLevel()
	{
		return type;
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
	
	public Sprite getSprite(boolean copy)
	{
		if (copy)
			return sprite.clone();
		return sprite;
	}
	
	public void draw(SpriteBatch display)
	{
		sprite.draw(display);
	}
}
