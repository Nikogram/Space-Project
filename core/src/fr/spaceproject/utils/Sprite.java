package fr.spaceproject.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Sprite
{
	private Vec2f position;
	private Vec2f size;
	private float angle;
	private Vec2f speed;
	private Vec2f acceleration;
	private Texture texture;
	private Color color;
	private Vec2f oldPosition;
	
	public Sprite(Vec2f position, Vec2f size, Texture texture)
	{
		this.position = position.clone();
		this.angle = 0;
		this.speed = new Vec2f();
		this.acceleration = new Vec2f();
		this.texture = texture;
		color = new Color(1, 1, 1, 1);
		oldPosition = this.position;
		
		if (size.x == 0 && size.y == 0)
			this.size = new Vec2f(texture.getHeight(), texture.getWidth());
		else
			this.size = size.clone();
	}
	
	@Override
	public Sprite clone()
	{
		Sprite sprite = new Sprite(position, size, texture);
		sprite.angle = angle;
		sprite.speed = speed.clone();
		sprite.acceleration = acceleration.clone();
		sprite.color = new Color(color);
		sprite.oldPosition = oldPosition.clone();
		
		return sprite;
	}
	
	public void finalize()
    {
		texture.dispose();
    }
	
	public Vec2f getPosition()
	{
		return position.clone();
	}
	
	public void setPosition(Vec2f position)
	{
		this.position = position.clone();
	}
	
	public Vec2f getSize()
	{
		return size.clone();
	}
	
	public void setSize(Vec2f size)
	{
		this.size = size.clone();
	}
	
	public float getAngle()
	{
		return angle;
	}
	
	public void setAngle(float angle)
	{
		this.angle = angle;
	}
	
	public Vec2f getSpeed()
	{
		return speed.clone();
	}
	
	public void setSpeed(Vec2f speed)
	{
		this.speed = speed.clone();
	}
	
	public Vec2f getAcceleration()
	{
		return acceleration.clone();
	}
	
	public void setAcceleration(Vec2f acceleration)
	{
		this.acceleration = acceleration.clone();
	}
	
	public void setTexture(Texture texture)
	{
		this.texture = texture;
	}
	
	public Color getColor()
	{
		return new Color(color);
	}
	
	public void setColor(Color color)
	{
		this.color = new Color(color);
	}
	
	public void setAlpha(float alpha)
	{
		color.a = alpha;
	}
	
	public Vec2f getOldPosition()
	{
		return position.clone();
	}
	
	public void setOldPosition(Vec2f oldPosition)
	{
		this.oldPosition = oldPosition.clone();
	}
	
	public Vec2f getRotatedPosition(Vec2f relativePositionToRotate, float rotationAngle)
	{
		double angle = Math.toRadians(rotationAngle);
		double cosa = Math.cos(angle);
		double sina = Math.sin(angle);
		
		return new Vec2f(position.x + (float)(relativePositionToRotate.x * cosa - relativePositionToRotate.y * sina),
				position.y + (float)(relativePositionToRotate.x * sina + relativePositionToRotate.y * cosa));	
	}
	
	public boolean isCollidedWithSprite(Sprite sprite, Vec2f intersectionPoint)
	{
		if (new Vec2f(position.x - sprite.position.x, position.y - sprite.position.y).getLength() <=
				new Vec2f(size.x + sprite.size.x, size.y + sprite.size.y).getLength())	// Test simplifi�
		{
			Vec2f spriteVertices[] = {new Vec2f(-sprite.size.x / 2, -sprite.size.y / 2),
					new Vec2f(sprite.size.x / 2, -sprite.size.y / 2),
					new Vec2f(sprite.size.x / 2, +sprite.size.y / 2),
					new Vec2f(-sprite.size.x / 2, +sprite.size.y / 2)};
			
			for (int i = 0; i < 4; ++i)
				spriteVertices[i] = sprite.getRotatedPosition(spriteVertices[i], sprite.angle - 90);
			
			for (int i = 0; i < 4; ++i)
			{
				if (isCollidedWithSegment(spriteVertices[i], spriteVertices[i + 1 > 3 ? 0 : i + 1], intersectionPoint))
					return true;
			}
			
			if (sprite.position.x != sprite.oldPosition.x || sprite.position.y != sprite.oldPosition.y)
			{
				Vec2f spritePosition = new Vec2f(sprite.position);
				sprite.position = new Vec2f(sprite.oldPosition);
				
				Vec2f spriteOldVertices[] = {new Vec2f(-sprite.size.x / 2, sprite.size.y / 2),
						new Vec2f(sprite.size.x / 2, sprite.size.y / 2),
						new Vec2f(sprite.size.x / 2, -sprite.size.y / 2),
						new Vec2f(-sprite.size.x / 2, -sprite.size.y / 2)};
				
				for (int i = 0; i < 4; ++i)
					spriteOldVertices[i] = sprite.getRotatedPosition(spriteOldVertices[i], sprite.angle);
				
				sprite.position = new Vec2f(spritePosition);
				
				for (int i = 0; i < 4; ++i)
				{
					if (isCollidedWithSegment(spriteVertices[i], spriteOldVertices[i], intersectionPoint))
						return true;
				}
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
			C = getRotatedPosition(new Vec2f(-size.x / 2, -size.y / 2), (angle - 90));
			D = getRotatedPosition(new Vec2f(size.x / 2, -size.y / 2), (angle - 90));
		}
		else if (borderId == 1)
		{
			C = getRotatedPosition(new Vec2f(size.x / 2, -size.y / 2), (angle - 90));
			D = getRotatedPosition(new Vec2f(size.x / 2, +size.y / 2), (angle - 90));
		}
		else if (borderId == 2)
		{
			C = getRotatedPosition(new Vec2f(size.x / 2, +size.y / 2), (angle - 90));
			D = getRotatedPosition(new Vec2f(-size.x / 2, +size.y / 2), (angle - 90));
		}
		else if (borderId == 3)
		{
			C = getRotatedPosition(new Vec2f(-size.x / 2, +size.y / 2), (angle - 90));
			D = getRotatedPosition(new Vec2f(-size.x / 2, -size.y / 2), (angle - 90));
		}
		else
			return false;
		
		
		// Soit P le point d'intersection
		// On a P = A + k*AB et P = C + m*CD, donc A + k*AB = C + m*CD
		// Apr�s d�composition on a k et m
		
		Vec2f AB = new Vec2f(B.x - A.x, B.y - A.y);
		Vec2f CD = new Vec2f(D.x - C.x, D.y - C.y);
		
		float denominator = AB.x * CD.y - AB.y * CD.x;
		if (denominator == 0)
			return borderIsCollidedWithSegment(new Vec2f(A.x + 0.001f, A.y + 0.001f), B, borderId, intersectionPoint);	// on recommence avec un petit d�calage pour ne plus avoir de segments paralleles
		
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
		oldPosition = new Vec2f(position);
		speed.set(speed.x + acceleration.x * lastFrameTime, speed.y + acceleration.y * lastFrameTime);
		move(new Vec2f(speed.x * lastFrameTime, speed.y * lastFrameTime));
	}
	
	public void updateSpeed(float lastFrameTime, boolean rotationIsTakenAccount)
	{
		oldPosition = new Vec2f(position);
		
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
