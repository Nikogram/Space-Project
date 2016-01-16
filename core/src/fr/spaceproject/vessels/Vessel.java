package fr.spaceproject.vessels;

import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.spaceproject.utils.*;

enum VesselAction { MoveForward, MoveBackward, MoveLeft, MoveRight, TurnLeft, TurnRight; }

public class Vessel
{
	protected VesselModule[][] modules;
	protected boolean isAI;
	protected int faction;
	protected float timeBeforeCall;
	protected Vector<VesselAction> actions;
	
	
	public Vessel(Vec2f position, Vec2i size, Vec2i cockpitPosition, boolean isAI, int faction)
	{
		modules = new VesselModule[size.x][size.y];
		for (int x = 0; x < size.x; ++x)
		{
			for (int y = 0; y < size.y; ++y)
				modules[x][y] = new VesselModule((x == cockpitPosition.x && y == cockpitPosition.y ? 1 : -1), 1, Orientation.Up);
		}
		
		this.isAI = isAI;
		this.faction = faction;
		timeBeforeCall = 0;
		actions = new Vector<VesselAction>();
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
	
	public void setModule(Vec2i position, int type, int level, Orientation orientation)
	{
		if (position.x >= 0 && position.y >= 0 && position.x < modules.length && position.y < modules[0].length)
		{
			if (type == 2)
				modules[position.x][position.y] = new EngineVesselModule(type, level, orientation);
			else
				modules[position.x][position.y] = new VesselModule(type, level, orientation);
		}
	}
	
	public void update(float lastFrameTime)
	{
		Vec2i cockpitPosition = getCockpitPosition();
		Sprite sprite = modules[cockpitPosition.x][cockpitPosition.y].sprite;
		
		actions.clear();
		
		// Modification de la vitesse
		if (Gdx.input.isKeyPressed(Keys.Q))
		{
			sprite.angle += lastFrameTime * 100;
			actions.add(VesselAction.TurnLeft);
		}
		if (Gdx.input.isKeyPressed(Keys.D))
		{
			sprite.angle -= lastFrameTime * 100;
			actions.add(VesselAction.TurnRight);
		}
		
		
		// Modification de l'acceleration	
		sprite.acceleration.normalize(0);
		
		if (!Gdx.input.isKeyPressed(Keys.Z) && !Gdx.input.isKeyPressed(Keys.S) && !Gdx.input.isKeyPressed(Keys.A) && !Gdx.input.isKeyPressed(Keys.E))
			sprite.acceleration = new Vec2f(0, 0);
		else
		{			
			if (Gdx.input.isKeyPressed(Keys.Z))
			{
				sprite.acceleration.add(new Vec2f(0, 100), sprite.angle);
				actions.add(VesselAction.MoveForward);
			}
			if (Gdx.input.isKeyPressed(Keys.S))
			{
				sprite.acceleration.add(new Vec2f(0, -100), sprite.angle);
				actions.add(VesselAction.MoveBackward);
			}
			if (Gdx.input.isKeyPressed(Keys.A))
			{
				sprite.acceleration.add(new Vec2f(-100, 0), sprite.angle);
				actions.add(VesselAction.MoveLeft);
			}
			if (Gdx.input.isKeyPressed(Keys.E))
			{
				sprite.acceleration.add(new Vec2f(100, 0), sprite.angle);
				actions.add(VesselAction.MoveRight);
			}
		}
		
		if (sprite.speed.getLength() > 200)
			sprite.speed.normalize(200);
		
		sprite.acceleration.normalize(100);
		updateSpeed(lastFrameTime);			
		
		
		// Application de la mise à jour pour tous les modules		
		for (int x = 0; x < modules.length; ++x)
		{
			for (int y = 0; y < modules[x].length; ++y)
				modules[x][y].update(lastFrameTime, modules[cockpitPosition.x][cockpitPosition.y].sprite, new Vec2i(x - cockpitPosition.x, y - cockpitPosition.y), actions);
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
	
	public void generate(int configuration)
	{
		for (int x = 0; x < modules.length; ++x)
		{
			for (int y = 0; y < modules[x].length; ++y)
				modules[x][y].type = -1;
		}
		
		if (configuration == 2)
		{
			setModule(new Vec2i(0, 1), 2, 1, Orientation.Left);
			setModule(new Vec2i(0, 2), 2, 1, Orientation.Left);
			setModule(new Vec2i(0, 3), 2, 1, Orientation.Left);
			setModule(new Vec2i(1, 0), 2, 1, Orientation.Down);
			setModule(new Vec2i(1, 1), 0, 1, Orientation.Up);
			setModule(new Vec2i(1, 2), 0, 1, Orientation.Up);
			setModule(new Vec2i(1, 3), 0, 1, Orientation.Up);
			setModule(new Vec2i(1, 4), 2, 1, Orientation.Up);
			setModule(new Vec2i(2, 1), 2, 1, Orientation.Down);
			setModule(new Vec2i(2, 2), 1, 1, Orientation.Up);
			setModule(new Vec2i(3, 0), 2, 1, Orientation.Down);
			setModule(new Vec2i(3, 1), 0, 1, Orientation.Up);
			setModule(new Vec2i(3, 2), 0, 1, Orientation.Up);
			setModule(new Vec2i(3, 3), 0, 1, Orientation.Up);
			setModule(new Vec2i(3, 4), 2, 1, Orientation.Up);
			setModule(new Vec2i(4, 1), 2, 1, Orientation.Right);
			setModule(new Vec2i(4, 2), 2, 1, Orientation.Right);
			setModule(new Vec2i(4, 3), 2, 1, Orientation.Right);
			
		}
		else
		{
			setModule(new Vec2i(2, 3), 0, 1, Orientation.Up);
			setModule(new Vec2i(2, 2), 0, 1, Orientation.Up);
			setModule(new Vec2i(1, 1), 0, 1, Orientation.Up);
			setModule(new Vec2i(3, 1), 0, 1, Orientation.Up);
			setModule(new Vec2i(2, 1), 1, 1, Orientation.Up);
			setModule(new Vec2i(1, 0), 2, 1, Orientation.Down);
			setModule(new Vec2i(3, 0), 2, 1, Orientation.Down);
			setModule(new Vec2i(0, 1), 2, 1, Orientation.Left);
			setModule(new Vec2i(4, 1), 2, 1, Orientation.Right);
			setModule(new Vec2i(1, 2), 2, 1, Orientation.Up);
			setModule(new Vec2i(3, 2), 2, 1, Orientation.Up);
		}
	}
	
	protected Vec2i getCockpitPosition()
	{
		for (int x = 0; x < modules.length; ++x)
		{
			for (int y = 0; y < modules[x].length; ++y)
			{
				if (modules[x][y].type == 1)
					return new Vec2i(x, y);
			}
		}
		
		return new Vec2i(0, 0);
	}
}
