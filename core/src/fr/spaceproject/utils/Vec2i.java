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
	
	@Override
	 public boolean equals(Object vector){
	  Vec2i obj = (Vec2i)(vector);
	  return x == obj.x && y == obj.y;
	 }
	
	public void set(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Vec2i getAdd(int nx,int ny){
		x += nx;
		y += ny;
		return new Vec2i(x,y);
	}
	
	
	public void add(Vec2f vector, float angle)
	{
		x += vector.x * Math.cos(angle * Math.PI / 180) - vector.y * Math.sin(angle * Math.PI / 180);
		y += vector.x * Math.sin(angle * Math.PI / 180) + vector.y * Math.cos(angle * Math.PI / 180);
	}
	
	public void normalize(float length)
	{
		if (getLength() != 0)
			set((int)(x / getLength() * 100), (int)(y / getLength() * 100));
	}
	
	public void rotate(float angle)
	{
		Vec2f old = new Vec2f(x, y);
		
		x = (int)(old.x * Math.cos(angle * Math.PI / 180) - old.y * Math.sin(angle * Math.PI / 180));
		y = (int)(old.x * Math.sin(angle * Math.PI / 180) + old.y * Math.cos(angle * Math.PI / 180));
	}
}
