package fr.spaceproject.game;

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

import fr.spaceproject.utils.SettingsFile;
import fr.spaceproject.utils.Sprite;
import fr.spaceproject.utils.Vec2f;

public class Game extends ApplicationAdapter
{
	protected SpriteBatch display;
	protected OrthographicCamera camera;
	protected float lastFrameTime;
	
	protected Sprite sprite;
	protected Sprite sprite2;
	
	
	@Override
	public void create()
	{
		display = new SpriteBatch();
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
		camera.update();
	    
		sprite = new Sprite(new Vec2f(200, 200), new Vec2f(88, 60), "ExampleVessel.png");
		sprite2 = new Sprite(new Vec2f(0, 0), new Vec2f(0, 0), "ExampleVessel.png");
		
		System.out.println(sprite.size.x);
	}

	@Override
	public void render()
	{
		// Mise à jour de l'état des éléments
		lastFrameTime = Gdx.graphics.getDeltaTime();
		sprite.updateSpeed(lastFrameTime);
		
		if (Gdx.input.isKeyPressed(Keys.LEFT))
			sprite.angle += lastFrameTime * 100;
		if (Gdx.input.isKeyPressed(Keys.RIGHT))
			sprite.angle -= lastFrameTime * 100;
		if (Gdx.input.isKeyPressed(Keys.UP))
			sprite.speed.y = lastFrameTime * 10000;
		else if (Gdx.input.isKeyPressed(Keys.DOWN))
			sprite.speed.y = -lastFrameTime * 10000;
		else
			sprite.speed = new Vec2f(0, 0);
		
		// Affichage
		camera.position.set(sprite.position.x, sprite.position.y, 0);
		camera.update();
		display.setProjectionMatrix(camera.combined);
		
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		display.begin();
		sprite.draw(display);
		sprite2.draw(display);
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
	public void resize(int width, int height) // Quand la fenêtre est redimensionnée
	{
		camera.viewportWidth = Gdx.graphics.getWidth();
		camera.viewportHeight = Gdx.graphics.getHeight();
		camera.update();
	}
}
