package fr.spaceproject.gui;

import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.spaceproject.utils.Button;
import fr.spaceproject.utils.Orientation;
import fr.spaceproject.utils.Sprite;
import fr.spaceproject.utils.TextureManager;
import fr.spaceproject.utils.Vec2f;
import fr.spaceproject.utils.Vec2i;
import fr.spaceproject.vessels.Vessel;

public class VesselCreation
{
	TextureManager textureManager;
	private boolean isActive;
	private boolean canBeActived;
	private Sprite background;
	Button[] modulesButtons;
	Button[] orientationButtons;
	Button[][] vesselButtons;
	int[][] vesselButtonsModuleId;
	Button ok;
	Button cancel;
	
	public VesselCreation(TextureManager textureManager)
	{
		this.textureManager = textureManager;
		isActive = false;
		canBeActived = true;
		
		background = new Sprite(new Vec2f(), new Vec2f(400, 400), textureManager.getTexture("Blank"));
		background.setColor(new Color(1, 1, 1, 0.5f));
		
		modulesButtons = new Button[]
		{
			new Button(new Vec2f(), new Vec2f(40, 40), textureManager.getTexture("Blank")),
			new Button(new Vec2f(), new Vec2f(40, 40), textureManager.getTexture("SimpleVesselModule")),
			new Button(new Vec2f(), new Vec2f(40, 40), textureManager.getTexture("EngineVesselModule")),
			new Button(new Vec2f(), new Vec2f(40, 40), textureManager.getTexture("CannonVesselModule")),
			new Button(new Vec2f(), new Vec2f(40, 40), textureManager.getTexture("CannonVesselModule")),
			new Button(new Vec2f(), new Vec2f(40, 40), textureManager.getTexture("ShieldVesselModule"))
		};
		modulesButtons[1].setIsActive(true);
		
		orientationButtons = new Button[]
		{
			new Button(new Vec2f(), new Vec2f(40, 40), textureManager.getTexture("Arrow")),
			new Button(new Vec2f(), new Vec2f(40, 40), textureManager.getTexture("Arrow")),
			new Button(new Vec2f(), new Vec2f(40, 40), textureManager.getTexture("Arrow")),
			new Button(new Vec2f(), new Vec2f(40, 40), textureManager.getTexture("Arrow"))
		};
		orientationButtons[0].setIsActive(true);
		for (int i = 0; i < 4; ++i)
			orientationButtons[i].setAngle(i * 90);
		
		vesselButtons = new Button[7][7];
		vesselButtonsModuleId = new int[7][7];
		for (int x = 0; x < 7; ++x)
		{
			for (int y = 0; y < 7; ++y)
			{
				vesselButtons[x][y] = new Button(new Vec2f(), new Vec2f(30, 30), textureManager.getTexture("Blank"));
				vesselButtonsModuleId[x][y] = -2;
			}
		}
		vesselButtonsModuleId[3][3] = 1;
		
		ok = new Button(new Vec2f(), new Vec2f(40, 40), textureManager.getTexture("Blank"));
		ok.setColor(new Color(0, 1, 0, 1));
		cancel = new Button(new Vec2f(), new Vec2f(40, 40), textureManager.getTexture("Blank"));
		cancel.setColor(new Color(1, 0, 0, 1));
	}
	
	public void update(float lastFrameTime, Vec2f cameraPosition, Vec2f cameraSize, Vessel playerVessel)
	{
		background.setPosition(cameraPosition);
		ok.setPosition(cameraPosition.getAdd(-50, -170));
		cancel.setPosition(cameraPosition.getAdd(50, -170));
		ok.update(cameraPosition, cameraSize);
		cancel.update(cameraPosition, cameraSize);
		
		if (ok.isActive())
		{
			for (int x = 0; x < 7; ++x)
			{
				for (int y = 0; y < 7; ++y)
				{
					Orientation orientation = Orientation.Up;
					if (vesselButtons[x][y].getAngle() == 90)
						orientation = Orientation.Left;
					else if (vesselButtons[x][y].getAngle() == 180)
						orientation = Orientation.Down;
					else if (vesselButtons[x][y].getAngle() == 270)
						orientation = Orientation.Right;
					
					playerVessel.setModule(new Vec2i(x, y), vesselButtonsModuleId[x][y], 10, orientation);
				}
			}
			
			isActive = false;
			canBeActived = true;
			ok.setIsActive(false);
		}
		else if (cancel.isActive())
		{
			isActive = false;
			canBeActived = true;
			cancel.setIsActive(false);
		}
		
		
		
		// Désactivation des boutons si un bouton devient actif
		for (int i = 0; i < modulesButtons.length; ++i)
		{
			if (modulesButtons[i].isNowInactive())
				modulesButtons[i].setIsActive(true);
			else if (modulesButtons[i].isNowActive())
			{
				for (int j = 0; j < modulesButtons.length; ++j)
				{
					if (i != j)
						modulesButtons[j].setIsActive(false);
				}
			}
		}
		
		for (int i = 0; i < orientationButtons.length; ++i)
		{
			if (orientationButtons[i].isNowInactive())
				orientationButtons[i].setIsActive(true);
			else if (orientationButtons[i].isNowActive())
			{
				for (int j = 0; j < orientationButtons.length; ++j)
				{
					if (i != j)
						orientationButtons[j].setIsActive(false);
				}
			}
		}
		
		for (int x = 0; x < 7; ++x)
		{
			for (int y = 0; y < 7; ++y)
			{
				vesselButtons[x][y].update(cameraPosition, cameraSize);
				
				if (vesselButtonsModuleId[x][y] == -2)
					vesselButtons[x][y].setTexture(textureManager.getTexture("Blank"));
				else if (vesselButtonsModuleId[x][y] == 0)
					vesselButtons[x][y].setTexture(textureManager.getTexture("SimpleVesselModule"));
				else if (vesselButtonsModuleId[x][y] == 1)
					vesselButtons[x][y].setTexture(textureManager.getTexture("CockpitVesselModule"));
				else if (vesselButtonsModuleId[x][y] == 2)
					vesselButtons[x][y].setTexture(textureManager.getTexture("EngineVesselModule"));
				else if (vesselButtonsModuleId[x][y] == 3)
					vesselButtons[x][y].setTexture(textureManager.getTexture("CannonVesselModule"));
				else if (vesselButtonsModuleId[x][y] == 4)
					vesselButtons[x][y].setTexture(textureManager.getTexture("CannonVesselModule"));
				else if (vesselButtonsModuleId[x][y] == 5)
					vesselButtons[x][y].setTexture(textureManager.getTexture("ShieldVesselModule"));
				
				vesselButtons[x][y].setPosition(new Vec2f((x - 3f) * 30, (y - 3f) * 30 + 80).getAdd(cameraPosition));
			}
		}
		
		// Mise a jour de la couleur des boutons en fonction de leur etat
		for (int i = 0; i < modulesButtons.length; ++i)
		{
			modulesButtons[i].update(cameraPosition, cameraSize);
			modulesButtons[i].setPosition(new Vec2f((i - 2.5f) * 50, -125).getAdd(cameraPosition));
			
			if (modulesButtons[i].isActive())
				modulesButtons[i].setColor(new Color(1, 0.5f, 0.5f, 1f));
			else
				modulesButtons[i].setColor(new Color(1, 1, 1, 1f));
		}
		
		for (int i = 0; i < orientationButtons.length; ++i)
		{
			orientationButtons[i].update(cameraPosition, cameraSize);
			orientationButtons[i].setPosition(new Vec2f((i - 1.5f) * 50, -75).getAdd(cameraPosition));
			
			if (orientationButtons[i].isActive())
				orientationButtons[i].setColor(new Color(1, 0.5f, 0.5f, 1f));
			else
				orientationButtons[i].setColor(new Color(1, 1, 1, 1f));
		}
		
		// Mise a jour des boutons du vaisseau
		Vec2i activedVesselButton = getVesselButton();
		
		if (activedVesselButton.x != -1 && !(activedVesselButton.x == 3 && activedVesselButton.y == 3))
		{
			vesselButtons[activedVesselButton.x][activedVesselButton.y].setAngle(getActiveOrientationButton() * 90);
			
			if (getActiveModuleButton() == 0)
				vesselButtonsModuleId[activedVesselButton.x][activedVesselButton.y] = -2;
			if (getActiveModuleButton() == 1)
				vesselButtonsModuleId[activedVesselButton.x][activedVesselButton.y] = 0;
			if (getActiveModuleButton() == 2)
				vesselButtonsModuleId[activedVesselButton.x][activedVesselButton.y] = 2;
			if (getActiveModuleButton() == 3)
				vesselButtonsModuleId[activedVesselButton.x][activedVesselButton.y] = 3;
			if (getActiveModuleButton() == 4)
				vesselButtonsModuleId[activedVesselButton.x][activedVesselButton.y] = 4;
			if (getActiveModuleButton() == 5)
				vesselButtonsModuleId[activedVesselButton.x][activedVesselButton.y] = 5;
		}
		
		
		// Desactivation des boutons du vaisseau
		for (int x = 0; x < 7; ++x)
		{
			for (int y = 0; y < 7; ++y)
			{
				if (vesselButtons[x][y].isActive())
					vesselButtons[x][y].setIsActive(false);
			}
		}
		
		// Mise a jour de l'etat de la fenetre
		if (!Gdx.input.isKeyPressed(Keys.C))
			canBeActived = true;
		
		if (canBeActived && Gdx.input.isKeyPressed(Keys.C))
		{
			isActive = !isActive;
			canBeActived = false;
		}
	}
	
	public void draw(SpriteBatch display)
	{
		if (isActive)
		{
			background.draw(display);
			
			for (int i = 0; i < modulesButtons.length; ++i)
				modulesButtons[i].draw(display);
			for (int i = 0; i < orientationButtons.length; ++i)
				orientationButtons[i].draw(display);
			
			for (int x = 0; x < 7; ++x)
			{
				for (int y = 0; y < 7; ++y)
					vesselButtons[x][y].draw(display);
			}
			
			ok.draw(display);
			cancel.draw(display);
		}
	}
	
	public int getActiveModuleButton()
	{
		for (int i = 0; i < modulesButtons.length; ++i)
		{
			if (modulesButtons[i].isActive())
				return i;
		}
		
		return -1;
	}
	
	public int getActiveOrientationButton()
	{
		for (int i = 0; i < orientationButtons.length; ++i)
		{
			if (orientationButtons[i].isActive())
				return i;
		}
		
		return -1;
	}
	
	public Vec2i getVesselButton()
	{
		for (int x = 0; x < 7; ++x)
		{
			for (int y = 0; y < 7; ++y)
			{
				if (vesselButtons[x][y].isActive())
					return new Vec2i(x, y);
			}
		}
		
		return new Vec2i(-1, -1);
	}
}
