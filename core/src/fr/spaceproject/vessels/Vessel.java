package fr.spaceproject.vessels;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.spaceproject.utils.*;
import fr.spaceproject.vessels.station.Station;

public class Vessel
{
	protected VesselModule[][] modules;
	protected boolean isAI;
	protected VesselAI AI;
	protected int faction;
	protected Vec2f position;
	protected Vec2i size;
	protected Vector<VesselAction> actions;
	protected Vec2i cockpitPosition;
	protected Vec2f cockpitPositionPixels;
	protected Sound engineSound;
	protected Sound collisionSound;
	protected TextureManager textureManager;
	protected boolean isDestroyed;
	protected Explosion explosion;
	protected Vector<Vessel> attackingVessels;
	protected Vec2f nextSpeed;
	
	
	public Vessel(Vec2f position, boolean isAI, int faction, Vec2f sectorSize, TextureManager textureManager)
	{
		this.position = position;
		size = new Vec2i(7, 7);
		cockpitPosition = new Vec2i(3, 3);
		
		modules = new VesselModule[size.x][size.y];
		for (int x = 0; x < size.x; ++x)
		{
			for (int y = 0; y < size.y; ++y)
			{
				modules[x][y] = new VesselModule((x == cockpitPosition.x && y == cockpitPosition.y ? VesselModuleType.Cockpit : VesselModuleType.Inexisting), 1, Orientation.Up, textureManager);
				modules[x][y].setSpritePosition(position);
			}
		}
		
		this.isAI = isAI;
		AI = new VesselAI(sectorSize);
		this.faction = faction;
		actions = new Vector<VesselAction>();
		cockpitPositionPixels = modules[cockpitPosition.x][cockpitPosition.y].getSprite().getPosition();
		this.textureManager = textureManager;
		isDestroyed = false;
		
		engineSound = Gdx.audio.newSound(Gdx.files.internal("EngineVesselModule.wav"));
		engineSound.loop(0.5f);
		engineSound.pause();
		collisionSound = Gdx.audio.newSound(Gdx.files.internal("CollisionVessel.mp3"));
		attackingVessels = new Vector<Vessel>();
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
	
	public Vec2f getSpeed()
	{
		return modules[cockpitPosition.x][cockpitPosition.y].getSpriteSpeed();
	}
	
	public Vec2f getCenter()
	{
		return new Vec2f((modules[0][0].getSpritePosition().x + modules[modules.length - 1][modules[0].length - 1].getSpritePosition().x) / 2f,
			(modules[0][0].getSpritePosition().y + modules[modules.length - 1][modules[0].length - 1].getSpritePosition().y) / 2f);
	}
	
	public Vec2i getSize()
	{
		return new Vec2i(modules.length, modules[0].length);
	}
	
	public float getAngle()
	{
		return modules[cockpitPosition.x][cockpitPosition.y].getSpriteAngle();
	}
	
	public int getFaction()
	{
		return faction;
	}
	
	public float getModuleEnergy(Vec2i modulePositions)
	{
		return modules[modulePositions.x][modulePositions.y].getEnergy();
	}
	
	public float getModuleEnergy(Vec2i modulePositions, boolean forceNormalEnergy)
	{
		return modules[modulePositions.x][modulePositions.y].getEnergy(forceNormalEnergy);
	}
	
	public void setModuleEnergy(Vec2i modulePositions, float energy, boolean forceNormalEnergy)
	{
		modules[modulePositions.x][modulePositions.y].setEnergy(energy, forceNormalEnergy);
	}
	
	public float getModuleSubEnergy(Vec2i modulePositions)
	{
		return modules[modulePositions.x][modulePositions.y].getSubEnergy();
	}
	
	public void setModuleEnergy(Vec2i modulePositions, float energy)
	{
		modules[modulePositions.x][modulePositions.y].setEnergy(energy);
	}
	
	public float getModuleMaxEnergy(Vec2i modulePositions)
	{
		return modules[modulePositions.x][modulePositions.y].getMaxEnergy();
	}
	
	public Sprite getModuleSprite(Vec2i position)
	{
		return modules[position.x][position.y].getSprite();
	}
	
	public Sprite getModuleSprite(Vec2i position, boolean copy)
	{
		return modules[position.x][position.y].getSprite(copy);
	}
	
	public VesselModuleType getModuleType(Vec2i modulePositions)
	{
		return modules[modulePositions.x][modulePositions.y].getType();
	}
	
	public boolean moduleIsShown(Vec2i modulePositions)
	{
		return modules[modulePositions.x][modulePositions.y].getType().ordinal() > VesselModuleType.Broken.ordinal();
	}
	
	public Vec2f getModulePosition(Vec2i modulePositions)
	{
		return modules[modulePositions.x][modulePositions.y].getSpritePosition();
	}
	
	public Vec2f getModuleSize(Vec2i modulePositions)
	{
		return modules[modulePositions.x][modulePositions.y].getSpriteSize();
	}
	
	public void setModuleSize(Vec2i modulePositions, Vec2f size)
	{
		modules[modulePositions.x][modulePositions.y].setSpriteSize(size);
	}
	
	public void setModuleIsTouched(Vec2i modulePositions)
	{
		modules[modulePositions.x][modulePositions.y].setIsTouched();
	}
	
	public void updateSpeed(float lastFrameTime)
	{
		for (int x = 0; x < modules.length; ++x)
		{
			for (int y = 0; y < modules[x].length; ++y)
				modules[x][y].updateSpriteSpeed(lastFrameTime, false);
		}
	}
	
	public void updateModuleVertices(Vec2i modulePositions)
	{
		modules[modulePositions.x][modulePositions.y].updateVertices();
	}
	
	public void setModule(Vec2i position, VesselModuleType type, int level, Orientation orientation)
	{
		if (position.x >= 0 && position.y >= 0 && position.x < modules.length && position.y < modules[0].length)
		{
			if (type.equals(VesselModuleType.Cockpit))
			{
				modules[position.x][position.y] = new VesselModule(type, level, orientation, textureManager);
				modules[position.x][position.y].setSpritePosition(cockpitPositionPixels);
			}
			else if (type.equals(VesselModuleType.Engine))
				modules[position.x][position.y] = new EngineVesselModule(type, level, orientation, textureManager);
			else if (type.equals(VesselModuleType.Cannon))
				modules[position.x][position.y] = new CannonVesselModule(type, level, orientation, textureManager);
			else if (type.equals(VesselModuleType.Laser))
				modules[position.x][position.y] = new LaserVesselModule(type, level, orientation, textureManager);
			else if (type.equals(VesselModuleType.Shield))
				modules[position.x][position.y] = new ShieldVesselModule(type, level, orientation, textureManager);
			else
				modules[position.x][position.y] = new VesselModule(type, level, orientation, textureManager);
		}
	}
	
	public boolean isDestroyed()
	{
		return isDestroyed;
	}
	
	public boolean isExplosing()
	{
		return explosion != null;
	}
	
	public void addAttackingVessel(Vessel vessel)
	{
		attackingVessels.add(vessel);
	}
	
	public Vector<Vessel> getAttackingVessel()
	{
		Vector<Vessel> tempsAttackingVessels = new Vector<Vessel>();
		for (int i = 0; i < attackingVessels.size(); ++i)
			tempsAttackingVessels.add(attackingVessels.get(i));
		return tempsAttackingVessels;
	}
	
	public void clearAttackingVessel()
	{
		attackingVessels.clear();
	}
	
	public float getMaxSpeed(Orientation orientation)
	{
		int engineLevelCount = 0;
		for (int x = 0; x < modules.length; ++x)
		{
			for (int y = 0; y < modules[x].length; ++y)
			{
				if (modules[x][y].getType().equals(VesselModuleType.Engine) && orientation.equals(modules[x][y].getOrientation()))
					engineLevelCount += modules[x][y].getLevel();
			}
		}
		
		return 100 + 20 * engineLevelCount;
	}
	
	public float getAcceleration(Orientation orientation)
	{
		int engineLevelCount = 0;
		for (int x = 0; x < modules.length; ++x)
		{
			for (int y = 0; y < modules[x].length; ++y)
			{
				if (modules[x][y].getType().equals(VesselModuleType.Engine) && orientation.equals(modules[x][y].getOrientation()))
					engineLevelCount += modules[x][y].getLevel();
			}
		}
		
		return 50 + 10 * engineLevelCount;
	}
	
	public void revive()
	{
		isDestroyed = false;
	}
	
	public void update(float lastFrameTime, Vector<Vessel> vessels, Station station, int[] factionsAgressivity)
	{		
		if (!isDestroyed)
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
			for (int x = 0; x < modules.length; ++x)
			{
				for (int y = 0; y < modules[x].length; ++y)
				{
					modules[x][y].update(lastFrameTime, modules[cockpitPosition.x][cockpitPosition.y].getSprite(), new Vec2i(x - cockpitPosition.x, y - cockpitPosition.y), actions);
					Sprite collidedObject = modules[x][y].updateCollisions(lastFrameTime, vessels, this, station, attackingVessels);
					
					if (collidedObject != null)
					{						
						Sprite touchedModule = modules[x][y].getSprite();
						
						Vec2f forceVector = getForceVector(touchedModule, collidedObject);		
						if (forceVector.x == 0 && forceVector.y == 0)
							forceVector = getForceVector(collidedObject, touchedModule).getNegative();
						
						if (forceVector.x != 0 || forceVector.y != 0)
						{
							Vec2f speedSupport = new Vec2f(cockpit.getSpriteSpeed().getNormalize(1).x + 2 * forceVector.x,
								cockpit.getSpriteSpeed().getNormalize(1).y + 2 * forceVector.y);
							speedSupport.normalize(1);
							
							cockpit.setSpriteSpeed(cockpit.getSpriteSpeed().getAdd(forceVector.x * lastFrameTime * 500, forceVector.y * lastFrameTime * 500));
							cockpit.setSpriteSpeed(cockpit.getSpriteSpeed().getNormalize(Math.min(cockpit.getSpriteSpeed().getLength(), 300)));
						}
					}
					
					if (modules[x][y].getEnergy(true) < 0 && modules[x][y].getType().ordinal() > VesselModuleType.Broken.ordinal())
					{
						if (x == cockpitPosition.x && y == cockpitPosition.y)
						{
							isDestroyed = true;
							explosion = new Explosion(getCenter(), false, textureManager);
							
							for (int X = 0; X < modules.length; ++X)
							{
								for (int Y = 0; Y < modules[x].length; ++Y)
								{
									modules[X][Y] = new VesselModule(VesselModuleType.Inexisting, 1, Orientation.Up, textureManager);
								}
							}
						}
						
						modules[x][y] = new BrokenVesselModule(1, modules[x][y].getOrientation(), textureManager, modules[x][y].getType().equals(VesselModuleType.Engine));
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
	
	private Vec2f getForceVector(Sprite sprite1, Sprite sprite2)
	{
		// Recherche du module du cockpit en collision
		int moduleVertexId = -1;
		for (int i = 0; i < 4 && moduleVertexId == -1; ++i)
			if (sprite2.pointIsWithIn(sprite1.getVertex(i)))
				moduleVertexId = i;
		
		if (moduleVertexId != -1)	// un sommet a été trouvé
		{
			// Recherche du bord de l'autre objet en collision
			Vec2f A = sprite1.getPosition();
			Vec2f B = sprite1.getVertex(moduleVertexId);
			int collidedBorderId = -1;
			
			for (int i = 0; i < 4 && collidedBorderId == -1; ++i)
			{
				if (sprite2.borderIsCollidedWithSegment(B, A, i))
					collidedBorderId = i;
			}
			
			// Recherche du bon vecteur directeur orthogonal au bord récuperé
			Vec2f borderCenter = new Vec2f((sprite2.getVertex(collidedBorderId).x + sprite2.getVertex(collidedBorderId == 3 ? 0 : collidedBorderId + 1).x) / 2,
					(sprite2.getVertex(collidedBorderId).y + sprite2.getVertex(collidedBorderId == 3 ? 0 : collidedBorderId + 1).y) / 2);
			return borderCenter.getAdd(sprite2.getPosition().getNegative()).getNormalize(1);
		}
		
		return new Vec2f(0, 0);
	}
	
	public void draw(SpriteBatch display)
	{
		for (int x = 0; x < modules.length && !isDestroyed; ++x)
		{
			for (int y = 0; y < modules[x].length; ++y)
				if (modules[x][y].getType().ordinal() > VesselModuleType.Broken.ordinal())
					modules[x][y].draw(display);
		}
	}
	
	public void drawForeground(SpriteBatch display)
	{		
		if (explosion != null)
			explosion.draw(display);
		
		for (int x = 0; x < modules.length && !isDestroyed; ++x)
		{
			for (int y = 0; y < modules[x].length; ++y)
				modules[x][y].drawForeground(display);
		}
	}
	
	public void drawBackground(SpriteBatch display)
	{		
		for (int x = 0; x < modules.length && !isDestroyed; ++x)
		{
			for (int y = 0; y < modules[x].length; ++y)
				if (modules[x][y].getType().ordinal() <= VesselModuleType.Broken.ordinal())
					modules[x][y].draw(display);
		}
	}
	
	public void generate(int configuration)
	{
		isDestroyed = false;
		
		if (configuration == 2)
		{
			modules = new VesselModule[5][5];
			for (int x = 0; x < modules.length; ++x)
				for (int y = 0; y < modules[x].length; ++y)
					setModule(new Vec2i(x, y), VesselModuleType.Inexisting, 1, Orientation.Up);
			
			setModule(new Vec2i(0, 1), VesselModuleType.Laser, 1, Orientation.Left);
			setModule(new Vec2i(0, 2), VesselModuleType.Engine, 1, Orientation.Left);
			setModule(new Vec2i(0, 3), VesselModuleType.Engine, 1, Orientation.Left);
			setModule(new Vec2i(1, 0), VesselModuleType.Engine, 1, Orientation.Down);
			setModule(new Vec2i(1, 1), VesselModuleType.Simple, 1, Orientation.Up);
			setModule(new Vec2i(1, 2), VesselModuleType.Shield, 1, Orientation.Left);
			setModule(new Vec2i(1, 3), VesselModuleType.Simple, 1, Orientation.Up);
			setModule(new Vec2i(1, 4), VesselModuleType.Cannon, 1, Orientation.Up);
			setModule(new Vec2i(2, 1), VesselModuleType.Cockpit, 1, Orientation.Up);
			setModule(new Vec2i(2, 3), VesselModuleType.Shield, 1, Orientation.Up);
			setModule(new Vec2i(2, 4), VesselModuleType.Engine, 1, Orientation.Up);
			setModule(new Vec2i(3, 0), VesselModuleType.Engine, 1, Orientation.Down);
			setModule(new Vec2i(3, 1), VesselModuleType.Simple, 1, Orientation.Up);
			setModule(new Vec2i(3, 2), VesselModuleType.Shield, 1, Orientation.Right);
			setModule(new Vec2i(3, 3), VesselModuleType.Simple, 1, Orientation.Up);
			setModule(new Vec2i(3, 4), VesselModuleType.Cannon, 1, Orientation.Up);
			setModule(new Vec2i(4, 1), VesselModuleType.Laser, 1, Orientation.Right);
			setModule(new Vec2i(4, 2), VesselModuleType.Engine, 1, Orientation.Right);
			setModule(new Vec2i(4, 3), VesselModuleType.Engine, 1, Orientation.Right);
			
			cockpitPosition = new Vec2i(2, 1);
			cockpitPositionPixels = modules[cockpitPosition.x][cockpitPosition.y].getSprite().getPosition();
		}
		else if (configuration == 3)
		{
			modules = new VesselModule[3][3];
			for (int x = 0; x < modules.length; ++x)
				for (int y = 0; y < modules[x].length; ++y)
					setModule(new Vec2i(x, y), VesselModuleType.Inexisting, 1, Orientation.Up);
			
			setModule(new Vec2i(0, 0), VesselModuleType.Engine, 1, Orientation.Down);
			setModule(new Vec2i(0, 1), VesselModuleType.Shield, 1, Orientation.Left);
			setModule(new Vec2i(0, 2), VesselModuleType.Cannon, 1, Orientation.Up);
			setModule(new Vec2i(1, 0), VesselModuleType.Shield, 1, Orientation.Down);
			setModule(new Vec2i(1, 1), VesselModuleType.Cockpit, 1, Orientation.Up);
			setModule(new Vec2i(1, 2), VesselModuleType.Shield, 1, Orientation.Up);
			setModule(new Vec2i(2, 0), VesselModuleType.Engine, 1, Orientation.Down);
			setModule(new Vec2i(2, 1), VesselModuleType.Shield, 1, Orientation.Right);
			setModule(new Vec2i(2, 2), VesselModuleType.Laser, 1, Orientation.Up);
			
			cockpitPosition = new Vec2i(1, 1);
			cockpitPositionPixels = modules[cockpitPosition.x][cockpitPosition.y].getSprite().getPosition();
		}
		else
		{
			modules = new VesselModule[5][5];
			for (int x = 0; x < modules.length; ++x)
				for (int y = 0; y < modules[x].length; ++y)
					setModule(new Vec2i(x, y), VesselModuleType.Inexisting, 1, Orientation.Up);
			
			setModule(new Vec2i(2, 3), VesselModuleType.Simple, 1, Orientation.Up);
			setModule(new Vec2i(2, 2), VesselModuleType.Simple, 1, Orientation.Up);
			setModule(new Vec2i(1, 1), VesselModuleType.Simple, 1, Orientation.Up);
			setModule(new Vec2i(3, 1), VesselModuleType.Simple, 1, Orientation.Up);
			setModule(new Vec2i(2, 1), VesselModuleType.Cockpit, 1, Orientation.Up);
			setModule(new Vec2i(1, 0), VesselModuleType.Engine, 1, Orientation.Down);
			setModule(new Vec2i(3, 0), VesselModuleType.Engine, 1, Orientation.Down);
			setModule(new Vec2i(0, 1), VesselModuleType.Engine, 1, Orientation.Left);
			setModule(new Vec2i(4, 1), VesselModuleType.Engine, 1, Orientation.Right);
			setModule(new Vec2i(1, 2), VesselModuleType.Engine, 1, Orientation.Up);
			setModule(new Vec2i(3, 2), VesselModuleType.Engine, 1, Orientation.Up);
			
			cockpitPosition = new Vec2i(2, 1);
			cockpitPositionPixels = modules[cockpitPosition.x][cockpitPosition.y].getSprite().getPosition();
		}
	}
}
