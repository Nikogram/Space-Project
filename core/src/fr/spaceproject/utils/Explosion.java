package fr.spaceproject.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class Explosion {
	private TextureManager textureManager;
	private float timeAfterExplosion;
	private Sprite explosionSprite;
	private Sound explosionSound;
	private boolean isSmall;


	public Explosion(Vec2f position, boolean isSmall, TextureManager textureManager) {
		this.textureManager = textureManager;
		timeAfterExplosion = 0.000001f;
		explosionSound = Gdx.audio.newSound(Gdx.files.internal("Explosion.mp3"));
		explosionSound.play();
		explosionSprite = new Sprite(position, (isSmall ? new Vec2f(20, 20) : new Vec2f(140, 140)), textureManager.getTexture("StationExplosion1"));
		this.isSmall = isSmall;
	}

	public void finalize() {
		explosionSound.dispose();
	}

	public float getExplosionTime() {
		return 0.3f;
	}

	public float getDissipationFactor() {
		return 3;
	}

	public void setPosition(Vec2f position) {
		explosionSprite.setPosition(position);
	}

	public boolean isFinished() {
		return timeAfterExplosion >= getDissipationFactor() * getExplosionTime();
	}

	public void update(float lastFrameTime) {
		if (timeAfterExplosion < getExplosionTime()) {
			int id = (int) (timeAfterExplosion / getExplosionTime() * 4) + 1;
			explosionSprite.setTexture(textureManager.getTexture("StationExplosion" + id));

			timeAfterExplosion += lastFrameTime;
		}
		else if (timeAfterExplosion < getDissipationFactor() * getExplosionTime()) {
			float id = 1 - (timeAfterExplosion / getDissipationFactor() / getExplosionTime());
			explosionSprite.setAlpha(id);

			timeAfterExplosion += lastFrameTime;
		}
	}

	public void draw(SpriteBatch display) {
		if (timeAfterExplosion < getDissipationFactor() * getExplosionTime())
			explosionSprite.draw(display);
	}
}
