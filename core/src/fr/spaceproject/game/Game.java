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
	
	protected WarMap map;
	protected Geopolitics state;
	
	public int test;


	@Override
	public void create()
	{
		display = new SpriteBatch();
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
		camera.update();
	    
		playerVessel = new Vessel(new Vec2f(0, 0), new Vec2i(5, 5), new Vec2i(2, 1), false, 0);
		playerVessel.generate(2);
		ennemyVessel = new Vessel(new Vec2f(-200, -200), new Vec2i(5, 5), new Vec2i(2, 1), true, 0);
		ennemyVessel.generate(1);
		
		map = new WarMap();
		
		state = new Geopolitics(5);
		
		int test =0;
	}

	@Override
	public void render()
	{
		Vector<Vessel> vessels = new Vector<Vessel>();
		vessels.add(playerVessel);
		vessels.add(ennemyVessel);
		
		// Mise a jour de l'etat des elements
		lastFrameTime = Gdx.graphics.getDeltaTime();
		playerVessel.update(lastFrameTime, vessels);
		ennemyVessel.update(lastFrameTime, vessels);
		
		
		// Affichage
		camera.position.set(playerVessel.getPosition().x, playerVessel.getPosition().y, 0);
		camera.update();
		display.setProjectionMatrix(camera.combined);
		
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		if (playerVessel.isCollidedWithVessel(ennemyVessel))
			Gdx.gl.glClearColor(0.5f, 0, 0, 1);
		else
			Gdx.gl.glClearColor(0, 0, 0, 1);
		
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		display.begin();
		ennemyVessel.draw(display);
		playerVessel.draw(display);
		display.end();
		
		
		if (Gdx.input.isKeyPressed(Keys.M)){
			for (int j=2;j>-3;j--){
				for (int i=-2;i<3;i++){
					System.out.print(map.appartCoor((new Coor(i,j)).toStrings())+ " ");
				}
				System.out.println();
			}
			System.out.println("-----");
			map.warBegin(state);
			System.out.println("\n"+test);
			test=test+1;
		}
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
