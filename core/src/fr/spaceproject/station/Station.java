package fr.spaceproject.station;

import java.util.Collections;
import java.util.Vector;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.spaceproject.utils.Orientation;
import fr.spaceproject.utils.Sprite;
import fr.spaceproject.utils.TextureManager;
import fr.spaceproject.utils.Vec2f;
import fr.spaceproject.utils.Vec2i;
import fr.spaceproject.vessels.Vessel;
import fr.spaceproject.vessels.VesselModule;

public class Station
{
	private StationModule[][] modules;
	private TextureManager textureManager;
	private Vec2f position;
	private Vector<Vessel> attackingVessels;
	
	public Station(Vec2f position, Vec2i size, int faction, TextureManager textureManager)
	{	
		this.textureManager = textureManager;
		this.position = position.clone();
		this.position.add(new Vec2f(-(size.x - 1) * 140 / 2, -(size.y - 1) * 140 / 2), 0);
		
		modules = new StationModule[size.x][size.y];
		for (int x = 0; x < size.x; ++x)
		{
			for (int y = 0; y < size.y; ++y)
				modules[x][y] = new StationModule(1, 1, new Vec2f(this.position.x + 140 * x, this.position.y + 140 * y), Orientation.Up, textureManager);
		}
		
		this.position = position.clone();
		attackingVessels = new Vector<Vessel>();
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
	
	public int getModuleType(Vec2i position)
	{
		return modules[position.x][position.y].getType();
	}
	
	public Vec2f getModulePosition(Vec2i position)
	{
		return modules[position.x][position.y].getSprite(false).getPosition();
	}
	
	public Vec2f getModuleSize(Vec2i position)
	{
		return modules[position.x][position.y].getSprite(false).getSize();
	}
	
	public Sprite getModuleSprite(Vec2i position)
	{
		return modules[position.x][position.y].getSprite();
	}
	
	public Sprite getModuleSprite(Vec2i position, boolean copy)
	{
		if (copy)
			return modules[position.x][position.y].getSprite(copy);
		return modules[position.x][position.y].getSprite();
	}
	
	public float getModuleEnergy(Vec2i position)
	{
		return modules[position.x][position.y].getEnergy();
	}
	
	public void setModuleEnergy(Vec2i position, float energy)
	{
		modules[position.x][position.y].setEnergy(energy);
	}
	
	public void addAttackingVessel(Vessel vessel)
	{
		attackingVessels.add(vessel);
	}
	
	public void generate(int configuration)
	{
	}
	
	public Vector<Vessel> update(float lastFrameTime)
	{
		for (int x = 0; x < modules.length; ++x)
		{
			for (int y = 0; y < modules[0].length; ++y)
			{
				if (modules[x][y].getEnergy() < 0 && modules[x][y].getType() >= 0)
					modules[x][y] = new BrokenStationModule(1, modules[x][y].getSprite(false).getPosition(), Orientation.Up, textureManager);
				else
					modules[x][y].update(lastFrameTime);
			}
		}
		
		Vector<Vessel> tempVessels = new Vector<Vessel>();
		for (int i = 0; i < attackingVessels.size(); ++i)
			tempVessels.add(attackingVessels.get(i));
		attackingVessels.clear();
		return tempVessels;
	}
	
	public void draw(SpriteBatch display)
	{
		for (int x = 0; x < modules.length; ++x)
		{
			for (int y = 0; y < modules[x].length; ++y)
			{
				if (modules[x][y].getType() >= 0)
					modules[x][y].draw(display);
			}
		}
	}
	
	public void drawBackground(SpriteBatch display)
	{
		for (int x = 0; x < modules.length; ++x)
		{
			for (int y = 0; y < modules[x].length; ++y)
			{
				if (modules[x][y].getType() < 0)
					modules[x][y].draw(display);
			}
		}
	}
}
