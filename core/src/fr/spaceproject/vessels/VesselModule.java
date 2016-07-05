package fr.spaceproject.vessels;

import java.util.Vector;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.spaceproject.utils.Orientation;
import fr.spaceproject.utils.Sprite;
import fr.spaceproject.utils.TextureManager;
import fr.spaceproject.utils.Vec2f;
import fr.spaceproject.utils.Vec2i;
import fr.spaceproject.vessels.station.Station;


public class VesselModule {
	protected VesselModuleType type;
	protected int level;
	protected float energy;
	protected float subEnergy;
	protected Sprite sprite;
	protected Orientation orientation;
	protected TextureManager textureManager;
	protected boolean isEngine;
	protected boolean isTouched;


	public VesselModule(VesselModuleType type, int level, Orientation orientation, TextureManager textureManager) {
		this.isEngine = type.equals(VesselModuleType.Engine);
		this.textureManager = textureManager;
		this.type = type;
		this.level = level;
		energy = getMaxEnergy();
		subEnergy = 0;
		sprite = new Sprite(new Vec2f(), new Vec2f(), getTexture());
		sprite.setAngle(orientation.ordinal() * 90);
		this.orientation = orientation;
		isTouched = false;
	}

	public VesselModule(VesselModuleType type, int level, Orientation orientation, TextureManager textureManager, boolean isEngine) {
		this.isEngine = type.equals(VesselModuleType.Engine) || isEngine;
		this.textureManager = textureManager;
		this.type = type;
		this.level = level;
		energy = getMaxEnergy();
		sprite = new Sprite(new Vec2f(), new Vec2f(), getTexture());
		sprite.setAngle(orientation.ordinal() * 90);
		this.orientation = orientation;
	}

	public Texture getTexture() {
		// -2 : inexistant
		if (type.equals(VesselModuleType.Cockpit))
			return textureManager.getTexture("CockpitVesselModule");
		else if (type.equals(VesselModuleType.Engine))
			return textureManager.getTexture("EngineVesselModule");
		else if (type.equals(VesselModuleType.Cannon))
			return textureManager.getTexture("CannonVesselModule");
		else if (type.equals(VesselModuleType.Laser))
			return textureManager.getTexture("CannonVesselModule");
		else if (type.equals(VesselModuleType.Shield))
			return textureManager.getTexture("ShieldVesselModule");
		else if (type.equals(VesselModuleType.Broken) && !isEngine)
			return textureManager.getTexture("BrokenVesselModule");
		else if (type.equals(VesselModuleType.Broken))
			return textureManager.getTexture("BrokenEngineVesselModule");
		else if (type.equals(VesselModuleType.Reinforced))
			return textureManager.getTexture("ReinforcedVesselModule");
		else // Simple
			return textureManager.getTexture("SimpleVesselModule");
	}

	public TextureManager getTextureManager() {
		return textureManager;
	}

	public VesselModuleType getType() {
		return type;
	}

	public void setType(VesselModuleType type) {
		this.type = type;
	}

	public int getLevel() {
		return level;
	}

	public float getEnergy() {
		if (subEnergy <= 0)
			return energy;
		return subEnergy;
	}

	public void setEnergy(float energy) {
		if (subEnergy <= 0)
			this.energy = energy;
		else
			subEnergy = energy;
	}

	public float getEnergy(boolean forceNormalEnergy) {
		if (forceNormalEnergy)
			return energy;
		return getEnergy();
	}

	public void setEnergy(float energy, boolean forceNormalEnergy) {
		if (forceNormalEnergy)
			this.energy = energy;
		else
			setEnergy(energy);
	}

	public float getSubEnergy() {
		return subEnergy;
	}

	public void setSubEnergy(float energy) {
		subEnergy = energy;
	}

	public Sprite getSprite() {
		return sprite.clone();
	}

	public Sprite getSprite(boolean copy) {
		if (copy)
			return sprite;
		return sprite;
	}

	public Vec2f getSpritePosition() {
		return sprite.getPosition();
	}

	public Vec2f getSpriteOldPosition() {
		return sprite.getOldPosition();
	}

	public void setSpritePosition(Vec2f position) {
		sprite.setPosition(position);
	}

	public void moveSprite(Vec2f movement) {
		sprite.move(movement);
	}

	public Vec2f getSpriteSize() {
		return sprite.getSize();
	}

	public void setSpriteSize(Vec2f size) {
		sprite.setSize(size);
	}

	public float getSpriteAngle() {
		return sprite.getAngle();
	}

	public void setSpriteAngle(float angle) {
		sprite.setAngle(angle);
	}

	public Vec2f getSpriteSpeed() {
		return sprite.getSpeed();
	}

	public void setSpriteSpeed(Vec2f speed) {
		sprite.setSpeed(speed);
	}

	public Vec2f getSpriteAcceleration() {
		return sprite.getAcceleration();
	}

	public void setSpriteAcceleration(Vec2f acceleration) {
		sprite.setAcceleration(acceleration);
	}

	public void updateSpriteSpeed(float lastFrameTime, boolean angleIsTakenAccount) {
		sprite.updateSpeed(lastFrameTime, angleIsTakenAccount);
	}

	public Orientation getOrientation() {
		return Orientation.valueOf(orientation.toString());
	}

	public float getMaxEnergy() {
		return 100 + 20 * (level - 1);
	}

	public boolean isTouched() {
		return isTouched;
	}

	public void setIsTouched() {
		isTouched = true;
	}

	public Sprite updateCollisions(float lastFrameTime, Vector<Vessel> vessels, Vessel moduleVessel, Station station, Vector<Vessel> shotVessels) {
		isTouched = false;

		float damagePerFrame = 5;

		for (int x = 0; x < station.getSize().x; ++x) {
			for (int y = 0; y < station.getSize().y; ++y) {
				if (sprite.getPosition().getDistance(station.getModulePosition(new Vec2i(x, y))) < 140 &&
						type.ordinal() > VesselModuleType.Broken.ordinal() && station.getModuleType(new Vec2i(x, y)).ordinal() > VesselModuleType.Broken.ordinal()
						&& sprite.isCollidedWithSprite(station.getModuleSprite(new Vec2i(x, y), false))) {
					energy -= lastFrameTime * 60 * damagePerFrame;
					station.setModuleEnergy(new Vec2i(x, y), station.getModuleEnergy(new Vec2i(x, y)) - lastFrameTime * 60 * damagePerFrame);
					station.addAttackingVessel(moduleVessel);
					return station.getModuleSprite(new Vec2i(x, y));
				}
			}
		}

		for (int i = 0; i < vessels.size(); ++i) {
			if (sprite.getPosition().getDistance(vessels.get(i).getCenter()) < 100)
				for (int x = 0; x < vessels.get(i).getSize().x && vessels.get(i) != moduleVessel; ++x) {
					for (int y = 0; y < vessels.get(i).getSize().y; ++y) {
						if (type.ordinal() > VesselModuleType.Broken.ordinal() && vessels.get(i).getModuleType(new Vec2i(x, y)).ordinal() > VesselModuleType.Broken.ordinal()
								&& sprite.isCollidedWithSprite(vessels.get(i).getModuleSprite(new Vec2i(x, y), false))) {
							vessels.get(i).setModuleEnergy(new Vec2i(x, y), vessels.get(i).getModuleEnergy(new Vec2i(x, y), true) - lastFrameTime * 60 * damagePerFrame, true);
							vessels.get(i).addAttackingVessel(moduleVessel);
							return vessels.get(i).getModuleSprite(new Vec2i(x, y));
						}
					}
				}
		}

		return null;
	}

	public void update(float lastFrameTime, Sprite vesselSprite, Vec2i moduleRelativePosition, Vector<VesselAction> actions) {
		sprite.setPosition(vesselSprite.getRotatedPosition(new Vec2f(20 * moduleRelativePosition.x, 20 * moduleRelativePosition.y), vesselSprite.getAngle()));
		sprite.setSpeed(vesselSprite.getSpeed());
		sprite.setAngle(vesselSprite.getAngle() + orientation.ordinal() * 90);
		sprite.updateVertices();
	}

	public void updateVertices() {
		sprite.updateVertices();
	}

	public void draw(SpriteBatch display) {
		if (type.ordinal() >= VesselModuleType.Broken.ordinal())
			sprite.draw(display);
	}

	public void drawForeground(SpriteBatch display) {
	}
}
