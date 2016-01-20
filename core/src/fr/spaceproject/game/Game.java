package fr.spaceproject.game;



import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.spaceproject.factions.Geopolitics;
import fr.spaceproject.factions.WarMap;
import fr.spaceproject.gui.AngryBar;
import fr.spaceproject.gui.FactionMap;
import fr.spaceproject.gui.MiniMap;
import fr.spaceproject.gui.VesselState;
import fr.spaceproject.utils.Coor;
import fr.spaceproject.utils.TextureManager;
import fr.spaceproject.vessels.Vessel;

public class Game extends ApplicationAdapter
{
	protected SpriteBatch display;
	protected TextureManager textureManager;
	protected OrthographicCamera camera;
	protected float lastFrameTime;
	
	protected FactionMap carte; //affichage de la minicarte
	private MiniMap miniMap;
	private AngryBar reput;
	private VesselState stateVessel;
	
	protected WarMap map;	//map total de l'univers
	protected Geopolitics state; //array de faction pour les mettres a jour
	
	
	private SectorMap zone; //gere la zone en elle meme
	

	@Override
	public void create()
	{
		display = new SpriteBatch();
		
		textureManager = new TextureManager();
		textureManager.addTexture("MiniMap", "MiniMap.png");
		textureManager.addTexture("StationUselessMiniMap", "StationUselessMiniMap.png");
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
		textureManager.addTexture("BrokenEngineVesselModule", "BrokenEngineVesselModule.png");
		textureManager.addTexture("FlamesEngineVesselModule", "FlamesEngineVesselModule.png");
		textureManager.addTexture("ProjectileCannonVesselModule", "ProjectileCannonVesselModule.png");
		textureManager.addTexture("ProjectileLaserVesselModule", "ProjectileLaserVesselModule.png");
		textureManager.addTexture("ShieldVesselModule", "ShieldVesselModule.png");
		textureManager.addTexture("SimpleVesselModule", "SimpleVesselModule.png");
		textureManager.addTexture("SimpleStationModule", "SimpleStationModule.png");
		textureManager.addTexture("BackgroundStationModule", "BackgroundStationModule.png");
		textureManager.addTexture("BrokenStationModule", "BrokenStationModule.png");
		textureManager.addTexture("StationExplosion1", "Explosion/Station/1.png");
		textureManager.addTexture("StationExplosion2", "Explosion/Station/2.png");
		textureManager.addTexture("StationExplosion3", "Explosion/Station/3.png");
		textureManager.addTexture("StationExplosion4", "Explosion/Station/4.png");
		textureManager.addTexture("StationExplosion5", "Explosion/Station/5.png");
		
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
		camera.update();
		
		map = new WarMap();
		state = new Geopolitics(5);
		zone = new SectorMap(1500,new Coor(0,0),0, textureManager,state,map);
		carte =new FactionMap(zone.getVector().get(0).getPosition(),zone.getCoor(),map, textureManager);
		stateVessel=new VesselState(zone.getVector().get(0),textureManager);
		miniMap=new MiniMap(zone.getVector().get(0).getPosition(),zone,zone.getStation(),textureManager);
		reput=new AngryBar(zone.getVector().get(0).getPosition(),state,textureManager);
		
	}

	@Override
	public void render()
	{

		System.gc();

		
		// Mise a jour de l'etat des elements
		lastFrameTime = Gdx.graphics.getDeltaTime();
		zone.update(lastFrameTime,state);
		
		//Mise a jour de l'HUD
		carte.update(zone.getPlayer().getPosition(),zone.getCoor(),map);
		miniMap.update(zone,zone.getVector(),zone.getStation());
		reput.update(zone.getVector().get(0).getPosition(),state,textureManager);
		stateVessel.Update(zone.getVector().get(0),textureManager);
		//Mise a jour des coordonnees
		zone.updateExit(zone.getPlayer(),map,state);
		// Affichage
		camera.position.set(zone.getPlayer().getPosition().x, zone.getPlayer().getPosition().y, 0);
		camera.update();
		display.setProjectionMatrix(camera.combined);
		
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		display.begin();
		zone.draw(display);
		carte.draw(display);
		miniMap.draw(display);
		reput.draw(display);
		stateVessel.draw(display);
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
