package fr.spaceproject.vessels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.spaceproject.utils.Sprite;
import fr.spaceproject.utils.TextureManager;
import fr.spaceproject.utils.Vec2f;

public class Projectile
{
	protected Sprite sprite;
	protected float timeBeforeDestruction;
	protected Sound sound;
	
	public Projectile(Vec2f position, Vec2f speed, float angle, float lifeTime, TextureManager textureManager)
	{
		sprite = new Sprite(position, new Vec2f(), textureManager.getTexture("ProjectileCannonVesselModule"));
		sprite.setAngle(angle);
		sprite.setSpeed(speed);
		timeBeforeDestruction = lifeTime;
		sound = Gdx.audio.newSound(Gdx.files.internal("ProjectileCannonVesselModule.wav"));
		sound.play();
	}
	
	public void finalize()
    {
		sound.dispose();
    }
	
	public void update(float lastFrameTime)
	{
		timeBeforeDestruction -= lastFrameTime;
		sprite.updateSpeed(lastFrameTime);
	}
	
	public void draw(SpriteBatch display)
	{
		sprite.draw(display);
	}
}
