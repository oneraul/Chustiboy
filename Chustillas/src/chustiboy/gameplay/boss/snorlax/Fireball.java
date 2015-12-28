package chustiboy.gameplay.boss.snorlax;

import chustiboy.GameOptions;
import chustiboy.Partida;
import chustiboy.gameplay.ParticleSystem;
import chustiboy.gameplay.Pool;
import chustiboy.gameplay.Pool.Poolable;
import chustiboy.gameplay.Rectangle;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import chustiboy.gameplay.Chustilla;
import chustiboy.gameplay.Dibujable;
import chustiboy.gameplay.Muro;

public class Fireball implements Dibujable, Poolable {
	
	public static Pool<Fireball> fireballs;
	
	private ParticleSystem particleSystem;
	Rectangle collider;
	private Vector2 pos, dir;
	private float v;
	boolean active;
	
	Fireball() {
		pos = new Vector2();
		dir = new Vector2();
		v = 6f;
		active = false;
		
		int width = 5;
		int height = 3;
		collider = new Rectangle(pos, width, height);
		particleSystem = new ParticleSystem.Builder(pos.x, pos.y, width, height)
			.max_particles(90).initial_particle_size(10f).build();
	}
	
	public void init(Vector2 pos, Vector2 dir) {
		this.pos.set(pos);
		this.dir.set(dir).scl(v); // dir has to be normalized!

		collider.pos.set(pos);
		particleSystem.setPosition(pos.x, pos.y);
		particleSystem.resetParticles();
		active = true;
	}
	
	@Override
	public float getDrawZ() {
		return pos.y;
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		if(active) {
			particleSystem.draw(batch);
			
			if(GameOptions.debug) {
				collider.debug(batch);
			}
		}
	}
	
	public void update() {
		pos.add(dir);
		collider.setPosition(pos);
		particleSystem.setPosition(pos.x, pos.y);
		
		boolean collided = false;
		
		for(Chustilla chustilla : Partida.chustillas) {
			if(collider.collide(chustilla.collider)) {
				chustilla.hit();
				collided = true;
				
				break;
			}
		}
		if(!collided) {
			for(Muro muro : Partida.muros) {
				if(collider.collide(muro.collider)) {
					collided = true;
					break;
				}
			}
		}
		
		if(collided) {
			fireballs.free(this);
		}
	}

	@Override
	public void reset() {
		active = false;
	}
}
