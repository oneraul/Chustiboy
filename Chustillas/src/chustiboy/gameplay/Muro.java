package chustiboy.gameplay;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import chustiboy.Assets;
import chustiboy.GameOptions;

public class Muro implements Dibujable {
	
	float x, y;
	int w, h, z;
	public Rectangle collider;
	private TextureRegion textureRegion;
	private Color color_front, color_top;
	
	public Muro(float x, float y, int width, int height) {
		this(x, y, width, height, Color.WHITE);
	}
	
	public Muro(float x, float y, int width, int height, Color color) {
		this.x = x;
		this.y = y;
		this.w = width;
		this.h = height;
		this.z = 50;
		this.color_front = new Color(color);
		this.color_top = new Color(color.r*0.4f, color.g*0.4f, color.b*0.4f, 1);
		
		collider = new Rectangle(new Vector2(x, y), w, h);
		
		textureRegion = new TextureRegion(Assets.textures[6]);
	}
	
	public void update() {
		collider.recoger_flechas();
	}
	
	@Override
	public float getDrawZ() {
		return y;
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		// front
		batch.setColor(color_front);
		textureRegion.setRegionWidth(w);
		textureRegion.setRegionHeight(z);
		batch.draw(textureRegion, x-w/2, y);
		//top
		batch.setColor(color_top);
		textureRegion.setRegionHeight(h);
		batch.draw(textureRegion, x-w/2, y+z);
		batch.setColor(1, 1, 1, 1);
		
		if(GameOptions.debug) {
			collider.debug(batch);
		}
	}
}

