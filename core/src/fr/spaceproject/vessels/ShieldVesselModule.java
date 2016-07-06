package fr.spaceproject.vessels;

import java.util.Vector;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.spaceproject.utils.Orientation;
import fr.spaceproject.utils.Sprite;
import fr.spaceproject.utils.TextureManager;
import fr.spaceproject.utils.Vec2f;
import fr.spaceproject.utils.Vec2i;


public class ShieldVesselModule extends VesselModule {
	private Sprite ballSprite;
	private float timeAfterShieldActivation;
	private boolean animationIsLaunched;
	private float timeBeforeRecharging;
	protected double cost = 1;


	public ShieldVesselModule(VesselModuleType type, int level, Orientation orientation, TextureManager textureManager) {
		super(type, level, orientation, textureManager);
		setSubEnergy(getMaxSubEnergy());
		ballSprite = new Sprite(new Vec2f(), new Vec2f(), textureManager.getTexture("ShieldBallVesselModule"));
		timeAfterShieldActivation = 0;
		animationIsLaunched = false;
		timeBeforeRecharging = 0;
	}

	public float getMaxSubEnergy() {
		return 200 + 50 * (getLevel() - 1);
	}

	public float getAnimationTime() {
		return 0.3f;
	}

	public float getTimeBeforeRecharging() {
		return 5.0f;
	}

	public float getRecharging() {
		return 5f;
	}

	@Override
	public void update(float lastFrameTime, Sprite vesselSprite, Vec2i moduleRelativePosition, Vector<VesselAction> actions) {
		super.update(lastFrameTime, vesselSprite, moduleRelativePosition, actions);
		ballSprite.setPosition(getSpritePosition());

		if (isTouched() || timeAfterShieldActivation > 0) {
			timeAfterShieldActivation += lastFrameTime;
			timeBeforeRecharging = getTimeBeforeRecharging();
		}
		else if (timeAfterShieldActivation <= 0)
			timeAfterShieldActivation = 0;

		if (timeAfterShieldActivation > getAnimationTime() && timeAfterShieldActivation < 2 * getAnimationTime() && isTouched()) {
			timeAfterShieldActivation = 2 * getAnimationTime() - timeAfterShieldActivation;
		}

		if (timeAfterShieldActivation < getAnimationTime())
			ballSprite.setAlpha(timeAfterShieldActivation / getAnimationTime());
		else if (timeAfterShieldActivation < 2 * getAnimationTime())
			ballSprite.setAlpha(1 - (timeAfterShieldActivation - getAnimationTime()) / getAnimationTime());
		else {
			ballSprite.setAlpha(0);
			timeAfterShieldActivation = 0;
		}

		ballSprite.setAlpha(ballSprite.getColor().a * getSubEnergy() / getMaxSubEnergy());

		if (timeBeforeRecharging <= 0)
			setSubEnergy(Math.min(getSubEnergy() + getRecharging() * lastFrameTime, getMaxSubEnergy()));
		else {
			timeBeforeRecharging -= lastFrameTime;
			if (getSubEnergy() < 0)
				setSubEnergy(0);
		}

		ballSprite.updateVertices();
	}

	@Override
	public void updateVertices() {
		super.updateVertices();
		ballSprite.updateVertices();
	}

	@Override
	public void drawForeground(SpriteBatch display) {
		if (getSubEnergy() > 0)
			ballSprite.draw(display);
	}
}
