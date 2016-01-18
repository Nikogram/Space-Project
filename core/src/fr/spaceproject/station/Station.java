package fr.spaceproject.station;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.spaceproject.utils.Orientation;
import fr.spaceproject.utils.TextureManager;
import fr.spaceproject.utils.Vec2f;
import fr.spaceproject.utils.Vec2i;

public class Station
{
	protected StationModule[][] modules;
	protected TextureManager textureManager;
	
	public Station(Vec2f position, Vec2i size, int faction, TextureManager textureManager)
	{
		this.textureManager = textureManager;
		position.add(new Vec2f(-size.x * 140 / 2, -size.y * 140 / 2), 0);
		
		modules = new StationModule[size.x][size.y];
		for (int x = 0; x < size.x; ++x)
		{
			for (int y = 0; y < size.y; ++y)
			{
				modules[x][y] = new StationModule(1, 1, new Vec2f(position.x + 140 * x, position.y + 140 * y), Orientation.Up, textureManager);
			}
		}
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
