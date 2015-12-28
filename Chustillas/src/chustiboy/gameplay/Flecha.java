package chustiboy.gameplay;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import chustiboy.GameOptions;

public class Flecha implements Dibujable {
	
	public static Array<Flecha> pool = new Array<Flecha>();
	
	private int w = 2, h = 20;
	private float v;
	private ESPRAIT sprite;
	Vector2 pos, dir, local_pos;
	public Point collider;
	public boolean dead;
	
	Flecha() {
		pos = new Vector2();
		dir = new Vector2();
		local_pos = new Vector2();
		v = 6f;
		dead = true;
		sprite = new ESPRAIT(w, h);
		collider = new Point(pos);
	}
	
	public void init(Vector2 pos, Vector2 dir) {
		dead = false;
		
		this.pos.set(pos);
		this.local_pos.set(0, 0);
		
		this.dir.set(dir);
		float angle = dir.angle() + 90;
		sprite.setRotation(angle);

		this.dir.scl(v);

		collider.pos.set(pos);
		collider.offset.set(0, 0);
	}
	
	public void update() {
		move(dir.x, dir.y);
	}
	
	@Override
	public float getDrawZ() {
		return collider.pos.y + collider.offset.y;
	}

	@Override
	public void draw(SpriteBatch batch) {
		if(!dead) {
			sprite.draw(batch);
			
			if(GameOptions.debug) {
				collider.debug(batch);
			}
		}
	}
	
	public static void fillPool(int chustillas) {
		int flechas_por_pj = 10;
		
		pool.clear();
		for(int i = chustillas * flechas_por_pj; i > 0; i--) {
			pool.add(new Flecha());
		}
	}
	
	public void clavarse(Collider parent_collider) {
		parent_collider.flechas.add(this);
		local_pos.set(pos).sub(parent_collider.pos);
		collider.offset.sub(dir.scl(3));
		dir.set(0, 0);
		collider.setPosition(pos.x, pos.y);
	}
	
	public void move(float x, float y) {
		this.setPosition(pos.x + x, pos.y + y);
	}

	public void setPosition(float x, float y) {
		pos.set(x, y);
		collider.setPosition(x, y);
		sprite.setPosition(x, y);
	}
}
