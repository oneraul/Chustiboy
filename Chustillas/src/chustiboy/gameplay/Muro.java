package chustiboy.gameplay;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import chustiboy.Assets;
import chustiboy.GameOptions;

public class Muro implements Dibujable {
	
	private float x, y;
	private int width, height, z;
	public Rectangle collider;
	private TextureRegion textureRegion;
	private Color color_front, color_top;
	
	public Muro(float x, float y, int width, int height) {
		this(x, y, width, height, Color.WHITE);
	}
	
	public Muro(float x, float y, int width, int height, Color color) {
		this.x           = x;
		this.y           = y;
		this.width       = width;
		this.height      = height;
		this.z           = 50;
		this.color_front = new Color(color);
		this.color_top   = new Color(color.r*0.4f, color.g*0.4f, color.b*0.4f, 1);
		
		collider = new Rectangle(new Vector2(x, y), width, height);
		
		textureRegion = new TextureRegion(Assets.textures[6]);
	}
	
	/*
	 * TODO
	 * si el muro es el que se mueve pero no el pj, la colision no funciona bien
	 */
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
		collider.setPosition(x, y);
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
		textureRegion.setRegionWidth(width);
		textureRegion.setRegionHeight(z);
		batch.draw(textureRegion, x-width/2, y);
		//top
		batch.setColor(color_top);
		textureRegion.setRegionHeight(height);
		batch.draw(textureRegion, x-width/2, y+z);
		batch.setColor(1, 1, 1, 1);
		
		if(GameOptions.debug) {
			collider.debug(batch);
		}
	}
	
	public float x() {
		return x;
	}
	
	public float y() {
		return y;
	}
	
	public int width() {
		return width;
	}
	
	public int height() {
		return height;
	}
}

