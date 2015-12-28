package chustiboy.gameplay.boss.snorlax;

import chustiboy.GameOptions;
import chustiboy.Partida;
import chustiboy.gameplay.Chustilla;
import chustiboy.gameplay.ParticleSystem;
import chustiboy.gameplay.Rectangle;
import chustiboy.net.Network;
import chustiboy.net.packets.Packet_pj_hit;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import chustiboy.gameplay.Dibujable;

public class Stomp implements Dibujable {

	private ParticleSystem particleSystem;
	Rectangle collider;
	boolean active;
	
	Stomp(int width) {
		active = false;

		particleSystem = new ParticleSystem.Builder(0, 0, width, 5)
			.max_particles(600).initial_particle_size(10f).build();
		
		collider = new Rectangle(0, 0, width, 100);
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
	
	@Override
	public float getDrawZ() {
		return collider.pos.y;
	}
	
	public void init(float x, float y) {
		collider.setPosition(x, y);
		particleSystem.setPosition(x, y);
		active = true;
	}
	
	public void stop() {
		active = false;
		particleSystem.resetParticles();
	}

	// only called by the server
	public void update(Network net) {
		if(!active) return;
		
		for(byte pj = 0; pj < Partida.chustillas.size; pj++) {
			Chustilla chustilla = Partida.chustillas.get(pj);
			if(collider.collide(chustilla.collider)) {
				chustilla.hit();
			
				Packet_pj_hit p = new Packet_pj_hit();
				p.pj_id = pj;
				net.sendTCP(p);
	 	  	}
		}
	}
}
