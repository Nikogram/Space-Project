package fr.spaceproject.station;

import java.util.Map;
import java.util.Vector;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.spaceproject.utils.Orientation;
import fr.spaceproject.utils.Sprite;
import fr.spaceproject.utils.TextureManager;
import fr.spaceproject.utils.Vec2f;
import fr.spaceproject.utils.Vec2i;
import fr.spaceproject.vessels.Projectile;
import fr.spaceproject.vessels.Vessel;

public class CannonStationModule extends StationModule
{
	private Sprite cannonSprite;
	Vessel targetVessel;
	boolean isShooting;
	protected Vector<Projectile> projectiles;
	protected float timeAfterShoot;
	
	public CannonStationModule(int level, Vec2f position, Orientation orientation, TextureManager textureManager)
	{
		super(1, level, position, orientation, textureManager);
		cannonSprite = new Sprite(new Vec2f(getSpritePosition()), new Vec2f(), textureManager.getTexture("CannonStationModule"));
		cannonSprite.setAngle(getOrientation().ordinal() * 90);
		targetVessel = null;
		isShooting = true;
		projectiles = new Vector<Projectile>();
		timeAfterShoot = 0;
	}
	
	public float getPower()
	{
		return 50 + 50f * (getLevel() - 1);
	}
	
	public float getTimeBeforeShoot()
	{
		return (float)Math.max(0.4, 2 - 0.02 * (getLevel() - 1));
	}
	
	public float getProjectileLifeTime()
	{
		return 5;
	}
	
	public float getProjectileSpeed()
	{
		return 600;
	}
	
	@Override
	public void update(float lastFrameTime, int faction, int[] factionsAgressivity, Vector<Vessel> vessels)
	{
		super.update(lastFrameTime,faction, factionsAgressivity, vessels);
		timeAfterShoot += lastFrameTime;
		
		if (isShooting)
			isShooting = false;
		
		targetVessel =  getClosestVessel(vessels, faction, factionsAgressivity);
		if (targetVessel != null)
		{
			focusPoint(lastFrameTime, targetVessel.getPosition());
			if (cannonSprite.getAngle() > getOrientation().ordinal() * 90 + 45)
				cannonSprite.setAngle(getOrientation().ordinal() * 90 + 45);
			else if (cannonSprite.getAngle() < getOrientation().ordinal() * 90 - 45)
				cannonSprite.setAngle(getOrientation().ordinal() * 90 - 45);
			 
			 
			Vec2f distancePoint = new Vec2f(cannonSprite.getPosition().x - targetVessel.getPosition().x, cannonSprite.getPosition().y - targetVessel.getPosition().y);		
			Vec2f sightVector = new Vec2f(-1, 0);
			sightVector.rotate(cannonSprite.getAngle());
			
			float angle = (float)Math.toDegrees(getAnglesDifference(distancePoint, sightVector)) - 90;
			 
			if (getSpritePosition().getDistance(targetVessel.getPosition()) < 600 && Math.abs(angle) < 10)
				isShooting = true;
		}
		
		if (timeAfterShoot >= getTimeBeforeShoot() && isShooting)
		{
			Vec2f projectilePosition = getSprite().getRotatedPosition(new Vec2f(0, -getSprite(false).getSize().y / 2), cannonSprite.getAngle() - 180);
			projectiles.add(new Projectile(projectilePosition, new Vec2f(0, -getProjectileSpeed()), cannonSprite.getAngle() - 180, getProjectileLifeTime(), getTextureManager()));
			timeAfterShoot = 0;
			projectiles.get(projectiles.size() - 1).getSprite(false).setColor(new Color(1, 1, 0.0f, 1));
		}
		
		for (int i = projectiles.size() - 1; i >= 0; --i)
		{
			projectiles.get(i).update(lastFrameTime);
			if (projectiles.get(i).getTimeBeforeDestruction() <= 0)
				projectiles.remove(i);
		}
	}
	
	@Override
	public void updateCollisions(Vector<Vessel> vessels)
	{
		for (int i = 0; i < vessels.size(); ++i)
		{
			for (int p = projectiles.size() - 1; p >= 0; --p)
			{
				if (projectiles.get(p).getSpritePosition().getDistance(vessels.get(i).getCenter()) < 100)
				{
					loop:
					for (int x = 0; x < vessels.get(i).getSize().x; ++x)
					for (int y = 0; y < vessels.get(i).getSize().y ; ++y)
					{
						boolean loopIsBroken = false;
						boolean spriteIsResized = false;
						
						if (vessels.get(i).getModuleType(new Vec2i(x, y)) == 5 && vessels.get(i).getModuleSubEnergy(new Vec2i(x, y)) > 0)
						{
							vessels.get(i).setModuleSize(new Vec2i(x, y), new Vec2f(vessels.get(i).getModuleSize(new Vec2i(x, y)).x * 3,
									vessels.get(i).getModuleSize(new Vec2i(x, y)).y * 3));
							spriteIsResized = true;
						}
						
						if (vessels.get(i).getModuleType(new Vec2i(x, y)) >= 0 && vessels.get(i).getModuleSprite(new Vec2i(x, y), false).isCollidedWithSprite(projectiles.get(p).getSprite(false), new Vec2f()))
						{
							vessels.get(i).setModuleEnergy(new Vec2i(x, y), vessels.get(i).getModuleEnergy(new Vec2i(x, y)) - getPower());
							vessels.get(i).setModuleIsTouched(new Vec2i(x, y));
							projectiles.remove(p);
							loopIsBroken = true;
						}
						
						if (spriteIsResized)
							vessels.get(i).setModuleSize(new Vec2i(x, y), new Vec2f(vessels.get(i).getModuleSize(new Vec2i(x, y)).x / 3,
								vessels.get(i).getModuleSize(new Vec2i(x, y)).y / 3));
						
						if (loopIsBroken)
							break loop;
					}
				}
			}
		}
		
		super.updateCollisions(vessels);
	}
	
	@Override
	public void draw(SpriteBatch display)
	{
		super.draw(display);
		
		cannonSprite.draw(display);
		for (int i = 0; i < projectiles.size(); ++i)
			projectiles.get(i).draw(display);
	}
	
	private Vessel getClosestVessel(Vector<Vessel> vessels, int faction, int[] factionsAgressivity)
	{
		Vessel closestVessel = null;
		float closestVesselDistance = Float.MAX_VALUE;
		
		for (int i = 0; i < vessels.size(); ++i)
		{
			float vesselDistance = getSpritePosition().getDistance(vessels.get(i).getPosition());
			
			if (vesselDistance < closestVesselDistance && !vessels.get(i).isDestroyed() && vessels.get(i).getFaction() != faction)
			{
				if (vessels.get(i).getFaction() != 0 || (vessels.get(i).getFaction() == 0 && factionsAgressivity[faction] >= 50))
				{
					closestVessel = vessels.get(i);
					closestVesselDistance = vesselDistance;
				}
			}
		}
		
		return closestVessel;
	}
	
	private void focusPoint(float lastFrameTime, Vec2f point)
	{
		Vec2f distancePoint = new Vec2f(cannonSprite.getPosition().x - point.x, cannonSprite.getPosition().y - point.y);		
		Vec2f sightVector = new Vec2f(-1, 0);
		sightVector.rotate(cannonSprite.getAngle());
		
		float angle = (float)Math.toDegrees(getAnglesDifference(distancePoint, sightVector)) - 90;
		
		if (angle > 1)
			cannonSprite.setAngle(cannonSprite.getAngle() + 140 * lastFrameTime);
		else if (angle < 1)
			cannonSprite.setAngle(cannonSprite.getAngle() - 140 * lastFrameTime);
	}
	
	private float getAnglesDifference(Vec2f angle1, Vec2f angle2)
	{
		float dot = angle1.x * angle2.x + angle1.y * angle2.y;
		float mag_v1 = (float)Math.sqrt( angle1.x * angle1.x + angle1.y * angle1.y );
		float mag_v2 = (float)Math.sqrt( angle2.x * angle2.x + angle2.y * angle2.y );
		
		float cosa = dot / (mag_v1 * mag_v2);
		
		return (float)Math.acos(cosa);
	}
}
