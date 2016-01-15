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
	
	public void set(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
}
