package fr.spaceproject.game;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.spaceproject.factions.Geopolitics;
import fr.spaceproject.factions.WarMap;
import fr.spaceproject.gui.AngryBar;
import fr.spaceproject.gui.FactionMap;
import fr.spaceproject.gui.MiniMap;
import fr.spaceproject.gui.VesselCreation;
import fr.spaceproject.gui.VesselState;
import fr.spaceproject.utils.Coor;
import fr.spaceproject.utils.TextureManager;
import fr.spaceproject.utils.Time;
import fr.spaceproject.utils.Vec2f;


public class Game extends ApplicationAdapter {
	protected SpriteBatch display;
	protected TextureManager textureManager;
	protected OrthographicCamera camera;
	float cameraAngle = 0;
	protected float lastFrameTime;
	private Time time;

	protected FactionMap carte; //affichage de la minicarte
	private MiniMap miniMap;
	private AngryBar reput;
	private VesselState stateVessel;

	protected WarMap map; //map total de l'univers
	protected Geopolitics state; //array de faction pour les mettres a jour	
	public personnage player; //gere les attribut du personnage


	private SectorMap zone; //gere la zone en elle meme

	protected VesselCreation vesselCreation; // interface de creation du vaisseau du joueur


	@Override
	public void create() {
		display = new SpriteBatch();

		textureManager = new TextureManager();
		textureManager.addTexture("MiniMap", "MiniMap.png");
		textureManager.addTexture("StationUselessMiniMap", "StationUselessMiniMap.png");
		textureManager.addTexture("MyVesselMiniMap", "MyVesselMiniMap.png");
		textureManager.addTexture("EnnemiMiniMap", "EnnemiMiniMap.png");
		textureManager.addTexture("PlayerVesselState0", "PlayerVesselState0.png");
		textureManager.addTexture("PlayerVesselState1", "PlayerVesselState1.png");
		textureManager.addTexture("PlayerVesselState2", "PlayerVesselState2.png");
		textureManager.addTexture("PlayerVesselState3", "PlayerVesselState3.png");
		textureManager.addTexture("PlayerVesselState4", "PlayerVesselState4.png");
		textureManager.addTexture("PlayerVesselState5", "PlayerVesselState5.png");
		textureManager.addTexture("PlayerVesselState6", "PlayerVesselState6.png");
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
		textureManager.addTexture("ShieldBallVesselModule", "ShieldBallVesselModule.png");
		textureManager.addTexture("ReinforcedVesselModule", "ReinforcedVesselModule.png");
		textureManager.addTexture("SimpleVesselModule", "SimpleVesselModule.png");
		textureManager.addTexture("SimpleStationModule", "SimpleStationModule.png");
		textureManager.addTexture("BackgroundStationModule", "BackgroundStationModule.png");
		textureManager.addTexture("CannonStationModule", "CannonStationModule.png");
		textureManager.addTexture("BrokenStationModule", "BrokenStationModule.png");
		textureManager.addTexture("StationExplosion1", "Explosion/Station/1.png");
		textureManager.addTexture("StationExplosion2", "Explosion/Station/2.png");
		textureManager.addTexture("StationExplosion3", "Explosion/Station/3.png");
		textureManager.addTexture("StationExplosion4", "Explosion/Station/4.png");
		textureManager.addTexture("StationExplosion5", "Explosion/Station/5.png");
		textureManager.addTexture("Blank", "Blank.png");
		textureManager.addTexture("Arrow", "Arrow.png");

		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
		camera.update();
		time = new Time();
		map = new WarMap();
		state = new Geopolitics(5);

		zone = new SectorMap(4000, new Coor(0, 0), 0, textureManager, state, map);
		vesselCreation = new VesselCreation(textureManager);
		player = new personnage();

		carte = new FactionMap(zone.getVector().get(0).getPosition(), zone.getCoor(), map, textureManager);
		stateVessel = new VesselState(zone.getVector().get(0), textureManager);
		miniMap = new MiniMap(zone.getVector().get(0).getPosition(), zone, zone.getStation(), textureManager);
		reput = new AngryBar(zone.getVector().get(0).getPosition(), state, textureManager);

	}

	@Override
	public void render() {
		// Mise a jour de l'etat des elements
		lastFrameTime = Gdx.graphics.getDeltaTime();
		zone.update(lastFrameTime, state, map, player);
		time.update(lastFrameTime);
		//Mise a jour de l'HUD
		carte.update(zone.getPlayer().getPosition(), zone.getCoor(), map);
		miniMap.update(zone, zone.getVector(), zone.getStation());
		reput.update(zone.getVector().get(0).getPosition(), state, textureManager);
		stateVessel.Update(zone.getVector().get(0), textureManager);
		//Mise a jour des coordonnees
		zone.updateExit(zone.getPlayer(), map, state);
		//Mise a jour de la map de l'univers 
		map.update(time, state, zone.getCoor().toStrings());

		if (vesselCreation.update(lastFrameTime, new Vec2f(camera.position.x, camera.position.y), new Vec2f(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), zone.getPlayer(), player))
			stateVessel = new VesselState(zone.getVector().get(0), textureManager);

		// Affichage		
		camera.position.set(zone.getPlayer().getPosition().x, zone.getPlayer().getPosition().y, 0);
		camera.update();
		display.setProjectionMatrix(camera.combined);

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		display.begin();
		camera.rotate((-zone.getVector().get(0).getAngle() - cameraAngle) * lastFrameTime);
		cameraAngle += (-zone.getVector().get(0).getAngle() - cameraAngle) * lastFrameTime;

		/*while (cameraAngle < 0 || cameraAngle > 360)
		{
			if (cameraAngle > 360)
			{
				camera.rotate(-360);
				cameraAngle -= 360;
			}
			else if (cameraAngle < 0)
			{
				camera.rotate(360);
				cameraAngle += 360;
			}
		}*/
		camera.update();
		display.setProjectionMatrix(camera.combined);
		zone.draw(display);

		camera.rotate(-cameraAngle);
		camera.update();
		display.setProjectionMatrix(camera.combined);

		carte.draw(display);
		miniMap.draw(display);
		reput.draw(display);
		stateVessel.draw(display);
		vesselCreation.draw(display);

		camera.rotate(+cameraAngle);
		camera.update();
		display.setProjectionMatrix(camera.combined);

		display.end();


		//System.out.println(1 / lastFrameTime);
	}

	@Override
	public void pause() // Quand le jeu est en pause sur Android, ou quand on quitte
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
