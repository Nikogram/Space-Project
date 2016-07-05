package fr.spaceproject.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Texture;


public class Button extends Sprite {
	boolean isActive;
	boolean canBeActived;
	boolean isNowActive;
	boolean isNowInactive;


	public Button(Vec2f position, Vec2f size, Texture texture) {
		super(position, size, texture);
		isActive = false;
		canBeActived = true;
	}

	public void update(Vec2f cameraPosition, Vec2f cameraSize) {
		if (!Gdx.input.isButtonPressed(Buttons.LEFT))
			canBeActived = true;
		isNowActive = false;
		isNowInactive = false;

		Vec2f mousePosition = new Vec2f(Gdx.input.getX() + cameraPosition.x - cameraSize.x / 2, (cameraSize.y - Gdx.input.getY()) + cameraPosition.y - cameraSize.y / 2);

		if (mousePosition.x >= getPosition().x - getSize().x / 2 &&
				mousePosition.x <= getPosition().x + getSize().x / 2 &&
				mousePosition.y >= getPosition().y - getSize().y / 2 &&
				mousePosition.y <= getPosition().y + getSize().y / 2 &&
				Gdx.input.isButtonPressed(Buttons.LEFT) && canBeActived) {
			isActive = !isActive;
			canBeActived = false;
			if (isActive)
				isNowActive = true;
			else
				isNowInactive = true;
		}
	}

	public boolean isActive() {
		return isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isNowActive() {
		return isNowActive;
	}

	public boolean isNowInactive() {
		return isNowInactive;
	}
}
