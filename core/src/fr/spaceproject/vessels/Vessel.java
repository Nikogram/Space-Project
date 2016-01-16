package fr.spaceproject.vessels;

import java.util.Vector;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.spaceproject.utils.*;

public class Vessel
{
	protected VesselModule[][] modules;
	protected boolean isAI;
	protected int faction;
	protected float timeBeforeCall;
	
	public Vessel(Vec2f position, Vec2i size, Vec2i cockpitPosition, boolean isAI, int faction)
	{
		modules = new VesselModule[size.x][size.y];
		for (int x = 0; x < size.x; ++x)
		{
			for (int y = 0; y < size.y; ++y)
				modules[x][y] = new VesselModule((x == cockpitPosition.x && y == cockpitPosition.y ? 1 : -1), 1);
		}
		
		this.isAI = isAI;
		this.faction = faction;
		timeBeforeCall = 0;
	}
	
	public Vec2f getPosition()
	{
		Vec2i cockpitPosition = getCockpitPosition();
		return modules[cockpitPosition.x][cockpitPosition.y].sprite.position;
	}
	
	public Vec2f getSpeed()
	{
		Vec2i cockpitPosition = getCockpitPosition();
		return modules[cockpitPosition.x][cockpitPosition.y].sprite.speed;
	}
	
	public float getAngle()
	{
		Vec2i cockpitPosition = getCockpitPosition();
		return modules[cockpitPosition.x][cockpitPosition.y].sprite.angle;
	}
	
	public void setSpeed(Vec2f speed)
	{
		Vec2i cockpitPosition = getCockpitPosition();
		modules[cockpitPosition.x][cockpitPosition.y].sprite.speed = speed;
	}
	
	public void setAngle(float angle)
	{
		Vec2i cockpitPosition = getCockpitPosition();
		modules[cockpitPosition.x][cockpitPosition.y].sprite.angle = angle;
	}
	
	public void updateSpeed(float lastFrameTime)
	{
		for (int x = 0; x < modules.length; ++x)
		{
			for (int y = 0; y < modules[x].length; ++y)
				modules[x][y].sprite.updateSpeed(lastFrameTime);
		}
	}
	
	public void setModule(Vec2i position, int type, int level)
	{
		if (position.x >= 0 && position.y >= 0 && position.x < modules.length && position.y < modules[0].length)
			modules[position.x][position.y] = new VesselModule(type, level);
	}
	
	public void update()
	{
		Vec2i cockpitPosition = getCockpitPosition();
		
		for (int x = 0; x < modules.length; ++x)
		{
			for (int y = 0; y < modules[x].length; ++y)
				modules[x][y].update(modules[cockpitPosition.x][cockpitPosition.y].sprite, new Vec2i(x - cockpitPosition.x, y - cockpitPosition.y));
		}
	}
	
	public void draw(SpriteBatch display)
	{
		for (int x = 0; x < modules.length; ++x)
		{
			for (int y = 0; y < modules[x].length; ++y)
				modules[x][y].draw(display);
		}
	}
	
	protected Vec2i getCockpitPosition()
	{
		for (int x = 0; x < modules.length; ++x)
		{
			for (int y = 0; y < modules[x].length; ++y)
			{
				if (modules[x][y].getType() == 1)
					return new Vec2i(x, y);
			}
		}
		
		return new Vec2i(0, 0);
	}
}
