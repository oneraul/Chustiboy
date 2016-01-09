package chustiboy.gameplay;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import chustiboy.Assets;
import chustiboy.GameOptions;
import chustiboy.Partida;

public class Flecha implements Dibujable {
	
	public static Array<Flecha> pool = new Array<Flecha>();
	public static Array<Flecha> all  = new Array<Flecha>();
	
	private float v, scale, z_tapada;
	Vector2 pos, dir;
	public Point collider;
	public boolean dead, clavada, tapada;
	FlechaAnimationData animationData;
	AnimatedSprite animation;
	
	Flecha() {
		pos = new Vector2();
		dir = new Vector2();
		v = 5f;
		scale = 2f;
		dead = true;
		collider = new Point(pos);
		
		animationData = new FlechaAnimationData();
		animation = new AnimatedSprite(Assets.textures[7]);
		animation.setFrames(animationData.getFrames());
	}
	
	public void init(Vector2 pos, Vector2 dir, byte animationDirection) {
		dead = false;
		clavada = false;
		tapada = false;
		
		this.pos.set(pos);
		this.dir.set(dir).scl(v);
		
		collider.pos.set(pos);
		collider.offset.set(0, 0);
		
		animationData.setDirection(animationDirection);
		animation.setFrames(animationData.getFrames());
	}
	
	public void update() {
		move(dir.x, dir.y);
		
		tapada = false;
		for(Muro muro : Partida.muros) {
			if(checkTapada(muro.collider_tapar)) break;
		}
	}
	
	@Override
	public float getDrawZ() {
		if(!tapada) return collider.pos.y;
		else 		return z_tapada;
	}

	@Override
	public void draw(SpriteBatch batch) {
		if(!dead) {
			
			if(!clavada) animation.animate();
			animation.draw(batch, pos.x, pos.y, scale);
			
			if(GameOptions.debug) {
				collider.debug(batch);
			}
		}
	}
	
	public static void fillPool(int chustillas) {
		int flechas_por_pj = 10;
		
		all.clear();
		pool.clear();
		for(int i = chustillas * flechas_por_pj; i > 0; i--) {
			pool.add(new Flecha());
		}
		all.addAll(pool);
	}
	
	public boolean checkTapada(Collider collider_tapar) {
		if(collider.collide(collider_tapar)) {
			tapada = true;
			z_tapada = collider_tapar.pos.y-1;
			return true;
		}
		
		return false;
	}
	
	public void clavarse(Collider parent_collider) {
		clavada = true;
		parent_collider.flechas.add(this);
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
	}
}
