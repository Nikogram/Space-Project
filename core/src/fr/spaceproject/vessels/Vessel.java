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
				modules[x][y].setSpritePosition(position);
			}
		}
		
		this.isAI = isAI;
		AI = new VesselAI();
		this.faction = faction;
		timeBeforeCall = 0;
		actions = new Vector<VesselAction>();
		this.cockpitPosition = cockpitPosition;
		cockpitPositionPixels = modules[cockpitPosition.x][cockpitPosition.y].getSprite().getPosition();
		
		engineSound = Gdx.audio.newSound(Gdx.files.internal("EngineVesselModule.wav"));
		engineSound.loop();
		engineSound.play();
		engineSound.pause();
		collisionSound = Gdx.audio.newSound(Gdx.files.internal("CollisionVessel.mp3"));
		collisionSoundIsPlayed = false;
	}
	
	public void finalize()
	{
		engineSound.dispose();
		collisionSound.dispose();
	}
	
	public Vec2f getPosition()
	{
		return modules[cockpitPosition.x][cockpitPosition.y].getSpritePosition();
	}
	
	public void setPosition(Vec2f position)
	{
		modules[cockpitPosition.x][cockpitPosition.y].setSpritePosition(position);
	}
	
	public float getAngle()
	{
		return modules[cockpitPosition.x][cockpitPosition.y].getSpriteAngle();
	}
	
	public boolean isCollidedWithVessel(Vessel vessel)
	{
		for (int x = 0; x < modules.length; ++x)
		for (int y = 0; y < modules[x].length; ++y)
		{
			for (int i = 0; i < vessel.modules.length; ++i)
			for (int j = 0; j < vessel.modules[i].length; ++j)
			{
				if (modules[x][y].getType() >= 0 && vessel.modules[i][j].getType() >= 0 && modules[x][y].getSprite().isCollidedWithSprite(vessel.modules[i][j].getSprite(), new Vec2f()))
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
				modules[x][y].updateSpriteSpeed(lastFrameTime, false);
		}
	}
	
	public void setModule(Vec2i position, int type, int level, Orientation orientation)
	{
		if (position.x >= 0 && position.y >= 0 && position.x < modules.length && position.y < modules[0].length)
		{
			if (type == 1)
			{
				modules[position.x][position.y] = new VesselModule(type, level, orientation);
				modules[position.x][position.y].setSpritePosition(cockpitPositionPixels);
			}
			else if (type == 2)
				modules[position.x][position.y] = new EngineVesselModule(type, level, orientation);
			else if (type == 3)
				modules[position.x][position.y] = new CannonVesselModule(type, level, orientation);
			else if (type == 4)
				modules[position.x][position.y] = new LaserVesselModule(type, level, orientation);
			else if (type == 5)
				modules[position.x][position.y] = new ShieldVesselModule(type, level, orientation);
			else
				modules[position.x][position.y] = new VesselModule(type, level, orientation);
		}
	}
	
	public void update(float lastFrameTime, Vector<Vessel> vessels)
	{
		VesselModule cockpit = modules[cockpitPosition.x][cockpitPosition.y];
		cockpitPositionPixels = cockpit.getSpritePosition();
		
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
			cockpit.setSpriteAngle(cockpit.getSpriteAngle() + lastFrameTime * 100);
			actions.add(VesselAction.TurnLeft);
		}
		if (currentActions.get(VesselAction.TurnRight))
		{
			cockpit.setSpriteAngle(cockpit.getSpriteAngle() - lastFrameTime * 100);
			actions.add(VesselAction.TurnRight);
		}
		
		
		// Modification de l'acceleration
		Vec2f newAcceleration = new Vec2f(0, 0);
		
		if (!currentActions.get(VesselAction.MoveForward) && !currentActions.get(VesselAction.MoveBackward) &&
				!currentActions.get(VesselAction.MoveLeft) && !currentActions.get(VesselAction.MoveRight))
			cockpit.setSpriteAcceleration(new Vec2f(0, 0));
		else
		{			
			if (currentActions.get(VesselAction.MoveForward))
			{
				newAcceleration.add(new Vec2f(0, 100), cockpit.getSpriteAngle());
				actions.add(VesselAction.MoveForward);
			}
			if (currentActions.get(VesselAction.MoveBackward))
			{
				newAcceleration.add(new Vec2f(0, -100), cockpit.getSpriteAngle());
				actions.add(VesselAction.MoveBackward);
			}
			if (currentActions.get(VesselAction.MoveLeft))
			{
				newAcceleration.add(new Vec2f(-100, 0), cockpit.getSpriteAngle());
				actions.add(VesselAction.MoveLeft);
			}
			if (currentActions.get(VesselAction.MoveRight))
			{
				newAcceleration.add(new Vec2f(100, 0), cockpit.getSpriteAngle());
				actions.add(VesselAction.MoveRight);
			}
		}
		
		if (cockpit.getSpriteSpeed().getLength() > 200)
			cockpit.setSpriteSpeed(cockpit.getSpriteSpeed().normalize(200));
		
		
		if (newAcceleration.getLength() > 100)
			newAcceleration.normalize(100);
		cockpit.setSpriteAcceleration(newAcceleration);
		updateSpeed(lastFrameTime);
		
		
		// Autres actions
		if (currentActions.get(VesselAction.Shoot))
			actions.add(VesselAction.Shoot);
		
		
		// Application de la mise � jour pour tous les modules		
		for (int x = 0; x < modules.length; ++x)
		{
			for (int y = 0; y < modules[x].length; ++y)
			{
				modules[x][y].update(lastFrameTime, modules[cockpitPosition.x][cockpitPosition.y].getSprite(), new Vec2i(x - cockpitPosition.x, y - cockpitPosition.y), actions);
				Vessel collidedVessel = modules[x][y].updateCollisions(vessels, this);
				
				if (collidedVessel != null)
				{
					Vec2f forceVector = new Vec2f(collidedVessel.getPosition().x - getPosition().x, collidedVessel.getPosition().y - getPosition().y);
					forceVector.normalize(-cockpit.getSpriteSpeed().getLength() - 50);
					cockpit.setSpriteSpeed(cockpit.getSpriteSpeed().getAdd(forceVector));
					
					if (!collisionSoundIsPlayed)
						collisionSound.play();
				}
				
				
				if (modules[x][y].getEnergy() < 0)
				{
					if (modules[x][y].getType() == 2)
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
				modules[x][y].setType(-2);
		}
		
		if (configuration == 2)
		{
			setModule(new Vec2i(0, 1), 4, 5, Orientation.Left);
			setModule(new Vec2i(0, 2), 2, 1, Orientation.Left);
			setModule(new Vec2i(0, 3), 2, 1, Orientation.Left);
			setModule(new Vec2i(1, 0), 2, 1, Orientation.Down);
			setModule(new Vec2i(1, 1), 0, 1, Orientation.Up);
			setModule(new Vec2i(1, 2), 5, 1, Orientation.Left);
			setModule(new Vec2i(1, 3), 0, 1, Orientation.Up);
			setModule(new Vec2i(1, 4), 3, 5, Orientation.Up);
			setModule(new Vec2i(2, 1), 1, 1, Orientation.Up);
			setModule(new Vec2i(2, 3), 5, 1, Orientation.Up);
			setModule(new Vec2i(2, 4), 2, 1, Orientation.Up);
			setModule(new Vec2i(3, 0), 2, 1, Orientation.Down);
			setModule(new Vec2i(3, 1), 0, 1, Orientation.Up);
			setModule(new Vec2i(3, 2), 5, 1, Orientation.Right);
			setModule(new Vec2i(3, 3), 0, 1, Orientation.Up);
			setModule(new Vec2i(3, 4), 3, 5, Orientation.Up);
			setModule(new Vec2i(4, 1), 4, 5, Orientation.Right);
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
