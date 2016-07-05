package fr.spaceproject.vessels;

import fr.spaceproject.utils.Orientation;
import fr.spaceproject.utils.TextureManager;


public class ReinforcedVesselModule extends VesselModule {
	public ReinforcedVesselModule(VesselModuleType type, int level, Orientation orientation, TextureManager textureManager) {
		super(VesselModuleType.Reinforced, level, orientation, textureManager);
	}

	@Override
	public float getMaxEnergy() {
		return 200 + 50 * (getLevel() - 1);
	}
}
