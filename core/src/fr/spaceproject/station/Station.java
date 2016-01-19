package fr.spaceproject.station;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.spaceproject.utils.Orientation;
import fr.spaceproject.utils.TextureManager;
import fr.spaceproject.utils.Vec2f;
import fr.spaceproject.utils.Vec2i;

public class Station
{
	private StationModule[][] modules;
	private TextureManager textureManager;
	private Vec2f position;
	
	public Station(Vec2f position, Vec2i size, int faction, TextureManager textureManager)
	{	
		this.textureManager = textureManager;
		this.position = position.clone();
		this.position.add(new Vec2f(-(size.x - 1) * 140 / 2, -(size.y - 1) * 140 / 2), 0);
		
		modules = new StationModule[size.x][size.y];
		for (int x = 0; x < size.x; ++x)
		{
			for (int y = 0; y < size.y; ++y)
			{
				modules[x][y] = new StationModule(1, 1, new Vec2f(this.position.x + 140 * x, this.position.y + 140 * y), Orientation.Up, textureManager);
			}
		}
		
		this.position = position.clone();
	}
	
	public Vec2f getPosition()
	{
		return position.clone();
	}
	
	public Vec2i getSize()
	{
		return new Vec2i(modules.length, modules[0].length);
	}
	
	public boolean moduleIsShown(Vec2i modulePosition)
	{
		return modules[modulePosition.x][modulePosition.y].getType() >= 0;
	}
	
	public void generate(int configuration)
	{
	}
	
	public void draw(SpriteBatch display)
	{
		for (int x = 0; x < modules.length; ++x)
		{
			for (int y = 0; y < modules[x].length; ++y)
			{
				modules[x][y].draw(display);
			}
		}
	}
}
