package fr.spaceproject.vessels;

import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.spaceproject.utils.Orientation;
import fr.spaceproject.utils.Sprite;
import fr.spaceproject.utils.TextureManager;
import fr.spaceproject.utils.Vec2f;
import fr.spaceproject.utils.Vec2i;

public class LaserVesselModule extends VesselModule
{
	protected Sprite laserSprite;
	protected float timeAfterShoot;
	protected Sound laserSound;
	boolean laserIsAlreadyUpdate;
	
	public LaserVesselModule(int type, int level, Orientation orientation, TextureManager textureManager)
	{
		super(type, level, orientation, textureManager);
		laserSprite = new Sprite(new Vec2f(), new Vec2f(getLength(), 6), textureManager.getTexture("ProjectileLaserVesselModule"));
		laserSprite.setAlpha(0);
		timeAfterShoot = getTimeBeforeShoot();
		laserSound = Gdx.audio.newSound(Gdx.files.internal("LaserVesselModule.mp3"));
		laserIsAlreadyUpdate = false;
	}
	
	public void finalize()
	{
		laserSound.dispose();
	}
	
	public float getPower()
	{
		return 100 + 50f * (getLevel() - 1);
	}
	
	public float getLength()
	{
		return 10000;
	}
	
	public float getTimeBeforeShoot()
	{
		return (float)Math.max(1, 0.4 - 0.02 * (getLevel() - 1));
	}
	
	@Override
	public Vessel updateCollisions(Vector<Vessel> vessels, Vessel moduleVessel)
	{
		int vesselId = -1;
		Vec2i modulePosition = new Vec2i(-1, -1);
		
		if (!laserIsAlreadyUpdate)
		{
			// Recuperation de la position module le plus proche
			float moduleDistance = -1;
			float moduleSize = 0;
			
			Sprite tempLaserSprite = new Sprite(new Vec2f(), new Vec2f(getLength(), 6), getTexture());
			tempLaserSprite.setPosition(getSprite().getRotatedPosition(new Vec2f(0, -tempLaserSprite.getSize().x / 2 - getSprite().getSize().x / 2), getSprite().getAngle() - 180));
			tempLaserSprite.setAngle(getSprite().getAngle() - 180);
			
			for (int i = 0; i < vessels.size(); ++i)
			{
				if (vessels.get(i) != moduleVessel && vessels.get(i).getCenter().getDistance(laserSprite.getPosition()) < getLength())
				{
					for (int x = 0; x < vessels.get(i).modules.length; ++x)
					{
						for (int y = 0; y < vessels.get(i).modules[x].length; ++y)
						{
							if (vessels.get(i).modules[x][y].getType() == 5)
								vessels.get(i).modules[x][y].getSprite(false).setSize(new Vec2f(vessels.get(i).modules[x][y].getSprite(false).getSize().x * 3,
									vessels.get(i).modules[x][y].getSprite(false).getSize().y * 3));
							
							if (vessels.get(i).modules[x][y].getType() >= 0 && tempLaserSprite.isCollidedWithSprite(vessels.get(i).modules[x][y].getSprite(false), new Vec2f()))
							{
								float distance = getSpritePosition().getDistance(vessels.get(i).modules[x][y].getSpritePosition()) - getSpriteSize().x / 2 - vessels.get(i).modules[x][y].getSpriteSize().x / 2;
								
								if (moduleDistance == -1 || moduleDistance > distance)
								{
									moduleDistance = distance;
									moduleSize = vessels.get(i).modules[x][y].getSpriteSize().x;
									vesselId = i;
									modulePosition.set(x, y);
								}
							}
							
							if (vessels.get(i).modules[x][y].getType() == 5)
								vessels.get(i).modules[x][y].getSprite(false).setSize(new Vec2f(vessels.get(i).modules[x][y].getSprite(false).getSize().x / 3,
									vessels.get(i).modules[x][y].getSprite(false).getSize().y / 3));
						}
					}
				}
			}
			
			if (moduleDistance > 0)
			{
				laserSprite.setSize(new Vec2f(moduleDistance + 3, 6));
				laserSprite.setPosition(getSprite().getRotatedPosition(new Vec2f(0, -laserSprite.getSize().x / 2 - getSprite().getSize().x / 2), getSprite().getAngle() - 180));
				laserSprite.setAngle(getSprite().getAngle() - 180);
				
			}
			else
			{
				laserSprite.setSize(new Vec2f(getLength(), 6));
				laserSprite.setPosition(getSprite().getRotatedPosition(new Vec2f(0, -laserSprite.getSize().x / 2 - getSprite().getSize().x / 2), getSprite().getAngle() - 180));
				laserSprite.setAngle(getSprite().getAngle() - 180);
			}
			
			laserIsAlreadyUpdate = true;
		}
		
		
		// Gestion de la collision entre le laser et tous les vaisseaux
		if (vesselId >= 0)
			vessels.get(vesselId).modules[modulePosition.x][modulePosition.y].setEnergy(vessels.get(vesselId).modules[modulePosition.x][modulePosition.y].getEnergy() - getPower());
			
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
			laserIsAlreadyUpdate = false;
			timeAfterShoot = 0;
			laserSound.play();
			//laserSprite.setPosition(getSprite().getRotatedPosition(new Vec2f(0, -laserSprite.getSize().x / 2 - getSprite().getSize().x / 2), getSprite().getAngle() - 180));
			//laserSprite.setAngle(getSprite().getAngle() - 180);
		}
		
		float time = 0.05f;
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
