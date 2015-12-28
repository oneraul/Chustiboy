package chustiboy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.math.MathUtils;

public class Assets {

	public static Preferences preferences;
	public static Texture[] textures;
	public static Sound[] sounds;
	public static float volume;
	
	public static Texture whitePixel;
	public static Texture greenPixel;
	
	public static void load() {
		
		preferences =  Gdx.app.getPreferences("Chustiboy");
		
		volume = preferences.getFloat("volume", 0.5f);
		
		textures = new Texture[] {
			loadPixel(Color.WHITE),
			loadPixel(Color.GREEN),
			new Texture("assets/boss.png"),
			new Texture("assets/deadboss.png"),
			sueloFuego(),
			new Texture("assets/spritesheet.png")
		};
		
		whitePixel = textures[0];
		greenPixel = textures[1];
		
		sounds = new Sound[] {
			Gdx.audio.newSound(Gdx.files.internal("assets/arrow.wav")),
			Gdx.audio.newSound(Gdx.files.internal("assets/wilhelm.wav"))
		};
	}
	
	public static void dispose() {
		for(Texture texture : textures) {
			texture.dispose();
		}
		for(Sound sound : sounds) {
			sound.dispose();
		}
	}
	
	public static Texture suelo(Color color0, Color color1) {
		short w = 50;
		short h = 50;
		
		Pixmap pix = new Pixmap(w, h, Pixmap.Format.RGB888);
		pix.setColor(color0);
		pix.fill();
		
		pix.setColor(color1);
		for(short x = w; x >= 0; x--) {
			for(short y = h; y >= 0; y--) {
				if(MathUtils.randomBoolean()) {
					pix.drawPixel(x, y);
				}
			}
		}
		
		Texture t = new Texture(pix);
		pix.dispose();
		
		t.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		return t;
	}
	
	private static Texture sueloFuego() {
		short w = 150;
		short h = 50;

		Pixmap pix = new Pixmap(w, h, Pixmap.Format.RGB888);
		pix.setColor(0.9f, 0.1f, 0.1f, 1);
		pix.fill();

		pix.setColor(Color.ORANGE);
		int pixel_size = 2;
		for(short x = 0; x < w; x += pixel_size) {
			for(short y = 0; y < h; y += pixel_size) {
				if(MathUtils.randomBoolean(0.3f)) {
					pix.drawRectangle(x, y, pixel_size, pixel_size);
				}
			}
		}
		

		Texture texture = new Texture(pix);
		pix.dispose();

		texture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		return texture;
	}
	
	private static Texture loadPixel(Color color) {
		Pixmap pix = new Pixmap(1, 1, Format.RGB888);
		pix.setColor(color);
		pix.fill();
		Texture t = new Texture(pix);
		pix.dispose();
		return t;
	}
}
