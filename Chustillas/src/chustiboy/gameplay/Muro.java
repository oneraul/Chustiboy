package chustiboy.gameplay;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import chustiboy.Assets;
import chustiboy.GameOptions;

public class Muro implements Dibujable {
	
	float x, y;
	int w, h, z;
	public Rectangle collider;
	
	public Muro(float x, float y, int width, int height) {
		this.x = x;
		this.y = y;
		this.w = width;
		this.h = height;
		this.z = 50;
		
		collider = new Rectangle(new Vector2(x, y), w, h);
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
		
		batch.setColor(1, 0.5f, 0.2f, 1);
		batch.draw(Assets.whitePixel, x-w/2, y, w, z);
		batch.setColor(0.2f, 0.5f, 1, 1);
		batch.draw(Assets.whitePixel, x-w/2, y+z, w, h);
		batch.setColor(1, 1, 1, 1);
		
		//for(Flecha flecha : collider.flechas) {
		//	flecha.draw(batch);
		//}
		
		if(GameOptions.debug) {
			collider.debug(batch);
		}
	}
}

