package fr.spaceproject.game;

import java.util.Vector;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.spaceproject.factions.Geopolitics;
import fr.spaceproject.factions.WarMap;
import fr.spaceproject.gui.FactionMap;
import fr.spaceproject.gui.MiniMap;
import fr.spaceproject.station.Station;
import fr.spaceproject.utils.Coor;
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
	
	protected Vector<Vessel> vessels;
	protected FactionMap carte; //affichage de la minicarte
	private MiniMap miniMap;
<<<<<<< HEAD
	protected Vessel obstacle;
=======
	private Vessel playerVessel;
	private Station station;
>>>>>>> 48546bca19302f822240e047e6e11f365b0829c4
	
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
		textureManager.addTexture("SimpleStationModule", "SimpleStationModule.png");
		
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
		camera.update();
	    
<<<<<<< HEAD
		
		obstacle = new Vessel(new Vec2f(-200, -200), new Vec2i(3, 3), new Vec2i(2, 1), true, 0, textureManager);
		
		map = new WarMap();
		zone = new SectorMap(1500,new Coor(0,0),3, textureManager);
		carte =new FactionMap(zone.getPlayer().getPosition(),zone.getCoor(),map, textureManager);
		miniMap=new MiniMap(zone.getPlayer().getPosition(),zone,textureManager);
=======
		vessels = new Vector<Vessel>();
		playerVessel = new Vessel(new Vec2f(0, 0), new Vec2i(5, 5), new Vec2i(1, 1), false, 0, textureManager);
		playerVessel.generate(2);
		station = new Station(new Vec2f(0, 0), new Vec2i(10, 5), 1, textureManager);
		
		map = new WarMap();
		zone = new SectorMap(1500,new Coor(0,0),5, textureManager);
		carte =new FactionMap(playerVessel.getPosition(),zone.getCoor(),map, textureManager);
		miniMap=new MiniMap(playerVessel.getPosition(),zone,textureManager);
>>>>>>> 48546bca19302f822240e047e6e11f365b0829c4
		state = new Geopolitics(5);
	}

	@Override
	public void render()
	{
<<<<<<< HEAD
		System.gc();
=======
		vessels.clear();
		vessels.add(playerVessel);
		zone.updateadd(vessels);
>>>>>>> 48546bca19302f822240e047e6e11f365b0829c4
		
		// Mise a jour de l'etat des elements
		lastFrameTime = Gdx.graphics.getDeltaTime();
		zone.update(lastFrameTime);
		
		//Mise a jour de l'HUD
		carte.update(zone.getPlayer().getPosition(),zone.getCoor(),map);
		miniMap.update(zone,zone.getVector());
		//Mise a jour des coordonnees
		zone.updateExit(zone.getPlayer(),map);
		// Affichage
		camera.position.set(zone.getPlayer().getPosition().x, zone.getPlayer().getPosition().y, 0);
		camera.update();
		display.setProjectionMatrix(camera.combined);
		
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		display.begin();
		station.draw(display);
		zone.draw(display);
		carte.draw(display);
		miniMap.draw(display);
		display.end();
		
		if (Gdx.input.isKeyPressed(Keys.M))
			map.warBegin(state);
		
		//System.out.println(1 / lastFrameTime);
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
