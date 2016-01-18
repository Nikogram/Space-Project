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
import fr.spaceproject.gui.MiniMap;
import fr.spaceproject.utils.Coor;
import fr.spaceproject.utils.Sprite;
import fr.spaceproject.utils.TextureManager;
import fr.spaceproject.utils.Vec2f;
import fr.spaceproject.utils.Vec2i;
import fr.spaceproject.vessels.Vessel;

public class Game extends ApplicationAdapter
{
	protected SpriteBatch display;
	protected TextureManager textureManager;
	protected OrthographicCamera camera;
	protected float lastFrameTime;
	
	
	protected FactionMap carte; //affichage de la minicarte
	private MiniMap miniMap;
	protected Vessel obstacle;
	private Vessel playerVessel;
	
	protected WarMap map;	//map total de l'univers
	protected Geopolitics state; //array de faction pour les mettres a jour
	
	
	private SectorMap zone; //gere la zone en elle meme
	

	@Override
	public void create()
	{
		display = new SpriteBatch();
		
		textureManager = new TextureManager();
		textureManager.addTexture("MiniMap", "MiniMap.png");
		textureManager.addTexture("MyVesselMiniMap", "MyVesselMiniMap.png");
		textureManager.addTexture("EnnemiMiniMap", "EnnemiMiniMap.png");
		textureManager.addTexture("BrokenVesselModule", "BrokenVesselModule.png");
		textureManager.addTexture("BrokenVesselModule", "BrokenVesselModule.png");
		textureManager.addTexture("CannonVesselModule", "CannonVesselModule.png");
		textureManager.addTexture("CockpitVesselModule", "CockpitVesselModule.png");
		textureManager.addTexture("ColorFaction0", "ColorFaction0.png");
		textureManager.addTexture("ColorFaction1", "ColorFaction1.png");
		textureManager.addTexture("ColorFaction2", "ColorFaction2.png");
		textureManager.addTexture("ColorFaction3", "ColorFaction3.png");
		textureManager.addTexture("ColorFaction4", "ColorFaction4.png");
		textureManager.addTexture("EngineVesselModule", "EngineVesselModule.png");
		textureManager.addTexture("FlamesEngineVesselModule", "FlamesEngineVesselModule.png");
		textureManager.addTexture("ProjectileCannonVesselModule", "ProjectileCannonVesselModule.png");
		textureManager.addTexture("ProjectileLaserVesselModule", "ProjectileLaserVesselModule.png");
		textureManager.addTexture("ShieldVesselModule", "ShieldVesselModule.png");
		textureManager.addTexture("SimpleVesselModule", "SimpleVesselModule.png");
		
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
		camera.update();
	    
		playerVessel = new Vessel(new Vec2f(0, 0), new Vec2i(5, 5), new Vec2i(2, 1), false, 0, textureManager);
		playerVessel.generate(2);
		obstacle = new Vessel(new Vec2f(-200, -200), new Vec2i(5, 5), new Vec2i(2, 1), true, 0, textureManager);
		
		map = new WarMap();
		zone = new SectorMap(1500,new Coor(0,0),4, textureManager);
		carte =new FactionMap(playerVessel.getPosition(),zone.getCoor(),map, textureManager);
		miniMap=new MiniMap(playerVessel.getPosition(),zone,textureManager);
		state = new Geopolitics(5);
		
		
	}

	@Override
	public void render()
	{
		System.gc();
		
		Vector<Vessel> vessels = new Vector<Vessel>();
		vessels.add(playerVessel);
		zone.updateadd(vessels);
		// Mise a jour de l'etat des elements
		lastFrameTime = Gdx.graphics.getDeltaTime();
		zone.updateTime(lastFrameTime, vessels);
		playerVessel.update(lastFrameTime, vessels);
		
		//Mise a jour de l'HUD
		carte.update(playerVessel.getPosition(),zone.getCoor(),map);
		miniMap.update(playerVessel.getPosition(), zone,vessels);
		//Mise a jour des coordonnees
		zone.updateExit(playerVessel,map);
		// Affichage
		camera.position.set(playerVessel.getPosition().x, playerVessel.getPosition().y, 0);
		camera.update();
		display.setProjectionMatrix(camera.combined);
		
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		display.begin();
		zone.draw(display);
		playerVessel.draw(display);
		carte.draw(display);
		miniMap.draw(display);
		obstacle.draw(display);
		display.end();
		
		if (Gdx.input.isKeyPressed(Keys.M))
			map.warBegin(state);
		
		System.out.println(1 / lastFrameTime);
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
	public void resize(int width, int height) // Quand la fenetre est redimensionne
	{
		camera.viewportWidth = Gdx.graphics.getWidth();
		camera.viewportHeight = Gdx.graphics.getHeight();
		camera.update();
	}
}
