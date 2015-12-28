package chustiboy.gameplay;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Circle extends Collider {
	float r;
	
	Circle(Vector2 pos, float r) {
		super(pos);
		this.r = r;
	}
	
	@Override
	public boolean collide(Collider shape) {
		if(shape instanceof Point)
			return Colisionador.colision((Point)shape, this);
		else if(shape instanceof Rectangle)
			return Colisionador.colision((Rectangle)shape, this);
		else if(shape instanceof Circle)
			return Colisionador.colision(this, (Circle)shape);
		
		return false;
	}

	@Override
	public void debug(SpriteBatch batch) {
		// TODO
	}
}
