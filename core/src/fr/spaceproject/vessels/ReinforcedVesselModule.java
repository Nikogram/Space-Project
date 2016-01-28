package fr.spaceproject.vessels;

import java.util.Vector;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.spaceproject.utils.Explosion;
import fr.spaceproject.utils.Orientation;
import fr.spaceproject.utils.Sprite;
import fr.spaceproject.utils.TextureManager;
import fr.spaceproject.utils.Vec2f;
import fr.spaceproject.utils.Vec2i;

public class ReinforcedVesselModule extends VesselModule
{
	public ReinforcedVesselModule(VesselModuleType type, int level, Orientation orientation, TextureManager textureManager)
	{
		super(VesselModuleType.Reinforced, level, orientation, textureManager);
	}
	
	@Override
	public float getMaxEnergy()
	{
		return 200 + 50 * (getLevel() - 1);
	}
}
