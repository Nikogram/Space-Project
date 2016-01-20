package fr.spaceproject.station;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.spaceproject.utils.Orientation;
import fr.spaceproject.utils.Sprite;
import fr.spaceproject.utils.TextureManager;
import fr.spaceproject.utils.Vec2f;

public class StationModule
{
	private TextureManager textureManager;
	private int type;
	private int level;
	private float energy;
	private Sprite sprite;
	
	
	public StationModule(int type, int level, Vec2f position, Orientation orientation, TextureManager textureManager)
	{
		this.textureManager = textureManager;
		this.type = type;
		this.level = level;
		energy = getMaxEnergy();
		sprite = new Sprite(position, new Vec2f(140, 140), getTexture());
	}
	
	public Texture getTexture()
	{
		if (type == -1)	// Broken
			return textureManager.getTexture("BrokenStationModule");
		else	// Simple
			return textureManager.getTexture("SimpleStationModule");
	}
	
	public float getMaxEnergy()
	{
		return 500 + 250 * (level - 1);
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
	
	public TextureManager getTextureManager()
	{
		return textureManager;
	}
	
	public void update(float lastFrameTime)
	{
	}
	
	public void draw(SpriteBatch display)
	{
		sprite.draw(display);
	}
}
