package fr.spaceproject.utils;

public class Vec2f
{
	public float x;
	public float y;
	
	public Vec2f()
	{
		this.x = 0;
		this.y = 0;
	}
	
	public Vec2f(Vec2f vec)
	{
		this.x = vec.x;
		this.y = vec.y;
	}
	
	public Vec2f(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public float getLength()
	{
		return (float)Math.sqrt(x*x + y*y);
	}
	
	public Vec2f getRotatedVector(float angle)
	{
		Vec2f newVector = new Vec2f(this);
		newVector.rotate(angle);
		return newVector;
	}
	
	public void set(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void add(Vec2f vector, float angle)
	{
		x += vector.x * Math.cos(angle * Math.PI / 180) - vector.y * Math.sin(angle * Math.PI / 180);
		y += vector.x * Math.sin(angle * Math.PI / 180) + vector.y * Math.cos(angle * Math.PI / 180);
	}
	
	public Vec2f add(int xd,int yd)
	{
		Vec2f newVector = new Vec2f(this);
		newVector.x +=xd;
		newVector.y +=yd;
		return newVector;
	}
	
	public void normalize(float length)
	{
		if (getLength() != 0)
			set(x / getLength() * length, y / getLength() * length);
	}
	
	public void rotate(float angle)
	{
		Vec2f old = new Vec2f(x, y);
		
		x = (float)(old.x * Math.cos(angle * Math.PI / 180) - old.y * Math.sin(angle * Math.PI / 180));
		y = (float)(old.x * Math.sin(angle * Math.PI / 180) + old.y * Math.cos(angle * Math.PI / 180));
	}
}
