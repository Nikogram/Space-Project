package fr.spaceproject.vessels;

import java.util.Vector;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.spaceproject.utils.Orientation;
import fr.spaceproject.utils.Sprite;
import fr.spaceproject.utils.Vec2f;
import fr.spaceproject.utils.Vec2i;

public class CannonVesselModule extends VesselModule
{
	protected Vector<Projectile> projectiles;
	protected float timeAfterShoot;
	
	public CannonVesselModule(int type, int level, Orientation orientation)
	{
		super(type, level, orientation);
		projectiles = new Vector<Projectile>();
		timeAfterShoot = 0;
	}
	
	@Override
	public String getTextureFileName()
	{
		return "CannonVesselModule.png";
	}
	
	public float getPower()
	{
		return 10 + 10f * (level - 1);
	}
	
	public float getTimeBeforeShoot()
	{
		return (float)Math.max(0.1, 0.4 - 0.02 * (level - 1));
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
			Vec2f projectilePosition = sprite.getRotatedPosition(new Vec2f(0, 0), sprite.angle - 180);
			projectiles.add(new Projectile(projectilePosition, new Vec2f(0, -getProjectileSpeed()), sprite.angle - 180, getProjectileLifeTime()));
			timeAfterShoot = 0;
		}
		
		for (int i = projectiles.size() - 1; i >= 0; --i)
		{
			projectiles.get(i).update(lastFrameTime);
			if (projectiles.get(i).timeBeforeDestruction <= 0)
			{
				projectiles.get(i).sound.dispose();
				projectiles.remove(i);
			}
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
