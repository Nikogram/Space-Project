package fr.spaceproject.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Sprite
{
	public Vec2f position;
	public Vec2f size;
	public float angle;
	public Vec2f speed;
	public Vec2f acceleration;
	Texture texture;
	
	public Sprite(Vec2f position, Vec2f size, String textureFileName)
	{
		this.position = position;
		this.angle = 0;
		this.speed = new Vec2f();
		this.acceleration = new Vec2f();
		texture = new Texture(Gdx.files.internal(textureFileName));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		if (size.x == 0 && size.y == 0)
			this.size = new Vec2f(texture.getWidth(), texture.getHeight());
		else
			this.size = size;
	}
	
	public void draw(SpriteBatch display)
	{
		display.draw(new TextureRegion(texture), position.x - size.x / 2, position.y - size.y / 2, size.x / 2, size.y / 2, size.x, size.y, 1.f, 1.f, angle - 90, false);
	}
	
	public void updateSpeed(float lastFrameTime)
	{
		speed.set(speed.x + acceleration.x * lastFrameTime, speed.y + acceleration.y * lastFrameTime);
		move(new Vec2f(speed.x * lastFrameTime, speed.y * lastFrameTime));
	}
	
	public void move(Vec2f movement)
	{
		position.set(position.x + (float)Math.cos(angle * Math.PI / 180) * movement.x - (float)Math.sin(angle * Math.PI / 180) * movement.y,
			position.y + (float)Math.sin(angle * Math.PI / 180) * movement.x + (float)Math.cos(angle * Math.PI / 180) * movement.y);
	}
}
