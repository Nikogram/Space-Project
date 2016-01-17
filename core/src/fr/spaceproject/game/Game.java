package fr.spaceproject.game;

import java.util.Vector;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.spaceproject.factions.Faction;
import fr.spaceproject.factions.Geopolitics;
import fr.spaceproject.factions.WarMap;
import fr.spaceproject.gui.FactionMap;
import fr.spaceproject.utils.Coor;
import fr.spaceproject.utils.Sprite;
import fr.spaceproject.utils.Vec2f;
import fr.spaceproject.utils.Vec2i;
import fr.spaceproject.vessels.Vessel;

public class Game extends ApplicationAdapter
{
	protected SpriteBatch display;
	protected OrthographicCamera camera;
	protected float lastFrameTime;
	
	protected Vessel playerVessel;
	protected Vessel ennemyVessel;
<<<<<<< HEAD
	protected FactionMap carte;
=======
	protected Vessel obstacle;
>>>>>>> ef1fa4201967cd584e7bdf80fdd35d6e987eb96d
	
	protected WarMap map;
	protected Geopolitics state;
	


	@Override
	public void create()
	{
		display = new SpriteBatch();
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
		camera.update();
	    
		playerVessel = new Vessel(new Vec2f(0, 0), new Vec2i(5, 5), new Vec2i(2, 1), false, 0);
		playerVessel.generate(2);
		ennemyVessel = new Vessel(new Vec2f(-500, -500), new Vec2i(5, 5), new Vec2i(2, 1), true, 0);
		ennemyVessel.generate(2);
		obstacle = new Vessel(new Vec2f(-200, -200), new Vec2i(5, 5), new Vec2i(2, 1), true, 0);
		
		map = new WarMap();
		carte =new FactionMap(playerVessel.getPosition(),new Coor(0,0),map);
		state = new Geopolitics(5);
		
	}

	@Override
	public void render()
	{
		Vector<Vessel> vessels = new Vector<Vessel>();
		vessels.add(playerVessel);
		vessels.add(ennemyVessel);
		
		// Mise a jour de l'etat des elements
		lastFrameTime = Gdx.graphics.getDeltaTime();
		ennemyVessel.update(lastFrameTime, vessels);
		playerVessel.update(lastFrameTime, vessels);
		
		
		carte.update(playerVessel.getPosition(),new Coor(0,0),map);
		
		// Affichage
		camera.position.set(playerVessel.getPosition().x, playerVessel.getPosition().y, 0);
		camera.update();
		display.setProjectionMatrix(camera.combined);
		
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		display.begin();
		ennemyVessel.draw(display);
		playerVessel.draw(display);
<<<<<<< HEAD
		carte.draw(display);
=======
		obstacle.draw(display);
>>>>>>> ef1fa4201967cd584e7bdf80fdd35d6e987eb96d
		display.end();
		
		
		if (Gdx.input.isKeyPressed(Keys.M))
			map.warBegin(state);
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
	public void resize(int width, int height) // Quand la fen�tre est redimensionn�
	{
		camera.viewportWidth = Gdx.graphics.getWidth();
		camera.viewportHeight = Gdx.graphics.getHeight();
		camera.update();
	}
}
