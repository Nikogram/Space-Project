package fr.spaceproject.vessels;

import fr.spaceproject.utils.Sprite;

public class EngineVesselModule extends VesselModule
{
	protected Sprite flamesSprite;
	
	public EngineVesselModule(int type, int level)
	{
		super(type, level);
	}
	
	public EngineVesselModule(int type, int level, float energy)
	{
		super(type, level, energy);
	}
}
