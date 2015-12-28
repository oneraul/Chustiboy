package chustiboy;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglPreferences;

public class Chustiboy {
	public static void main(String[] args) {
    	LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
    	
    	cfg.title = "Chustiboy";
    	cfg.resizable = false;
    	
    	LwjglPreferences prefs = new LwjglPreferences("Chustiboy", cfg.preferencesDirectory);
    	cfg.width = prefs.getInteger("width", 800);
    	cfg.height = prefs.getInteger("height", 600);
    	cfg.fullscreen = prefs.getBoolean("fullscreen");
    	
    	new LwjglApplication(new TheGAME(), cfg);
    }
}
