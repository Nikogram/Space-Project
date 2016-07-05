package fr.spaceproject.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.spaceproject.utils.Sprite;
import fr.spaceproject.utils.TextureManager;
import fr.spaceproject.utils.Vec2f;


public class Background {
	private Sprite[] starsSprites;


	Background(Vec2f sectorSize, TextureManager textureManager) {
		starsSprites = new Sprite[(int) (sectorSize.x * sectorSize.y / 1000)];

		for (int i = 0; i < starsSprites.length; ++i) {
			float starSize = (float) (Math.random() * 2 + 1);
			starsSprites[i] = new Sprite(new Vec2f(2 * (float) (Math.random() * 2 * sectorSize.x - sectorSize.x), 2 * (float) (Math.random() * 2 * sectorSize.y - sectorSize.y)),
					new Vec2f(starSize, starSize), textureManager.getTexture("Blank"));
		}
	}

	public void draw(SpriteBatch display, Vec2f cameraPosition) {
		Vec2f cameraSize = new Vec2f(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		for (int i = 0; i < starsSprites.length; ++i) {
			if (starsSprites[i].getPosition().x > cameraPosition.x - cameraSize.x && starsSprites[i].getPosition().y > cameraPosition.y - cameraSize.y &&
					starsSprites[i].getPosition().x < cameraPosition.x + cameraSize.x && starsSprites[i].getPosition().y < cameraPosition.y + cameraSize.y)
				starsSprites[i].draw(display);
		}
	}
}
