package fr.spaceproject.vessels;

import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.spaceproject.utils.Orientation;
import fr.spaceproject.utils.Sprite;
import fr.spaceproject.utils.Vec2f;
import fr.spaceproject.utils.Vec2i;

public class LaserVesselModule extends VesselModule
{
	protected Sprite laserSprite;
	protected float timeAfterShoot;
	protected Sound laserSound;
	
	public LaserVesselModule(int type, int level, Orientation orientation)
	{
		super(type, level, orientation);
		laserSprite = new Sprite(new Vec2f(), new Vec2f(500, 6), "ProjectileLaserVesselModule.png");
		laserSprite.setAlpha(0);
		timeAfterShoot = getTimeBeforeShoot();
		laserSound = Gdx.audio.newSound(Gdx.files.internal("LaserVesselModule.mp3"));
	}
	
	public void finalize()
	{
		laserSound.dispose();
	}
	
	@Override
	public String getTextureFileName()
	{
		return "CannonVesselModule.png";
	}
	
	public float getPower()
	{
		return 1 + 1f * (getLevel() - 1);
	}
	
	public float getTimeBeforeShoot()
	{
		return (float)Math.max(1, 0.4 - 0.02 * (getLevel() - 1));
	}
	
	@Override
	public Vessel updateCollisions(Vector<Vessel> vessels, Vessel moduleVessel)
	{		
		for (int i = 0; i < vessels.size(); ++i)
		{
			for (int x = 0; x < vessels.get(i).modules.length && vessels.get(i) != moduleVessel; ++x)
			{
				for (int y = 0; y < vessels.get(i).modules[x].length; ++y)
				{
					//if (vessels.get(i).modules[x][y].type >= 0 && vessels.get(i).modules[x][y].sprite.isCollidedWithSprite(laserSprite, new Vec2f()))
					if (vessels.get(i).modules[x][y].getType() >= 0 && laserSprite.isCollidedWithSprite(vessels.get(i).modules[x][y].getSprite(), new Vec2f()))
						vessels.get(i).modules[x][y].setEnergy(vessels.get(i).modules[x][y].getEnergy() - getPower() * laserSprite.getColor().a);
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
			timeAfterShoot = 0;
			laserSound.play();
		}
		
		laserSprite.setPosition(getSprite().getRotatedPosition(new Vec2f(0, -laserSprite.getSize().x / 2 - getSprite().getSize().x / 2), getSprite().getAngle() - 180));
		laserSprite.setAngle(getSprite().getAngle() - 180);
		
		float time = 0.1f;
		if (timeAfterShoot > time * 2)
			laserSprite.setAlpha(0);
		else if (timeAfterShoot < time)
			laserSprite.setAlpha((1 / time) * timeAfterShoot);
		else
			laserSprite.setAlpha(1 - (1 / time) * (timeAfterShoot - time));
	}
	
	@Override
	public void draw(SpriteBatch display)
	{
		super.draw(display);
		laserSprite.draw(display);
	}
}
