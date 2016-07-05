package fr.spaceproject.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class Sprite {
	private Vec2f position;
	private Vec2f size;
	private float angle;
	private Vec2f speed;
	private Vec2f nextSpeed;
	private Vec2f acceleration;
	private Texture texture;
	private Color color;
	private Vec2f oldPosition;
	private Vec2f[] vertices;


	public Sprite(Vec2f position, Vec2f size, Texture texture) {
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

		vertices = new Vec2f[] { new Vec2f(), new Vec2f(), new Vec2f(), new Vec2f() };
		updateVertices();
	}

	@Override
	public Sprite clone() {
		Sprite sprite = new Sprite(position, size, texture);
		sprite.angle = angle;
		sprite.speed = speed.clone();
		sprite.acceleration = acceleration.clone();
		sprite.color = new Color(color);
		sprite.oldPosition = oldPosition.clone();

		return sprite;
	}

	public void finalize() {
		texture.dispose();
	}

	public Vec2f getPosition() {
		return position.clone();
	}

	public void setPosition(Vec2f position) {
		this.position = position.clone();
	}

	public Vec2f getSize() {
		return size.clone();
	}

	public Vec2f getMaxSize() {
		float maxX = Math.max(vertices[0].x, Math.max(vertices[1].x, Math.max(vertices[2].x, vertices[3].x)));
		float maxY = Math.max(vertices[0].y, Math.max(vertices[1].y, Math.max(vertices[2].y, vertices[3].y)));
		return new Vec2f(Math.abs(maxX - position.x) * 2, Math.abs(maxY - position.y) * 2);
	}

	public void setSize(Vec2f size) {
		this.size = size.clone();
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	public Vec2f getSpeed() {
		return speed.clone();
	}

	public void setSpeed(Vec2f speed) {
		this.speed = speed.clone();
	}

	public Vec2f getAcceleration() {
		return acceleration.clone();
	}

	public void setAcceleration(Vec2f acceleration) {
		this.acceleration = acceleration.clone();
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public Color getColor() {
		return new Color(color);
	}

	public void setColor(Color color) {
		this.color = new Color(color);
	}

	public void setAlpha(float alpha) {
		color.a = alpha;
	}

	public Vec2f getOldPosition() {
		return position.clone();
	}

	public void setOldPosition(Vec2f oldPosition) {
		this.oldPosition = oldPosition.clone();
	}

	public Vec2f getRotatedPosition(Vec2f relativePositionToRotate, float rotationAngle) {
		double angle = Math.toRadians(rotationAngle);
		double cosa = Math.cos(angle);
		double sina = Math.sin(angle);

		return new Vec2f(position.x + (float) (relativePositionToRotate.x * cosa - relativePositionToRotate.y * sina),
				position.y + (float) (relativePositionToRotate.x * sina + relativePositionToRotate.y * cosa));
	}

	public Vec2f getVertex(int vertexId) {
		if (vertexId >= 0 && vertexId < 4)
			return vertices[vertexId].clone();
		return new Vec2f();
	}

	private Vec2f getVertexComputing(int vertexId) {
		if (vertexId == 0)
			return getRotatedPosition(new Vec2f(-size.x / 2, -size.y / 2), (angle - 90));
		else if (vertexId == 1)
			return getRotatedPosition(new Vec2f(size.x / 2, -size.y / 2), (angle - 90));
		else if (vertexId == 2)
			return getRotatedPosition(new Vec2f(size.x / 2, +size.y / 2), (angle - 90));
		else if (vertexId == 3)
			return getRotatedPosition(new Vec2f(-size.x / 2, +size.y / 2), (angle - 90));
		return new Vec2f();
	}

	public void updateVertices() {
		for (int i = 0; i < 4; ++i)
			vertices[i].set(getVertexComputing(i));
	}

	public boolean isCollidedWithSprite(Sprite sprite) {
		if (new Vec2f(position.x - sprite.position.x, position.y - sprite.position.y).getLength() <= new Vec2f(size.x + sprite.size.x, size.y + sprite.size.y).getLength()) // Test simplifie
		{
			updateVertices();
			sprite.updateVertices();

			for (int i = 0; i < 4; ++i) {
				if (pointIsWithIn(sprite.getVertex(i)) || sprite.pointIsWithIn(getVertex(i)))
					return true;
			}

			if (sprite.position.x != sprite.oldPosition.x || sprite.position.y != sprite.oldPosition.y) {
				boolean ok = false;
				Vec2f spritePosition = new Vec2f(sprite.position);
				sprite.position = new Vec2f(sprite.oldPosition);

				for (int i = 0; i < 4; ++i) {
					if (pointIsWithIn(sprite.getVertex(i)) || sprite.pointIsWithIn(getVertex(i)))
						ok = true;
				}

				sprite.position = new Vec2f(spritePosition);

				return ok;
			}

			/*Vec2f spriteVertices[] = {new Vec2f(-sprite.size.x / 2, -sprite.size.y / 2),
					new Vec2f(sprite.size.x / 2, -sprite.size.y / 2),
					new Vec2f(sprite.size.x / 2, +sprite.size.y / 2),
					new Vec2f(-sprite.size.x / 2, +sprite.size.y / 2)};
			
			for (int i = 0; i < 4; ++i)
				spriteVertices[i] = sprite.getRotatedPosition(spriteVertices[i], sprite.angle - 90);
			
			for (int i = 0; i < 4; ++i)
			{
				if (isCollidedWithSegment(spriteVertices[i], spriteVertices[i + 1 > 3 ? 0 : i + 1]))
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
					if (isCollidedWithSegment(spriteVertices[i], spriteOldVertices[i]))
						return true;
				}
			}*/
		}

		return false;
	}

	public boolean isCollidedWithSegment(Vec2f A, Vec2f B) {
		for (int i = 0; i < 4; ++i) {
			if (borderIsCollidedWithSegment(A, B, i))
				return true;
		}

		return false;
	}

	public boolean borderIsCollidedWithSegment(Vec2f A, Vec2f B, int borderId) {
		if (borderId < 0 || borderId > 3)
			return false;

		// A et B : les deux points du premier segment
		// C et D : les deux points d'un des segments du sprite
		Vec2f C = vertices[borderId];
		Vec2f D = vertices[borderId == 3 ? 0 : borderId + 1];


		// Soit P le point d'intersection
		// On a P = A + k*AB et P = C + m*CD, donc A + k*AB = C + m*CD
		// Apr�s d�composition on a k et m

		Vec2f AB = new Vec2f(B.x - A.x, B.y - A.y);
		Vec2f CD = new Vec2f(D.x - C.x, D.y - C.y);

		float denominator = AB.x * CD.y - AB.y * CD.x;
		if (denominator == 0)
			return borderIsCollidedWithSegment(new Vec2f(A.x + 0.001f, A.y + 0.001f), B, borderId); // on recommence avec un petit decalage pour ne plus avoir de segments paralleles

		float k = -(A.x * CD.y - C.x * CD.y - CD.x * A.y + CD.x * C.y);
		float m = -(-AB.x * A.y + AB.x * C.y + AB.y * A.x - AB.y * C.x);

		if (k <= 0 || k >= denominator || m <= 0 || m >= denominator)
			return false;

		return true;
	}

	public boolean pointIsWithIn(Vec2f point) {
		// A, B, C : points du triangle

		for (int i = 0; i < 2; ++i) {
			// Compute vectors 
			Vec2f vectorAC = getVertex(2).getAdd(getVertex(0).getNegative());
			Vec2f vectorAB = getVertex(i == 0 ? 1 : 3).getAdd(getVertex(0).getNegative());
			Vec2f vectorAP = point.getAdd(getVertex(0).getNegative());

			// Compute dot products
			float dotACAC = vectorAC.getDot(vectorAC);
			float dotACAB = vectorAC.getDot(vectorAB);
			float dotACAP = vectorAC.getDot(vectorAP);
			float dotABAB = vectorAB.getDot(vectorAB);
			float dotABAP = vectorAB.getDot(vectorAP);

			// Compute barycentric coordinates
			float factor = 1 / (dotACAC * dotABAB - dotACAB * dotACAB);
			float u = (dotABAB * dotACAP - dotACAB * dotABAP) * factor; // Projection of AP on AB
			float v = (dotACAC * dotABAP - dotACAB * dotACAP) * factor; // Projection of AP on AC

			if (u >= 0 && v >= 0 && u + v < 1)
				return true;
		}

		return false;
	}

	public void draw(SpriteBatch display) {
		Color c = display.getColor();
		display.setColor(color.r, color.g, color.b, color.a);
		display.draw(new TextureRegion(texture), position.x - size.x / 2, position.y - size.y / 2, size.x / 2, size.y / 2, size.x, size.y, 1.f, 1.f, angle - 90, false);
		display.setColor(c.r, c.g, c.b, c.a);
	}

	public void updateSpeed(float lastFrameTime) {
		oldPosition = new Vec2f(position);
		speed.set(speed.x + acceleration.x * lastFrameTime, speed.y + acceleration.y * lastFrameTime);
		move(new Vec2f(speed.x * lastFrameTime, speed.y * lastFrameTime));
	}

	public void updateSpeed(float lastFrameTime, boolean rotationIsTakenAccount) {
		oldPosition = new Vec2f(position);

		if (rotationIsTakenAccount)
			updateSpeed(lastFrameTime);
		else {
			speed.set(speed.x + acceleration.x * lastFrameTime, speed.y + acceleration.y * lastFrameTime);
			position.set(position.x + speed.x * lastFrameTime, position.y + speed.y * lastFrameTime);
		}
	}

	public void move(Vec2f movement) {
		position.set(position.x + (float) Math.cos(angle * Math.PI / 180) * movement.x - (float) Math.sin(angle * Math.PI / 180) * movement.y,
				position.y + (float) Math.sin(angle * Math.PI / 180) * movement.x + (float) Math.cos(angle * Math.PI / 180) * movement.y);
	}
}
