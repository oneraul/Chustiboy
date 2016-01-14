package chustiboy.gameplay;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public abstract class Boss implements Dibujable {
	public short id;
	protected Array<Collider> hitColliders;
	public Array<Rectangle> movementColliders;
	protected ESPRAIT sprite;
	protected Vector2 pos;
	private Vector2 tmpPos;
	
	public abstract void addDibujablesToList(Array<Dibujable> list);
	public abstract void addDibujablesSueloToList(Array<Dibujable> list);
	public abstract void update();
	public abstract void die();
	public abstract void attack();
	
	public Boss() {
		hitColliders = new Array<Collider>();
		movementColliders = new Array<Rectangle>();
		pos = new Vector2();
		tmpPos = new Vector2();
	}
	
	@Override
	public abstract void draw(SpriteBatch batch);
	
	public Vector2 getPosition() {
		return tmpPos.set(pos);
	}
	
	public void setPosition(float x, float y) {
		pos.set(x, y);
		sprite.setPosition(pos.x, pos.y);

		for(Collider collider : hitColliders) {
			collider.setPosition(pos);
		}
		
		for(Collider collider : movementColliders) {
			collider.setPosition(pos);
		}
	}
	
	public void move(float x, float y) {
		setPosition(pos.x + x, pos.y + y);
	}
}
