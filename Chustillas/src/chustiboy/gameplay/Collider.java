package chustiboy.gameplay;

import chustiboy.Partida;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public abstract class Collider {
	public Vector2 pos;
	protected Vector2 offset;
	public Array<Flecha> flechas;
	
	Collider(Vector2 pos, Vector2 offset) {
		this.offset = new Vector2(offset);
		this.pos = new Vector2(pos).add(offset);
		flechas = new Array<Flecha>();
	}
	
	Collider(Vector2 pos) {
		this(pos, new Vector2());
	}
	
	public void setPosition(Vector2 newPos) {
		this.pos.set(newPos).add(offset);
		
		for(Flecha flecha : flechas) {
			flecha.setPosition(pos.x, pos.y);
		}
	}
	
	public void setPosition(float x, float y) {
		this.pos.set(x, y).add(offset);
		
		for(Flecha flecha : flechas) {
			flecha.setPosition(pos.x, pos.y);
		}
	}
	
	public abstract boolean collide(Collider shape);
	
	public void recoger_flechas() {
		for(int i = flechas.size-1; i >= 0; i--) {
			Flecha flecha = flechas.get(i);

			Chustilla chustilla;
			for(int pj = Partida.chustillas.size-1; pj >= 0; pj--) {
				chustilla = Partida.chustillas.get(pj);

				if(flecha.collider.collide(chustilla.collider)) {
					flechas.removeIndex(i);
					Flecha.pool.add(flecha);
					flecha.tapada = false;
					flecha.dead = true;
					break;
				}
			}
		}
	}
	
	public abstract void debug(SpriteBatch batch);
}
