package fr.spaceproject.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.spaceproject.factions.Faction;
import fr.spaceproject.factions.WarMap;
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
	
	protected WarMap Map;
	
	protected Faction green;
	protected Faction blue;
	protected Faction red;
	protected Faction yellow;
	protected Faction Neutral;
	

	
	@Override
	public void create()
	{
		display = new SpriteBatch();
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
		camera.update();
	    
		obstacle = new Sprite(new Vec2f(0, 0), new Vec2f(100, 100), "SimpleVesselModule.png");
		playerVessel = new Vessel(new Vec2f(200, 200), new Vec2i(3, 4), new Vec2i(1, 1), false, 0);
		playerVessel.setModule(new Vec2i(1, 3), 0, 1);
		playerVessel.setModule(new Vec2i(1, 2), 0, 1);
		playerVessel.setModule(new Vec2i(0, 1), 0, 1);
		playerVessel.setModule(new Vec2i(2, 1), 0, 1);
		playerVessel.setModule(new Vec2i(0, 0), 2, 1);
		playerVessel.setModule(new Vec2i(2, 0), 2, 1);
		
		Map = new WarMap();
		
		Neutral = new Faction(0,0,"grey");
		blue = new Faction(1,1,"blue");
		red = new Faction(2,1,"red");
		yellow = new Faction(3,1,"yellow");
		green = new Faction(4,1,"green");
		
	}

	@Override
	public void render()
	{
		// Mise � jour de l'�tat des �lements
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
		
		
		if (Gdx.input.isKeyPressed(Keys.M)){
			for (int j=1;j>-2;j--){
				for (int i=-1;i<2;i++){
					System.out.print(Map.appartCoor(i+" "+j));
				}
				System.out.println();
			}
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
