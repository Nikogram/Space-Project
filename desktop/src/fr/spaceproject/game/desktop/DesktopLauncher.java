package fr.spaceproject.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import fr.spaceproject.game.Game;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.samples = 4; 
		new LwjglApplication(new Game(), config);
		System.out.println((double)(3.000000 * 10e37 + 100000));
	}
}
