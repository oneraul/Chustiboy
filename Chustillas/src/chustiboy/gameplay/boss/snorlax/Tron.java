package chustiboy.gameplay.boss.snorlax;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import chustiboy.Partida;
import chustiboy.GameOptions;
import chustiboy.gameplay.Chustilla;
import chustiboy.gameplay.ParticleSystem;
import chustiboy.gameplay.Pool;
import chustiboy.gameplay.Pool.Poolable;
import chustiboy.gameplay.Dibujable;
import chustiboy.gameplay.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Tron {
	private Pool<TronPosition> posiciones;
	private float timer, init_time;
	private Vector2 nextPos;
	private short pj;
	private boolean active;
	private int counter;
	
	Tron(short pj) {
		this.pj = pj;
		init_time = 0.2f;
		active = false;
		nextPos = new Vector2();
		
		int size_of_the_tron = 15;
		posiciones = new Pool<TronPosition>(size_of_the_tron, false) {
			@Override
			public TronPosition newItem() {
				return new TronPosition();
			}
		};
	}
	
	public void addDibujablesToList(Array<Dibujable> list) {
		for(TronPosition t : posiciones.pool)
			list.add(t);
	}
	
	public void update() {
		if(active) {
			float dt = Gdx.graphics.getDeltaTime();
			if(counter > 0 && (timer -= dt) <= 0) addPosition();
			
			for(int i = posiciones.active.size-1; i >= 0; i--) {
				TronPosition t = posiciones.active.get(i);
				if(t.update(dt))
					posiciones.free(t);
			}
			
			if(counter == 0 && posiciones.active.size == 0) {
				active = false;
				posiciones.freeAll();
			}
		}
	}
	
	public void start() {
		if(!active) {
			active = true;
			timer = init_time;
			counter = posiciones.pool.size;
			nextPos.set(Partida.chustillas.get(pj).getPosition());
		}
	}
	
	private void addPosition() {
		counter--;
		timer += init_time;
		posiciones.obtain().init(nextPos);
		nextPos.set(Partida.chustillas.get(pj).getPosition());
	}
}

class TronPosition implements Dibujable, Poolable {
	Vector2 pos;
	float timer;
	ParticleSystem particleSystem;
	Rectangle collider;
	
	private final float init_time = 3f;
	
	TronPosition() {
		pos = new Vector2();
		
		int size = 5;
		collider = new Rectangle(pos, size, size);
		particleSystem = new ParticleSystem.Builder(pos.x, pos.y, size, size)
			.max_particles(60).initial_particle_size(6f).build();
		particleSystem.emiting = false;
	}
	
	public void init(Vector2 pos) {
		this.pos.set(pos);
		particleSystem.resetParticles();
		particleSystem.setPosition(pos.x, pos.y);
		particleSystem.emiting = true;
		collider.setPosition(pos);
		timer = init_time;
	}
	
	boolean update(float dt) {
		timer -= dt;
		
		if(timer <= 0) return true;
		
		for(Chustilla chustilla : Partida.chustillas) {
			 if(collider.collide(chustilla.collider)) {
				chustilla.hit();
				break;
			}
		}
		
		return false;
	}
	
	@Override
	public float getDrawZ() {
		return pos.y;
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		particleSystem.draw(batch);

		if(GameOptions.debug && particleSystem.emiting) {
			collider.debug(batch);
		}
	}

	@Override
	public void reset() {
		particleSystem.emiting = false;
	}
}
