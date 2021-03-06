package fr.spaceproject.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.spaceproject.utils.Sprite;
import fr.spaceproject.utils.TextureManager;
import fr.spaceproject.utils.Vec2f;
import fr.spaceproject.utils.Vec2i;
import fr.spaceproject.vessels.Vessel;
import fr.spaceproject.vessels.VesselModuleType;


public class VesselState {

	private Sprite[][] playerImage;
	private VesselModuleType[][] types;


	public VesselState(Vessel playerVessel, TextureManager textureManager) {
		playerImage = new Sprite[playerVessel.getSize().x][playerVessel.getSize().y];
		types = new VesselModuleType[playerVessel.getSize().x][playerVessel.getSize().y];
		for (int i = 0; i < playerVessel.getSize().x; i++)
			for (int j = 0; j < playerVessel.getSize().y; j++)
				if (playerVessel.getModuleType(new Vec2i(i, j)).ordinal() > VesselModuleType.Broken.ordinal()) {
					types[i][j] = playerVessel.getModuleType(new Vec2i(i, j));
					playerImage[i][j] = new Sprite(playerVessel.getPosition().getAdd(-Gdx.graphics.getWidth() / 2 + 25 + 31 * i, Gdx.graphics.getHeight() / 2 - 210 + 31 * j), new Vec2f(0, 0),
							textureManager.getTexture("PlayerVesselState" + (playerVessel.getModuleType(new Vec2i(i, j)).ordinal() - 2)));
					playerImage[i][j].setColor(new Color(0, 1, 0, 1));
				}
				else
					types[i][j] = VesselModuleType.Inexisting;

	}

	public void Update(Vessel playerVessel, TextureManager textureManager) {
		playerImage = new Sprite[playerVessel.getSize().x][playerVessel.getSize().y];
		for (int i = 0; i < playerVessel.getSize().x; i++)
			for (int j = 0; j < playerVessel.getSize().y; j++) {
				if (playerVessel.getModuleType(new Vec2i(i, j)).equals(types[i][j]) && types[i][j].ordinal() > VesselModuleType.Broken.ordinal()) {
					playerImage[i][j] = new Sprite(playerVessel.getPosition().getAdd(-Gdx.graphics.getWidth() / 2 + 25 + 31 * i, Gdx.graphics.getHeight() / 2 - 210 + 31 * j), new Vec2f(0, 0),
							textureManager.getTexture("PlayerVesselState" + (playerVessel.getModuleType(new Vec2i(i, j)).ordinal() - 2)));
					playerImage[i][j].setColor(ColorModule(playerVessel, i, j));
				}
				if (!playerVessel.getModuleType(new Vec2i(i, j)).equals(types[i][j])) {
					playerImage[i][j] = new Sprite(playerVessel.getPosition().getAdd(-Gdx.graphics.getWidth() / 2 + 25 + 31 * i, Gdx.graphics.getHeight() / 2 - 210 + 31 * j), new Vec2f(0, 0),
							textureManager.getTexture("PlayerVesselState" + (types[i][j].ordinal() - 2)));
					playerImage[i][j].setColor(new Color(0.2f, 0.2f, 0.2f, 1));
				}
			}
	}

	public void draw(SpriteBatch display) {
		for (int i = 0; i < playerImage.length; i++)
			for (int j = 0; j < playerImage[0].length; j++)
				if (playerImage[i][j] != null)
					playerImage[i][j].draw(display);
	}


	private Color ColorModule(Vessel playerVessel, int i, int j) {
		if (playerVessel.getModuleEnergy(new Vec2i(i, j), true) < 0.25f * playerVessel.getModuleMaxEnergy(new Vec2i(i, j)))
			return new Color(1, 0, 0, 1);
		else if (playerVessel.getModuleEnergy(new Vec2i(i, j), true) < 0.5f * playerVessel.getModuleMaxEnergy(new Vec2i(i, j)))
			return new Color(1, 1, 0, 1);
		else
			return new Color(0, 1, 0, 1);
	}
}
