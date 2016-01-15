package fr.spaceproject.utils;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class SettingsFile
{
	protected String fileName;
	protected FileHandle file;
	protected Map<String, String> values;
	
	public SettingsFile(String fileName)
	{
		// fileName : nom du fichier à ouvrir
		
		this.fileName = fileName;
		this.file = Gdx.files.external(fileName);
		values = new LinkedHashMap<String, String>();
	}
	
	public String getValue(String parameterName)
	{
		if (values.containsKey(parameterName))
			return values.get(parameterName);
		return "";
	}
	
	public boolean parameterIsExisting(String parameterName)
	{
		return values.containsKey(parameterName);
	}
	
	public void setValue(String parameterName, String value)
	{
		values.put(parameterName, value);
	}
	
	public void removeValue(String parameterName)
	{
		values.remove(parameterName);
	}
	
	public void load()
	{
		// Chargement des informations du fichier texte	
		
		values.clear();
		
		String data = file.readString();
		boolean firstPartIsCompleted = false;
		String firstPart = "";
		String secondPart = "";
		for (int i = 0; i < data.length(); ++i)
		{
			if (data.charAt(i) == '=')
			{
				firstPartIsCompleted = true;
			}
			else if (data.charAt(i) == '\n')
			{
				values.put(firstPart, secondPart);
				
				firstPartIsCompleted = false;
				firstPart = "";
				secondPart = "";
			}
			else
			{
				if (firstPartIsCompleted)
					secondPart += data.charAt(i);
				else
					firstPart += data.charAt(i);
			}
		}
	}
	
	public void save()
	{
		// Sauvegarde des informations dans le fichier texte
		
		String data = "";
		Iterator<Entry<String, String>> it = values.entrySet().iterator();
		
		while (it.hasNext())
		{
			Entry<String, String> value = it.next();
			data += value.getKey() + "=" + value.getValue() + "\n";
		}
		
		file.writeString(data, false);
	}
}
