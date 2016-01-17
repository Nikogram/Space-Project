package fr.spaceproject.vessels;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.spaceproject.utils.*;

enum VesselAction { MoveForward, MoveBackward, MoveLeft, MoveRight, TurnLeft, TurnRight, Shoot; }

public class Vessel
{
	protected VesselModule[][] modules;
	protected boolean isAI;
	protected VesselAI AI;
	protected int faction;
	protected float timeBeforeCall;
	protected Vector<VesselAction> actions;
	protected Vec2i cockpitPosition;
	protected Vec2f cockpitPositionPixels;
	protected Sound engineSound;
	protected Sound collisionSound;
	protected boolean collisionSoundIsPlayed;
	
	
	public Vessel(Vec2f position, Vec2i size, Vec2i cockpitPosition, boolean isAI, int faction)
	{
		modules = new VesselModule[size.x][size.y];
		for (int x = 0; x < size.x; ++x)
		{
			for (int y = 0; y < size.y; ++y)
			{
				modules[x][y] = new VesselModule((x == cockpitPosition.x && y == cockpitPosition.y ? 1 : -2), 1, Orientation.Up);
				modules[x][y].sprite.position = position;
			}
		}
		
		this.isAI = isAI;
		AI = new VesselAI();
		this.faction = faction;
		timeBeforeCall = 0;
		actions = new Vector<VesselAction>();
		this.cockpitPosition = cockpitPosition;
		cockpitPositionPixels = modules[cockpitPosition.x][cockpitPosition.y].sprite.position;
		
		engineSound = Gdx.audio.newSound(Gdx.files.internal("EngineVesselModule.wav"));
		engineSound.loop();
		engineSound.play();
		engineSound.pause();
		collisionSound = Gdx.audio.newSound(Gdx.files.internal("CollisionVessel.mp3"));
		collisionSoundIsPlayed = false;
	}
	
	public final Vec2f getPosition()
	{
		return new Vec2f(modules[cockpitPosition.x][cockpitPosition.y].sprite.position);
	}
	
	public float getAngle()
	{
		return modules[cockpitPosition.x][cockpitPosition.y].sprite.angle;
	}
	
	public boolean isCollidedWithVessel(Vessel vessel)
	{
		for (int x = 0; x < modules.length; ++x)
		for (int y = 0; y < modules[x].length; ++y)
		{
			for (int i = 0; i < vessel.modules.length; ++i)
			for (int j = 0; j < vessel.modules[i].length; ++j)
			{
				if (modules[x][y].type >= 0 && vessel.modules[i][j].type >= 0 && modules[x][y].sprite.isCollidedWithSprite(vessel.modules[i][j].sprite, new Vec2f()))
					return true;
			}
		}
		
		return false;
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
			if (type == 1)
			{
				modules[position.x][position.y] = new VesselModule(type, level, orientation);
				modules[position.x][position.y].sprite.position = cockpitPositionPixels;
			}
			else if (type == 2)
				modules[position.x][position.y] = new EngineVesselModule(type, level, orientation);
			else if (type == 3)
				modules[position.x][position.y] = new CannonVesselModule(type, level, orientation);
			else if (type == 4)
				modules[position.x][position.y] = new ShieldVesselModule(type, level, orientation);
			else
				modules[position.x][position.y] = new VesselModule(type, level, orientation);
		}
	}
	
	public void update(float lastFrameTime, Vector<Vessel> vessels)
	{
		Sprite sprite = modules[cockpitPosition.x][cockpitPosition.y].sprite;
		cockpitPositionPixels = sprite.position;
		
		actions.clear();
		
		Map<VesselAction, Boolean> currentActions = new LinkedHashMap<VesselAction, Boolean>();
		currentActions.put(VesselAction.TurnLeft, false);
		currentActions.put(VesselAction.TurnRight, false);
		currentActions.put(VesselAction.MoveForward, false);
		currentActions.put(VesselAction.MoveBackward, false);
		currentActions.put(VesselAction.MoveLeft, false);
		currentActions.put(VesselAction.MoveRight, false);
		currentActions.put(VesselAction.Shoot, false);
		
		// Generation des actions
		if (!isAI)
		{
			if (Gdx.input.isKeyPressed(Keys.Q))
				currentActions.put(VesselAction.TurnLeft, true);
			if (Gdx.input.isKeyPressed(Keys.D))
				currentActions.put(VesselAction.TurnRight, true);
			if (Gdx.input.isKeyPressed(Keys.Z))
				currentActions.put(VesselAction.MoveForward, true);
			if (Gdx.input.isKeyPressed(Keys.S))
				currentActions.put(VesselAction.MoveBackward, true);
			if (Gdx.input.isKeyPressed(Keys.A))
				currentActions.put(VesselAction.MoveLeft, true);
			if (Gdx.input.isKeyPressed(Keys.E))
				currentActions.put(VesselAction.MoveRight, true);
			if (Gdx.input.isKeyPressed(Keys.SPACE))
				currentActions.put(VesselAction.Shoot, true);
		}
		else
			AI.update(this, vessels.get(0), currentActions, lastFrameTime * 100);
		
		
		// Modification de la vitesse
		if (currentActions.get(VesselAction.TurnLeft))
		{
			sprite.angle += lastFrameTime * 100;
			actions.add(VesselAction.TurnLeft);
		}
		if (currentActions.get(VesselAction.TurnRight))
		{
			sprite.angle -= lastFrameTime * 100;
			actions.add(VesselAction.TurnRight);
		}
		
		
		// Modification de l'acceleration
		sprite.acceleration.normalize(0);
		
		
		if (!currentActions.get(VesselAction.MoveForward) && !currentActions.get(VesselAction.MoveBackward) &&
				!currentActions.get(VesselAction.MoveLeft) && !currentActions.get(VesselAction.MoveRight))
			sprite.acceleration = new Vec2f(0, 0);
		else
		{			
			if (currentActions.get(VesselAction.MoveForward))
			{
				sprite.acceleration.add(new Vec2f(0, 100), sprite.angle);
				actions.add(VesselAction.MoveForward);
			}
			if (currentActions.get(VesselAction.MoveBackward))
			{
				sprite.acceleration.add(new Vec2f(0, -100), sprite.angle);
				actions.add(VesselAction.MoveBackward);
			}
			if (currentActions.get(VesselAction.MoveLeft))
			{
				sprite.acceleration.add(new Vec2f(-100, 0), sprite.angle);
				actions.add(VesselAction.MoveLeft);
			}
			if (currentActions.get(VesselAction.MoveRight))
			{
				sprite.acceleration.add(new Vec2f(100, 0), sprite.angle);
				actions.add(VesselAction.MoveRight);
			}
		}
		
		if (sprite.speed.getLength() > 200)
			sprite.speed.normalize(200);
		
		if (sprite.acceleration.getLength() > 100)
			sprite.acceleration.normalize(100);
		updateSpeed(lastFrameTime);
		
		
		// Autres actions
		if (currentActions.get(VesselAction.Shoot))
			actions.add(VesselAction.Shoot);
		
		
		// Application de la mise à jour pour tous les modules		
		for (int x = 0; x < modules.length; ++x)
		{
			for (int y = 0; y < modules[x].length; ++y)
			{
				modules[x][y].update(lastFrameTime, modules[cockpitPosition.x][cockpitPosition.y].sprite, new Vec2i(x - cockpitPosition.x, y - cockpitPosition.y), actions);
				Vessel collidedVessel = modules[x][y].updateCollisions(vessels, this);
				
				if (collidedVessel != null)
				{
					Vec2f forceVector = new Vec2f(collidedVessel.getPosition().x - getPosition().x, collidedVessel.getPosition().y - getPosition().y);
					forceVector.normalize(-sprite.speed.getLength() - 50);
					sprite.speed.add(forceVector, 0);
					
					if (!collisionSoundIsPlayed)
						collisionSound.play();
				}
				
				
				if (modules[x][y].energy < 0)
				{
					if (modules[x][y].type == 2)
						modules[x][y] = new VesselModule(-2, 1, Orientation.Up);
					else
						modules[x][y] = new VesselModule(-1, 1, Orientation.Up);
				}
			}
		}
		
		
		// Gestion du son des moteurs
		boolean flamesAreShown = false;
		for (int i = 0; i < actions.size(); ++i)
		{
			if (actions.get(i).equals(VesselAction.MoveForward))
				flamesAreShown = true;
			else if (actions.get(i).equals(VesselAction.MoveBackward))
				flamesAreShown = true;
			else if (actions.get(i).equals(VesselAction.MoveLeft))
				flamesAreShown = true;
			else if (actions.get(i).equals(VesselAction.MoveRight))
				flamesAreShown = true;
		}
		
		
		// Gestion du son		
		if (flamesAreShown)
			engineSound.resume();
		else
			engineSound.pause();
		
		collisionSoundIsPlayed = false;
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
				modules[x][y].type = -2;
		}
		
		if (configuration == 2)
		{
			setModule(new Vec2i(0, 1), 3, 5, Orientation.Left);
			setModule(new Vec2i(0, 2), 2, 1, Orientation.Left);
			setModule(new Vec2i(0, 3), 2, 1, Orientation.Left);
			setModule(new Vec2i(1, 0), 2, 1, Orientation.Down);
			setModule(new Vec2i(1, 1), 0, 1, Orientation.Up);
			setModule(new Vec2i(1, 2), 4, 1, Orientation.Left);
			setModule(new Vec2i(1, 3), 0, 1, Orientation.Up);
			setModule(new Vec2i(1, 4), 3, 5, Orientation.Up);
			setModule(new Vec2i(2, 1), 1, 1, Orientation.Up);
			setModule(new Vec2i(2, 3), 4, 1, Orientation.Up);
			setModule(new Vec2i(2, 4), 2, 1, Orientation.Up);
			setModule(new Vec2i(3, 0), 2, 1, Orientation.Down);
			setModule(new Vec2i(3, 1), 0, 1, Orientation.Up);
			setModule(new Vec2i(3, 2), 4, 1, Orientation.Right);
			setModule(new Vec2i(3, 3), 0, 1, Orientation.Up);
			setModule(new Vec2i(3, 4), 3, 5, Orientation.Up);
			setModule(new Vec2i(4, 1), 3, 5, Orientation.Right);
			setModule(new Vec2i(4, 2), 2, 1, Orientation.Right);
			setModule(new Vec2i(4, 3), 2, 1, Orientation.Right);
			
		}
		else if (configuration == 3)
		{
			setModule(new Vec2i(2, 0), 0, 1, Orientation.Up);
			setModule(new Vec2i(2, 1), 1, 1, Orientation.Up);
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
}
