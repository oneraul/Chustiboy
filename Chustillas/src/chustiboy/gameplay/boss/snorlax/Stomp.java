package chustiboy.gameplay.boss.snorlax;

import chustiboy.Partida;
import chustiboy.gameplay.Chustilla;
import chustiboy.gameplay.ParticleSystem;
import chustiboy.gameplay.Rectangle;
import chustiboy.gameplay.ScreenShaker;
import chustiboy.net.Network;
import chustiboy.net.packets.Packet_pj_hit;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import chustiboy.gameplay.Dibujable;

public class Stomp implements Dibujable {

	ParticleSystem particleSystem;
	Rectangle collider;
	boolean active;
	
	Stomp(int width) {
		active = false;

		particleSystem = new ParticleSystem.Builder(0, 0, width, 5)
			.max_particles(300).emission_rate(25).initial_particle_size(8f).build();
		
		collider = new Rectangle(0, 0, width, 40);
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		if(active) {
			particleSystem.draw(batch);
		}
	}
	
	@Override
	public float getDrawZ() {
		return collider.pos.y-10;
	}
	
	public void init(float x, float y) {
		collider.setPosition(x, y);
		particleSystem.setPosition(x, y);
		
		new Thread() {
			public void run() {
				try {
					Thread.sleep(300);
				} catch(InterruptedException e) {
					System.out.println("stomp#init()");
					e.printStackTrace();
				}
				
				active = true;
				ScreenShaker.shake();
			}
		}.start();
	}
	
	public void stop() {
		active = false;
		particleSystem.resetParticles();
	}

	// TODO only called by the server
	public void update(Network net) {
		if(active) {
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
}
