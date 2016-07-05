package fr.spaceproject.vessels.station;

import java.util.Vector;

import com.badlogic.gdx.graphics.Texture;

import fr.spaceproject.utils.Orientation;
import fr.spaceproject.utils.Sprite;
import fr.spaceproject.utils.TextureManager;
import fr.spaceproject.utils.Vec2f;
import fr.spaceproject.utils.Vec2i;
import fr.spaceproject.vessels.Vessel;
import fr.spaceproject.vessels.VesselAction;
import fr.spaceproject.vessels.VesselModule;
import fr.spaceproject.vessels.VesselModuleType;


public class StationModule extends VesselModule {
	public StationModule(VesselModuleType type, int level, Orientation orientation, TextureManager textureManager) {
		super(type, level, orientation, textureManager);
		sprite.setTexture(getTexture());
	}

	@Override
	public Texture getTexture() {
		if (type.equals(VesselModuleType.Broken))
			return textureManager.getTexture("BrokenStationModule");
		else if (type.equals(VesselModuleType.Cannon))
			return textureManager.getTexture("SimpleStationModule");
		else if (type.equals(VesselModuleType.Shield))
			return textureManager.getTexture("ShieldVesselModule");
		else // Simple
			return textureManager.getTexture("SimpleStationModule");
	}

	@Override
	public float getMaxEnergy() {
		return 5000 + 2500 * (level - 1);
	}

	public void update(float lastFrameTime, Sprite vesselSprite, Vec2i moduleRelativePosition, Vector<VesselAction> actions, Vector<Vessel> vessels, int faction, int[] factionsAgressivity) {
		sprite.setPosition(vesselSprite.getRotatedPosition(new Vec2f(140 * moduleRelativePosition.x, 140 * moduleRelativePosition.y), vesselSprite.getAngle()));
		sprite.setSpeed(vesselSprite.getSpeed());
		sprite.setAngle(vesselSprite.getAngle() + orientation.ordinal() * 90);
		sprite.updateVertices();
	}

	@Override
	public Sprite updateCollisions(float lastFrameTime, Vector<Vessel> vessels, Vessel moduleVessel, Station station, Vector<Vessel> shotVessels) {
		return new Sprite(new Vec2f(), new Vec2f(), getTexture());
	}
}
