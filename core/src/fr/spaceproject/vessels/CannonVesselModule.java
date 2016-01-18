package fr.spaceproject.vessels;

import java.util.Vector;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.spaceproject.utils.Orientation;
import fr.spaceproject.utils.Sprite;
import fr.spaceproject.utils.TextureManager;
import fr.spaceproject.utils.Vec2f;
import fr.spaceproject.utils.Vec2i;

public class CannonVesselModule extends VesselModule
{
	protected Vector<Projectile> projectiles;
	protected float timeAfterShoot;
	
	public CannonVesselModule(int type, int level, Orientation orientation, TextureManager textureManager)
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
		return 1;
	}
	
	public float getProjectileSpeed()
	{
		return 600;
	}
	
	@Override
	public Vessel updateCollisions(Vector<Vessel> vessels, Vessel moduleVessel)
	{		
		for (int i = 0; i < vessels.size(); ++i)
		{
			for (int p = projectiles.size() - 1; p >= 0; --p)
			{
				if (projectiles.get(p).sprite.getPosition().getDistance(vessels.get(i).getCenter()) < 100)
				loop:
				for (int x = 0; x < vessels.get(i).modules.length && vessels.get(i) != moduleVessel; ++x)
				for (int y = 0; y < vessels.get(i).modules[x].length; ++y)
				{
					if (vessels.get(i).modules[x][y].getType() >= 0 && vessels.get(i).modules[x][y].getSprite().isCollidedWithSprite(projectiles.get(p).sprite, new Vec2f()))
					{
						vessels.get(i).modules[x][y].setEnergy(vessels.get(i).modules[x][y].getEnergy() - getPower());
						projectiles.remove(p);
						break loop;
					}
				}
			}
		}
		
		return super.updateCollisions(vessels, moduleVessel);
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
			timeAfterShoot = 0;
		}
		
		for (int i = projectiles.size() - 1; i >= 0; --i)
		{
			projectiles.get(i).update(lastFrameTime);
			if (projectiles.get(i).timeBeforeDestruction <= 0)
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
