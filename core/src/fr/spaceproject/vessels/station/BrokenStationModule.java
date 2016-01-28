package fr.spaceproject.vessels.station;

import java.util.Vector;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.spaceproject.utils.Explosion;
import fr.spaceproject.utils.Orientation;
import fr.spaceproject.utils.Sprite;
import fr.spaceproject.utils.TextureManager;
import fr.spaceproject.utils.Vec2f;
import fr.spaceproject.utils.Vec2i;
import fr.spaceproject.vessels.Vessel;
import fr.spaceproject.vessels.VesselAction;
import fr.spaceproject.vessels.VesselModuleType;

public class BrokenStationModule extends StationModule
{
	private Explosion explosion;
	
	public BrokenStationModule(int level, Vec2f position, Orientation orientation, TextureManager textureManager)
	{
		super(VesselModuleType.Broken, level, orientation, textureManager);
		explosion = new Explosion(position, false, textureManager);
	}

	@Override
	public void update(float lastFrameTime, Sprite vesselSprite, Vec2i moduleRelativePosition, Vector<VesselAction> actions, Vector<Vessel> vessels, int faction, int[] factionsAgressivity)
	{
		super.update(lastFrameTime, vesselSprite, moduleRelativePosition, actions, vessels, faction, factionsAgressivity);
		explosion.update(lastFrameTime);
	}
	
	@Override
	public void draw(SpriteBatch display)
	{
		super.draw(display);
		
		if (!explosion.isFinished())
			explosion.draw(display);
	}
}
