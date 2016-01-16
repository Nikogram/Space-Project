package fr.spaceproject.utils;

public class Vec2i
{
	public int x;
	public int y;
	
	public Vec2i()
	{
		this.x = 0;
		this.y = 0;
	}
	
	public Vec2i(Vec2i vec)
	{
		this.x = vec.x;
		this.y = vec.y;
	}
	
	public Vec2i(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public float getLength()
	{
		return (float)Math.sqrt(x*x + y*y);
	}
	
	public void set(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
}
