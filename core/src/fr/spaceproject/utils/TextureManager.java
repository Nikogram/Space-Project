package fr.spaceproject.utils;

import java.util.LinkedHashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;


public class TextureManager {
	private Map<String, Texture> textures;


	public TextureManager() {
		textures = new LinkedHashMap<String, Texture>();
	}

	public void addTexture(String name, String textureFileName) {
		textures.put(name, new Texture(Gdx.files.internal(textureFileName)));
		textures.get(name).setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}

	public Texture getTexture(String name) {
		return textures.get(name);
	}
}
