package chustiboy.gameplay.boss;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import chustiboy.gameplay.ESPRAIT;
import chustiboy.gameplay.Collider;
import chustiboy.gameplay.Dibujable;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public abstract class Boss implements Dibujable {
	public short id;
	protected Array<Collider> colliders;
	protected ESPRAIT sprite;
	protected Vector2 pos;
	private Vector2 tmp = new Vector2();
	
	public abstract void addDibujablesToList(Array<Dibujable> list);
	public abstract void update();
	public abstract void die();
	public abstract void attack();
	
	@Override
	public abstract void draw(SpriteBatch batch);
	
	public Vector2 getPosition() {
		return tmp.set(pos);
	}
	
	public void setPosition(float x, float y) {
		pos.set(x, y);
		sprite.setPosition(pos.x, pos.y);

		for(Collider collider : colliders) {
			collider.setPosition(pos);
		}
	}
	
	public void move(float x, float y) {
		setPosition(pos.x + x, pos.y + y);
	}
}
