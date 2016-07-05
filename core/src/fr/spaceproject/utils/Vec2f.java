package fr.spaceproject.utils;

public class Vec2f {
	public float x;
	public float y;


	public Vec2f() {
		this.x = 0;
		this.y = 0;
	}

	public Vec2f(Vec2f vec) {
		this.x = vec.x;
		this.y = vec.y;
	}

	public Vec2f(float x, float y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public Vec2f clone() {
		return new Vec2f(this);
	}

	public float getLength() {
		return (float) Math.sqrt(x * x + y * y);
	}

	public float getDot(Vec2f a) {
		return x * a.x + y * a.y;
	}

	public Vec2f getRotatedVector(float angle) {
		Vec2f newVector = new Vec2f(this);
		newVector.rotate(angle);
		return newVector;
	}

	public float getAnglesDifference(Vec2f vector) {
		if (getLength() * vector.getLength() != 0) {
			float dot = x * vector.x + y * vector.y;
			float cosa = dot / (getLength() * vector.getLength());

			return (float) Math.acos(cosa) - 90;
		}

		return 0;
	}

	public float getDistanceWithSegment(Vec2f A, Vec2f B) {
		// Droite d'�quation de la forme ax+by+c=0;
		float a = A.y - B.y;
		float b = B.x - A.x;
		float c = A.x * B.y - B.x * A.y;

		return Math.abs(a * x + b * y + c) / (float) Math.sqrt(a * a + b * b);
	}

	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void set(Vec2f vector) {
		this.x = vector.x;
		this.y = vector.y;
	}

	public void add(Vec2f vector, float angle) {
		if (angle == 0) {
			x += vector.x;
			y += vector.y;
		}
		else {
			double angleRad = Math.toRadians(angle);
			double cosa = Math.cos(angleRad);
			double sina = Math.sin(angleRad);


			x += vector.x * cosa - vector.y * sina;
			y += vector.x * sina + vector.y * cosa;
		}
	}

	public Vec2f getAdd(float xd, float yd) {
		Vec2f newVector = new Vec2f(this);
		newVector.x += xd;
		newVector.y += yd;
		return newVector;
	}

	public Vec2f getNegative() {
		return new Vec2f(-x, -y);
	}

	public float getDistance(Vec2f vector) {
		return getAdd(-vector.x, -vector.y).getLength();
	}

	public Vec2f getAdd(Vec2f movement) {
		Vec2f newVector = new Vec2f(this);
		newVector.x += movement.x;
		newVector.y += movement.y;
		return newVector;
	}

	public void normalize(float length) {
		if (getLength() != 0)
			set(x / getLength() * length, y / getLength() * length);
	}

	public Vec2f getNormalize(float length) {
		Vec2f vector = new Vec2f(this);
		if (getLength() != 0)
			vector.set(x / getLength() * length, y / getLength() * length);
		return vector;
	}

	public void rotate(float angle) {
		Vec2f old = new Vec2f(x, y);

		x = (float) (old.x * Math.cos(angle * Math.PI / 180) - old.y * Math.sin(angle * Math.PI / 180));
		y = (float) (old.x * Math.sin(angle * Math.PI / 180) + old.y * Math.cos(angle * Math.PI / 180));
	}
}
