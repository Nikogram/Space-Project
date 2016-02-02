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
import fr.spaceproject.vessels.station.Station;

public class LaserVesselModule extends VesselModule
{
	protected Sprite laserSprite;
	protected float timeAfterShoot;
	protected Sound laserSound;
	boolean laserIsAlreadyUpdate;
	boolean laserIsOn;
	
	public LaserVesselModule(VesselModuleType type, int level, Orientation orientation, TextureManager textureManager)
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
		return 40 + 20f * (getLevel() - 1);
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
	public Sprite updateCollisions(float lastFrameTime, Vector<Vessel> vessels, Vessel moduleVessel, Station station, Vector<Vessel> shotVessels)
	{
		// Recuperation de la position module le plus proche
		int vesselId = -1;
		Vec2i modulePosition = new Vec2i(-1, -1);
		Vec2i stationModulePosition = new Vec2i(-1, -1);
		
		if (!laserIsAlreadyUpdate)
		{
			float moduleDistance = -1;
			
			Sprite tempLaserSprite = new Sprite(new Vec2f(), new Vec2f(getLength(), 6), getTexture());
			tempLaserSprite.setPosition(getSprite().getRotatedPosition(new Vec2f(0, -tempLaserSprite.getSize().x / 2 - getSprite().getSize().x / 2), getSprite().getAngle() - 180));
			tempLaserSprite.setAngle(getSprite().getAngle() - 180);
			tempLaserSprite.updateVertices();
			Vec2f vertex1 = tempLaserSprite.getVertex(0);
			Vec2f vertex2 = tempLaserSprite.getVertex(2);
			
			for (int x = 0; x < station.getSize().x; ++x)
			{
				for (int y = 0; y < station.getSize().y; ++y)
				{
					if (station.getModuleType(new Vec2i(x, y)).ordinal() > VesselModuleType.Broken.ordinal() && station.getModuleSprite(new Vec2i(x, y), false).isCollidedWithSegment(vertex1, vertex2))
					{
						float distance = getSpritePosition().getDistance(station.getModulePosition(new Vec2i(x, y))) - getSpriteSize().x / 2 - station.getModuleSize(new Vec2i(x, y)).x / 4;
						
						if (moduleDistance == -1 || moduleDistance > distance)
						{
							moduleDistance = distance;
							stationModulePosition.set(x, y);
						}
					}
				}
			}
			
			for (int i = 0; i < vessels.size(); ++i)
			{
				if (vessels.get(i) != moduleVessel)
				{
					Sprite tempSprite = new Sprite(vessels.get(i).getCenter(), new Vec2f(200, 200), getTexture());
					tempSprite.setAngle(vessels.get(i).getAngle());
					
					if (tempSprite.isCollidedWithSegment(vertex1, vertex2))
					for (int x = 0; x < vessels.get(i).getSize().x; ++x)
					{
						for (int y = 0; y < vessels.get(i).getSize().y; ++y)
						{
							boolean spriteIsResized = false;
							
							if (vessels.get(i).getModuleType(new Vec2i(x, y)).equals(VesselModuleType.Shield) && vessels.get(i).getModuleSubEnergy(new Vec2i(x, y)) > 0)
							{
								vessels.get(i).setModuleSize(new Vec2i(x, y), new Vec2f(vessels.get(i).getModuleSize(new Vec2i(x, y)).x * 3,
										vessels.get(i).getModuleSize(new Vec2i(x, y)).y * 3));
								vessels.get(i).updateModuleVertices(new Vec2i(x, y));
								spriteIsResized = true;
							}
							
							if (vessels.get(i).getModuleType(new Vec2i(x, y)).ordinal() > VesselModuleType.Broken.ordinal() && vessels.get(i).getModuleSprite(new Vec2i(x, y), false).isCollidedWithSegment(vertex1, vertex2))
							{
								float distance = getSpritePosition().getDistance(vessels.get(i).getModulePosition(new Vec2i(x, y))) - getSpriteSize().x / 2 - vessels.get(i).getModuleSize(new Vec2i(x, y)).x / 2;
								
								if (moduleDistance == -1 || moduleDistance > distance)
								{
									moduleDistance = distance;
									vesselId = i;
									modulePosition.set(x, y);
								}
							}
							
							if (spriteIsResized)
							{
								vessels.get(i).setModuleSize(new Vec2i(x, y), new Vec2f(vessels.get(i).getModuleSize(new Vec2i(x, y)).x / 3,
										vessels.get(i).getModuleSize(new Vec2i(x, y)).y / 3));
								vessels.get(i).updateModuleVertices(new Vec2i(x, y));
							}
						}
					}
				}
			}
			
			if (moduleDistance > 0 || stationModulePosition.x != -1)
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
		{
			vessels.get(vesselId).setModuleEnergy(modulePosition, vessels.get(vesselId).getModuleEnergy(modulePosition) - getPower());
			vessels.get(vesselId).setModuleIsTouched(new Vec2i(modulePosition.x, modulePosition.y));
			vessels.get(vesselId).addAttackingVessel(moduleVessel);
		}
		else if (stationModulePosition.x != -1)
		{
			station.setModuleEnergy(stationModulePosition, station.getModuleEnergy(stationModulePosition) - getPower());
			station.addAttackingVessel(moduleVessel);
		}
			
		return super.updateCollisions(lastFrameTime, vessels, moduleVessel, station, shotVessels);
	}
	
	@Override
	public void update(float lastFrameTime, Sprite vesselSprite, Vec2i moduleRelativePosition, Vector<VesselAction> actions)
	{
		super.update(lastFrameTime, vesselSprite, moduleRelativePosition, actions);
		timeAfterShoot += lastFrameTime;
		laserIsOn = false;
		
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
			laserIsOn = true;
		}
		
		laserSprite.setAlpha(laserIsOn ? 1 : 0);
		
		/*float time = 0.05f;
		if (timeAfterShoot > time * 2)
			laserSprite.setAlpha(0);
		else if (timeAfterShoot < time)
			laserSprite.setAlpha((1 / time) * timeAfterShoot);
		else
			laserSprite.setAlpha(1 - (1 / time) * (timeAfterShoot - time));*/
	}
	
	@Override
	public void draw(SpriteBatch display)
	{
		super.draw(display);
		laserSprite.draw(display);
	}
}
