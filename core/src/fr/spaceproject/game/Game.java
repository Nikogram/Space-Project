package fr.spaceproject.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.spaceproject.utils.Orientation;
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
	    
		obstacle = new Sprite(new Vec2f(0, 0), new Vec2f(100, 100), "SimpleVesselModule.png");
		playerVessel = new Vessel(new Vec2f(200, 200), new Vec2i(5, 5), new Vec2i(2, 1), false, 0);
		playerVessel.generate(2);
	}

	@Override
	public void render()
	{
		// Mise à jour de l'état des élements
		lastFrameTime = Gdx.graphics.getDeltaTime();
		playerVessel.update(lastFrameTime);
		
		
		// Affichage
		camera.position.set(playerVessel.getPosition().x, playerVessel.getPosition().y, 0);
		camera.update();
		display.setProjectionMatrix(camera.combined);
		
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		display.begin();
		obstacle.draw(display);
		playerVessel.draw(display);
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
