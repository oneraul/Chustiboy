package chustiboy.gameplay;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import chustiboy.Assets;

public class Rectangle extends Collider {
	public int w, h;
	
	/*		 _____
	 * 		|	  |		o => pos;
	 * 		|     |		w = width / 2;
	 * 		|__o__|		h = height;
	 */
	
	public Rectangle(Vector2 pos, int width, int height, int padding_x, int padding_y) {
		super(pos, new Vector2(padding_x, padding_y));
		this.w = width / 2;
		this.h = height;
	}
	
	public Rectangle(Vector2 pos, int width, int height) {
		this(pos, width, height, 0, 0);
	}
	
	public Rectangle(float x, float y, int width, int height) {
		this(new Vector2(x, y), width, height, 0, 0);
	}
	
	@Override
	public boolean collide(Collider shape) {
		if(shape instanceof Point)
			return Colisionador.colision((Point)shape, this);
		else if(shape instanceof Rectangle)
			return Colisionador.colision(this, (Rectangle)shape);
		else if(shape instanceof Circle)
			return Colisionador.colision(this, (Circle)shape);
		
		return false;
	}

	@Override
	public void debug(SpriteBatch batch) {
		batch.draw(Assets.greenPixel, pos.x-w, pos.y, w*2, h);
	}
}
