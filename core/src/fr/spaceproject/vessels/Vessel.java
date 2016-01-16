package fr.spaceproject.vessels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
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
	
	public void updateSpeed(float lastFrameTime)
	{
		for (int x = 0; x < modules.length; ++x)
		{
			for (int y = 0; y < modules[x].length; ++y)
				modules[x][y].sprite.updateSpeed(lastFrameTime, false);
		}
	}
	
	public void setModule(Vec2i position, int type, int level)
	{
		if (position.x >= 0 && position.y >= 0 && position.x < modules.length && position.y < modules[0].length)
			modules[position.x][position.y] = new VesselModule(type, level);
	}
	
	public void update(float lastFrameTime)
	{
		Vec2i cockpitPosition = getCockpitPosition();
		Sprite sprite = modules[cockpitPosition.x][cockpitPosition.y].sprite;
		
		// Modification de la vitesse
		updateSpeed(lastFrameTime);
		
		
		/*if (Gdx.input.isKeyPressed(Keys.UP))
			sprite.acceleration = sprite.speed.y < 200 ? new Vec2f(0, 100) : new Vec2f(0, 0);
		else if (Gdx.input.isKeyPressed(Keys.DOWN))
			sprite.acceleration = sprite.speed.y > -200 ? new Vec2f(0, -100) : new Vec2f(0, 0);
		else
			sprite.acceleration = new Vec2f(0, 0);*/
		
		sprite.acceleration.normalize(0);
		if (!Gdx.input.isKeyPressed(Keys.Z) && !Gdx.input.isKeyPressed(Keys.S) && !Gdx.input.isKeyPressed(Keys.LEFT) && !Gdx.input.isKeyPressed(Keys.RIGHT))
			sprite.acceleration = new Vec2f(0, 0);
		else
		{
			if (Gdx.input.isKeyPressed(Keys.Z))
				sprite.acceleration.add(new Vec2f(0, 100), sprite.angle);
			if (Gdx.input.isKeyPressed(Keys.S))
				sprite.acceleration.add(new Vec2f(0, -100), sprite.angle);
			if (Gdx.input.isKeyPressed(Keys.LEFT))
				sprite.acceleration.add(new Vec2f(-100, 0), sprite.angle);
			if (Gdx.input.isKeyPressed(Keys.RIGHT))
				sprite.acceleration.add(new Vec2f(100, 0), sprite.angle);
		}
		
		sprite.acceleration.normalize(100);
		
		if (Gdx.input.isKeyPressed(Keys.Q))
			sprite.angle += lastFrameTime * 100;
		if (Gdx.input.isKeyPressed(Keys.D))
			sprite.angle -= lastFrameTime * 100;
			
		
		
		// Application de la mise à jour pour tous les modules		
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
