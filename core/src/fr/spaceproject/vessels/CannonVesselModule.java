package fr.spaceproject.vessels;

import java.util.Vector;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.spaceproject.utils.Orientation;
import fr.spaceproject.utils.Sprite;
import fr.spaceproject.utils.TextureManager;
import fr.spaceproject.utils.Vec2f;
import fr.spaceproject.utils.Vec2i;
import fr.spaceproject.vessels.station.Station;

public class CannonVesselModule extends VesselModule
{
	protected Vector<Projectile> projectiles;
	protected float timeAfterShoot;
	
	public CannonVesselModule(VesselModuleType type, int level, Orientation orientation, TextureManager textureManager)
	{
		super(type, level, orientation, textureManager);
		projectiles = new Vector<Projectile>();
		timeAfterShoot = 0;
	}
	
	public float getPower()
	{
		return 10 + 10f * (getLevel() - 1);
	}
	
	public float getTimeBeforeShoot()
	{
		return (float)Math.max(0.1, 0.4 - 0.02 * (getLevel() - 1));
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
	public Sprite updateCollisions(Vector<Vessel> vessels, Vessel moduleVessel, Station station, Vector<Vessel> shotVessels)
	{
		for (int x = 0; x < station.getSize().x; ++x)
		{
			for (int y = 0; y < station.getSize().y; ++y)
			{
				for (int p = projectiles.size() - 1; p >= 0 && station.getModuleType(new Vec2i(x, y)).ordinal() > VesselModuleType.Broken.ordinal(); --p)
				{
					if (projectiles.get(p).getSpritePosition().getDistance(station.getModulePosition(new Vec2i(x, y))) < 100 &&
							projectiles.get(p).getSprite(false).isCollidedWithSprite(station.getModuleSprite(new Vec2i(x, y), false)))
					{
						station.setModuleEnergy(new Vec2i(x, y), station.getModuleEnergy(new Vec2i(x, y)) - getPower());
						station.addAttackingVessel(moduleVessel);
						projectiles.remove(p);
					}
				}
			}
		}
		
		for (int i = 0; i < vessels.size(); ++i)
		{
			for (int p = projectiles.size() - 1; p >= 0; --p)
			{
				if (projectiles.get(p).getSpritePosition().getDistance(vessels.get(i).getCenter()) < 100)
				{
					loop:
					for (int x = 0; x < vessels.get(i).getSize().x && vessels.get(i) != moduleVessel; ++x)
					for (int y = 0; y < vessels.get(i).getSize().y ; ++y)
					{
						boolean loopIsBroken = false;
						boolean spriteIsResized = false;
						
						if (vessels.get(i).getModuleType(new Vec2i(x, y)).equals(VesselModuleType.Shield) && vessels.get(i).getModuleSubEnergy(new Vec2i(x, y)) > 0)
						{
							vessels.get(i).setModuleSize(new Vec2i(x, y), new Vec2f(vessels.get(i).getModuleSize(new Vec2i(x, y)).x * 3,
									vessels.get(i).getModuleSize(new Vec2i(x, y)).y * 3));
							vessels.get(i).updateModuleVertices(new Vec2i(x, y));
							spriteIsResized = true;
						}
						
						if (vessels.get(i).getModuleType(new Vec2i(x, y)).ordinal() > VesselModuleType.Broken.ordinal() && vessels.get(i).getModuleSprite(new Vec2i(x, y), false).isCollidedWithSprite(projectiles.get(p).getSprite(false)))
						{
							vessels.get(i).setModuleEnergy(new Vec2i(x, y), vessels.get(i).getModuleEnergy(new Vec2i(x, y)) - getPower());
							vessels.get(i).setModuleIsTouched(new Vec2i(x, y));
							projectiles.remove(p);
							vessels.get(i).addAttackingVessel(moduleVessel);
							loopIsBroken = true;
						}
						
						if (spriteIsResized)
						{
							vessels.get(i).setModuleSize(new Vec2i(x, y), new Vec2f(vessels.get(i).getModuleSize(new Vec2i(x, y)).x / 3,
								vessels.get(i).getModuleSize(new Vec2i(x, y)).y / 3));
							vessels.get(i).updateModuleVertices(new Vec2i(x, y));
						}
						
						if (loopIsBroken)
							break loop;
					}
				}
			}
		}
		
		return super.updateCollisions(vessels, moduleVessel, station, shotVessels);
	}
	
	@Override
	public void update(float lastFrameTime, Sprite vesselSprite, Vec2i moduleRelativePosition, Vector<VesselAction> actions)
	{
		super.update(lastFrameTime, vesselSprite, moduleRelativePosition, actions);
		timeAfterShoot += lastFrameTime;
		
		boolean wantShoot = false;
		for (int i = 0; i < actions.size() && !wantShoot; ++i)
		{
			if (actions.get(i).equals(VesselAction.Shoot))
				wantShoot = true;
		}

		if (timeAfterShoot >= getTimeBeforeShoot() && wantShoot)
		{
			Vec2f projectilePosition = getSprite().getRotatedPosition(new Vec2f(0, 0), getSprite().getAngle() - 180);
			projectiles.add(new Projectile(projectilePosition, new Vec2f(0, -getProjectileSpeed()), getSprite().getAngle() - 180, getProjectileLifeTime(), getTextureManager()));
			projectiles.get(projectiles.size() - 1).getSprite(false).setColor(new Color(0, 1, 0, 1));
			timeAfterShoot = 0;
		}
		
		for (int i = projectiles.size() - 1; i >= 0; --i)
		{
			projectiles.get(i).update(lastFrameTime);
			if (projectiles.get(i).getTimeBeforeDestruction() <= 0)
				projectiles.remove(i);
		}
	}
	
	@Override
	public void draw(SpriteBatch display)
	{
		super.draw(display);
		
		for (int i = 0; i < projectiles.size(); ++i)
			projectiles.get(i).draw(display);
	}
}
