package fr.spaceproject.vessels.station;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

import fr.spaceproject.utils.Orientation;
import fr.spaceproject.utils.Sprite;
import fr.spaceproject.utils.TextureManager;
import fr.spaceproject.utils.Vec2f;
import fr.spaceproject.utils.Vec2i;
import fr.spaceproject.vessels.Vessel;
import fr.spaceproject.vessels.VesselAction;
import fr.spaceproject.vessels.VesselModule;
import fr.spaceproject.vessels.VesselModuleType;

public class Station extends Vessel
{
	public Station(Vec2f position, int faction, Vec2f sectorSize, TextureManager textureManager)
	{
		super(position, true, faction, sectorSize, textureManager);
		
		for (int x = 0; x < size.x; ++x)
		{
			for (int y = 0; y < size.y; ++y)
			{
				Orientation orientation = Orientation.Down;
				if (x == 0)
					orientation = Orientation.Left;
				else if (x == size.x - 1)
					orientation = Orientation.Right;
				else if (y == size.y - 1)
					orientation = Orientation.Up;
				
				
				if (x == 0 || y == 0 || x == size.x - 1 || y == size.y - 1)
					setModule(new Vec2i(x, y), VesselModuleType.Cannon, 1, orientation);
				else
					setModule(new Vec2i(x, y), VesselModuleType.Simple, 1, orientation);	
			}
		}
	}
	
	@Override
	public void setModule(Vec2i position, VesselModuleType type, int level, Orientation orientation)
	{
		if (position.x >= 0 && position.y >= 0 && position.x < modules.length && position.y < modules[0].length)
		{
			if (type.equals(VesselModuleType.Cannon))
				modules[position.x][position.y] = new CannonStationModule(level, orientation, textureManager);
			else if (type.equals(VesselModuleType.Shield))
				modules[position.x][position.y] = new StationModule(type, level, orientation, textureManager);
			else if (type.equals(VesselModuleType.Broken))
				modules[position.x][position.y] = new BrokenStationModule(level, modules[position.x][position.y].getSprite(false).getPosition(), orientation, textureManager);
			else
				modules[position.x][position.y] = new StationModule(type, level, orientation, textureManager);
		}
	}
	
	@Override
	public void update(float lastFrameTime, Vector<Vessel> vessels, Station station, int[] factionsAgressivity)
	{
		if (!isDestroyed)
		{
			VesselModule cockpit = modules[cockpitPosition.x][cockpitPosition.y];
			//cockpitPositionPixels = cockpit.getSpritePosition();
			
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
				AI.update(vessels, this, currentActions, factionsAgressivity, lastFrameTime * 100);
			
			
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
			Orientation pushOrientation = null;
			Vec2f initialSpeed = cockpit.getSpriteSpeed();
			Vec2f initialAcceleration = cockpit.getSpriteAcceleration();
			
			if (!currentActions.get(VesselAction.MoveForward) && !currentActions.get(VesselAction.MoveBackward) &&
					!currentActions.get(VesselAction.MoveLeft) && !currentActions.get(VesselAction.MoveRight))
				cockpit.setSpriteAcceleration(new Vec2f(0, 0));
			else
			{			
				if (currentActions.get(VesselAction.MoveForward))
				{
					pushOrientation = Orientation.Down;
					newAcceleration.add(new Vec2f(0, getAcceleration(pushOrientation)), cockpit.getSpriteAngle());
					actions.add(VesselAction.MoveForward);
				}
				if (currentActions.get(VesselAction.MoveBackward))
				{
					pushOrientation = Orientation.Up;
					newAcceleration.add(new Vec2f(0, -getAcceleration(pushOrientation)), cockpit.getSpriteAngle());
					actions.add(VesselAction.MoveBackward);
				}
				if (currentActions.get(VesselAction.MoveLeft))
				{
					pushOrientation = Orientation.Right;
					newAcceleration.add(new Vec2f(-getAcceleration(pushOrientation), 0), cockpit.getSpriteAngle());
					actions.add(VesselAction.MoveLeft);
				}
				if (currentActions.get(VesselAction.MoveRight))
				{
					pushOrientation = Orientation.Left;
					newAcceleration.add(new Vec2f(getAcceleration(pushOrientation), 0), cockpit.getSpriteAngle());
					actions.add(VesselAction.MoveRight);
				}
			}
			
			
			if (pushOrientation != null && newAcceleration.getLength() > getAcceleration(pushOrientation))
				newAcceleration.normalize(getAcceleration(pushOrientation));
			cockpit.setSpriteAcceleration(newAcceleration);
			updateSpeed(lastFrameTime);
			
			if (pushOrientation != null)
			{
				if (cockpit.getSpriteSpeed().getLength() > initialSpeed.getLength() && cockpit.getSpriteSpeed().getLength() > getMaxSpeed(pushOrientation))
					cockpit.setSpriteSpeed(cockpit.getSpriteSpeed().getNormalize(Math.max(initialSpeed.getLength(), getMaxSpeed(pushOrientation))));
			}
			
				
			// Autres actions
			if (currentActions.get(VesselAction.Shoot))
				actions.add(VesselAction.Shoot);
			
			
			// Application de la mise a jour pour tous les modules	
			loop:
			for (int x = 0; x < modules.length; ++x)
			{
				for (int y = 0; y < modules[x].length; ++y)
				{
					((StationModule)modules[x][y]).update(lastFrameTime, new Sprite(cockpitPositionPixels, new Vec2f(140, 140), textureManager.getTexture("Blank")), new Vec2i(x - cockpitPosition.x, y - cockpitPosition.y), actions, vessels, faction, factionsAgressivity);
					Sprite collidedObject = modules[x][y].updateCollisions(lastFrameTime, vessels, this, station, attackingVessels);
					
					if (collidedObject != null)
					{
						Vec2f forceVector = new Vec2f(collidedObject.getPosition().x - getPosition().x, collidedObject.getPosition().y - getPosition().y);
 						forceVector.normalize(-Math.min(cockpit.getSpriteSpeed().getLength() + 50, 300));
 						cockpit.setSpriteSpeed(forceVector);
 						collisionSound.play(1f);
					}
					
					if (modules[x][y].getEnergy(true) < 0 && modules[x][y].getType().ordinal() > VesselModuleType.Broken.ordinal())
						setModule(new Vec2i(x, y), VesselModuleType.Broken, 1, Orientation.Up);
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
			if (flamesAreShown && !isDestroyed)
				engineSound.resume();
			else
				engineSound.pause();
		}
		else if (explosion != null)
		{
			explosion.update(lastFrameTime);
			
			if (explosion.isFinished())
				explosion = null;
		}
	}
}
