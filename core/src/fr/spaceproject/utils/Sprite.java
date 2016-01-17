package fr.spaceproject.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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
	public Color color;
	
	public Sprite(Vec2f position, Vec2f size, String textureFileName)
	{
		this.position = position;
		this.angle = 0;
		this.speed = new Vec2f();
		this.acceleration = new Vec2f();
		texture = new Texture(Gdx.files.internal(textureFileName));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		color = new Color(1, 1, 1, 1);
		
		if (size.x == 0 && size.y == 0)
			this.size = new Vec2f(texture.getHeight(), texture.getWidth());
		else
			this.size = size;
	}
	
	public Vec2f getRotatedPosition(Vec2f relativePositionToRotate, float rotationAngle)
	{
		Vec2f rotatedVertex = new Vec2f((float)(relativePositionToRotate.x * Math.cos(rotationAngle * Math.PI / 180) - relativePositionToRotate.y * Math.sin(rotationAngle * Math.PI / 180)),
				(float)(relativePositionToRotate.x * Math.sin(rotationAngle * Math.PI / 180) + relativePositionToRotate.y * Math.cos(rotationAngle * Math.PI / 180)));
		rotatedVertex.set(rotatedVertex.x + position.x, rotatedVertex.y + position.y);
		
		return rotatedVertex;
	}
	
	public boolean isCollidedWithSprite(Sprite sprite, Vec2f intersectionPoint)
	{
		if (new Vec2f(position.x - sprite.position.x, position.y - sprite.position.y).getLength() <=
				new Vec2f(size.x / 2 + sprite.size.x / 2, size.y / 2 + sprite.size.y / 2).getLength())	// Test simplifié
		{
			Vec2f spriteVertices[] = {new Vec2f(sprite.position.x + -sprite.size.x / 2, sprite.position.y + sprite.size.y / 2),
					new Vec2f(sprite.position.x + sprite.size.x / 2, sprite.position.y + sprite.size.y / 2),
					new Vec2f(sprite.position.x + sprite.size.x / 2, sprite.position.y + -sprite.size.y / 2),
					new Vec2f(sprite.position.x + -sprite.size.x / 2, sprite.position.y + -sprite.size.y / 2)};
			
			for (int i = 0; i < 4; ++i)
			{
				if (isCollidedWithSegment(spriteVertices[i], spriteVertices[i + 1 > 3 ? 0 : i + 1], intersectionPoint))
					return true;
			}
		}
		
		return false;
	}
	
	public boolean isCollidedWithSegment(Vec2f A, Vec2f B, Vec2f intersectionPoint)
	{
		for (int i = 0; i < 4; ++i)
		{
			if (borderIsCollidedWithSegment(A, B, i, intersectionPoint))
				return true;
		}
		
		return false;
	}
	
	public boolean borderIsCollidedWithSegment(Vec2f A, Vec2f B, int borderId, Vec2f intersectionPoint)
	{
		// A et B : les deux points du premier segment
		// C et D : les deux points d'un des segments du sprite
		Vec2f C;
		Vec2f D;
		
		if (borderId == 0)
		{
			C = getRotatedPosition(new Vec2f(-size.x / 2, size.y / 2), angle);
			D = getRotatedPosition(new Vec2f(size.x / 2, size.y / 2), angle);
		}
		else if (borderId == 1)
		{
			C = getRotatedPosition(new Vec2f(size.x / 2, size.y / 2), angle);
			D = getRotatedPosition(new Vec2f(size.x / 2, -size.y / 2), angle);
		}
		else if (borderId == 2)
		{
			C = getRotatedPosition(new Vec2f(size.x / 2, -size.y / 2), angle);
			D = getRotatedPosition(new Vec2f(-size.x / 2, -size.y / 2), angle);
		}
		else if (borderId == 3)
		{
			C = getRotatedPosition(new Vec2f(-size.x / 2, -size.y / 2), angle);
			D = getRotatedPosition(new Vec2f(-size.x / 2, size.y / 2), angle);
		}
		else
			return false;
		
		
		// Soit P le point d'intersection
		// On a P = A + k*AB et P = C + m*CD, donc A + k*AB = C + m*CD
		// Après décomposition on a k et m
		
		Vec2f AB = new Vec2f(B.x - A.x, B.y - A.y);
		Vec2f CD = new Vec2f(D.x - C.x, D.y - C.y);
		
		float denominator = AB.x * CD.y - AB.y * CD.x;
		if (denominator == 0)
			return borderIsCollidedWithSegment(new Vec2f(A.x + 0.001f, A.y + 0.001f), B, borderId, intersectionPoint);	// on recommence avec un petit décalage pour ne plus avoir de segments paralleles
		
		float k = -(A.x * CD.y - C.x * CD.y - CD.x * A.y + CD.x * C.y) / denominator;
		float m = -(-AB.x * A.y + AB.x * C.y + AB.y * A.x - AB.y * C.x) / denominator;

		if (k <= 0 || k >= 1 || m <= 0 || m >= 1)
			return false;

		intersectionPoint = new Vec2f(A.x + k * AB.x, A.y + k * AB.y);
		return true;
	}
	
	public void draw(SpriteBatch display)
	{
		Color c = display.getColor();
		display.setColor(color.r, color.g, color.b, color.a);
		display.draw(new TextureRegion(texture), position.x - size.x / 2, position.y - size.y / 2, size.x / 2, size.y / 2, size.x, size.y, 1.f, 1.f, angle - 90, false);
		display.setColor(c.r, c.g, c.b, c.a);
	}
	
	public void updateSpeed(float lastFrameTime)
	{
		speed.set(speed.x + acceleration.x * lastFrameTime, speed.y + acceleration.y * lastFrameTime);
		move(new Vec2f(speed.x * lastFrameTime, speed.y * lastFrameTime));
	}
	
	public void updateSpeed(float lastFrameTime, boolean rotationIsTakenAccount)
	{
		if (rotationIsTakenAccount)
			updateSpeed(lastFrameTime);
		else
		{
			speed.set(speed.x + acceleration.x * lastFrameTime, speed.y + acceleration.y * lastFrameTime);
			position.set(position.x + speed.x * lastFrameTime, position.y + speed.y * lastFrameTime);
		}
	}
	
	public void move(Vec2f movement)
	{
		position.set(position.x + (float)Math.cos(angle * Math.PI / 180) * movement.x - (float)Math.sin(angle * Math.PI / 180) * movement.y,
			position.y + (float)Math.sin(angle * Math.PI / 180) * movement.x + (float)Math.cos(angle * Math.PI / 180) * movement.y);
	}
}
