package chustiboy.gameplay.boss.snorlax;

import chustiboy.Assets;
import chustiboy.Partida;
import chustiboy.gameplay.AnimatedSprite;
import chustiboy.gameplay.Chustilla;
import chustiboy.gameplay.ParticleSystem;
import chustiboy.gameplay.Rectangle;
import chustiboy.gameplay.Pool.Poolable;
import chustiboy.gameplay.Dibujable;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Charco implements Dibujable, Poolable {
	
	Rectangle collider;
	AnimatedSprite sprite;
	ParticleSystem particleSystem;
	boolean drawing = false, activated;
	private float a;
	
	public void init(float x, float y, int width, int height) {
		collider = new Rectangle(x, y, width, height);
		
		particleSystem = new ParticleSystem.Builder(x, y, width, height)
			.max_particles(width * 2).initial_particle_size(4f).build();
		
		sprite = new AnimatedSprite(Assets.textures[4], width, height, 3, 0.10f);
		sprite.setPosition(x, y);
		sprite.setAlpha(0);
		
		drawing = true;
		activated = false;
	}
	
	@Override
	public float getDrawZ() {
		return 999999;
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		if(drawing) {
			sprite.draw(batch);
			particleSystem.draw(batch);
		}
	}
	
	public void update() {
		if(!activated) {
			sprite.setAlpha(a);
			
			a += 0.02f;
			if(a >= 1f)
				activated = true;
		}
		
		for(Chustilla chustilla : Partida.chustillas) {
			if(collider.collide(chustilla.collider)) {
				chustilla.hit();
			}
		}
	}

	@Override
	public void reset() {
		activated = false;
		drawing = false;
		a = 0;
	}
}
