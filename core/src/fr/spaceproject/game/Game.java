package fr.spaceproject.game;

import java.util.Map;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import fr.spaceproject.factions.Sector;
import fr.spaceproject.utils.SettingsFile;
import fr.spaceproject.utils.Sprite;
import fr.spaceproject.utils.Vec2f;
import fr.spaceproject.utils.Vec2i;
import fr.spaceproject.vessels.Vessel;

public class Game extends ApplicationAdapter
{
	protected SpriteBatch display;
	protected OrthographicCamera camera;
	protected float lastFrameTime;
	
	protected Sprite obstacle;
	protected Vessel playerVessel;
	
	
	
	@Override
	public void create()
	{
		display = new SpriteBatch();
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
		camera.update();
	    
		obstacle = new Sprite(new Vec2f(0, 0), new Vec2f(), "SimpleVesselModule.png");
		playerVessel = new Vessel(new Vec2f(200, 200), new Vec2i(3, 4), new Vec2i(1, 1), false, 0);
		playerVessel.setModule(new Vec2i(1, 3), 0, 1);
		playerVessel.setModule(new Vec2i(1, 2), 0, 1);
		playerVessel.setModule(new Vec2i(0, 1), 0, 1);
		playerVessel.setModule(new Vec2i(2, 1), 0, 1);
		playerVessel.setModule(new Vec2i(0, 0), 2, 1);
		playerVessel.setModule(new Vec2i(2, 0), 2, 1);
	}

	@Override
	public void render()
	{
		// Mise à jour de l'état des élements
		lastFrameTime = Gdx.graphics.getDeltaTime();
		
		playerVessel.updateSpeed(lastFrameTime);
		if (Gdx.input.isKeyPressed(Keys.LEFT))
			playerVessel.setAngle(playerVessel.getAngle() + lastFrameTime * 100);
		if (Gdx.input.isKeyPressed(Keys.RIGHT))
			playerVessel.setAngle(playerVessel.getAngle() - lastFrameTime * 100);
		if (Gdx.input.isKeyPressed(Keys.UP))
			playerVessel.setSpeed(new Vec2f(0, lastFrameTime * 10000));
		else if (Gdx.input.isKeyPressed(Keys.DOWN))
			playerVessel.setSpeed(new Vec2f(0, -lastFrameTime * 10000));
		else
			playerVessel.setSpeed(new Vec2f(0, 0));
		
		playerVessel.update();
		
		// Affichage
		camera.position.set(playerVessel.getPosition().x, playerVessel.getPosition().y, 0);
		camera.update();
		display.setProjectionMatrix(camera.combined);
		
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		display.begin();
		playerVessel.draw(display);
		obstacle.draw(display);
		display.end();
	}
	
	@Override
	public void pause()	// Quand le jeu est en pause sur Android, ou quand on quitte
	{
		// Sauvegarde du jeu
	}
	
	@Override
	public void resume() // Quand le jeu reprend sur Android
	{
	}
	
	@Override
	public void resize(int width, int height) // Quand la fenêtre est redimensionné
	{
		camera.viewportWidth = Gdx.graphics.getWidth();
		camera.viewportHeight = Gdx.graphics.getHeight();
		camera.update();
	}
}
