package chustiboy.gameplay;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import chustiboy.Assets;

public class Point extends Collider {
	public Point(Vector2 pos) {
		super(pos);
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
		 batch.draw(Assets.greenPixel, pos.x-2f, pos.y, 4f, 4f);
	}
}
