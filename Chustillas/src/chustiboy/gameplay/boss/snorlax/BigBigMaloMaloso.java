package chustiboy.gameplay.boss.snorlax;

import chustiboy.Partida;
import chustiboy.gameplay.Boss;
import chustiboy.gameplay.Chustilla;
import chustiboy.gameplay.Collider;
import chustiboy.gameplay.ESPRAIT;
import chustiboy.gameplay.Flecha;
import chustiboy.gameplay.Pool;
import chustiboy.gameplay.Rectangle;
import chustiboy.gameplay.ScreenShaker;
import chustiboy.gameplay.Dibujable;
import chustiboy.net.Network;
import chustiboy.net.NetworkHost;
import chustiboy.net.packets.boss.Packet_boss_dead;
import chustiboy.net.packets.boss.snorlax.Packet_boss_init_casita;
import chustiboy.net.packets.boss.snorlax.Packet_boss_spawn_firePuddle;
import chustiboy.net.packets.boss.snorlax.Packet_boss_spawn_fireball;
import chustiboy.net.packets.boss.snorlax.Packet_boss_stomp;
import chustiboy.net.packets.boss.snorlax.Packet_boss_stomp_stop;
import chustiboy.net.packets.boss.snorlax.Packet_boss_tron;
import chustiboy.Assets;
import chustiboy.GameOptions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class BigBigMaloMaloso extends Boss {
	
	private int size = 150;
	Stomp stomp;
	Tron[] trons;
	Pool<Charco> charcos;
	Pool<Fireball> fireballs;
	Casita casita;
	Network net;
	boolean dead;
	
	public Color stageColor;
	
	// IA & blackboard
	private IA ia;
	int hp;
	float phase_timer;
	boolean charging;
	Chustilla target;

	public BigBigMaloMaloso(Network net) {
		if(net instanceof NetworkHost) {
			this.net = net;
			ia = new IA(this);
		}
		
		stageColor = new Color(Color.WHITE);
		sprite = new ESPRAIT(Assets.textures[2], size, size);
		dead = false;
		
		// colliders

		Rectangle pataDerecha   = new Rectangle(pos, 25, 20,  37, 5);
		Rectangle pataIzquierda = new Rectangle(pos, 25, 20, -43, 5);
		Rectangle cuerpoAncho   = new Rectangle(pos, (int)(size * 0.9f), (int)(size * 0.4f), 0, (int)(size * 0.2f));
		Rectangle cuerpoAlto    = new Rectangle(pos, (int)(size * 0.5f), (int)(size * 0.8f), 0, (int)(size * 0.1f));
		
		movementColliders.add(pataDerecha);
		movementColliders.add(pataIzquierda);
		
		hitColliders.addAll(movementColliders);
		hitColliders.add(cuerpoAlto);
		hitColliders.add(cuerpoAncho);
		
		
		// ataques
		
		stomp = new Stomp(size);
		casita = new Casita();
		
		trons = new Tron[Partida.chustillas.size];
		for(short i = 0; i < trons.length; i++)
			trons[i] = new Tron(i);
		
		fireballs = new Pool<Fireball>(100, false) {
			@Override
			public Fireball newItem() {
				return new Fireball();
			}
		};
		Fireball.fireballs = fireballs;
		
		charcos = new Pool<Charco>(8, true) {
			@Override
			public Charco newItem() {
				return new Charco();
			}
		};
		
		String vertexShader   = Gdx.files.internal("assets/passthrough.vert").readString();
		String fragmentShader = Gdx.files.internal("assets/lava.frag").readString();
		Charco.shaderLava = new ShaderProgram(vertexShader, fragmentShader);
		if(!Charco.shaderLava.isCompiled())
      		throw new GdxRuntimeException(Charco.shaderLava.getLog());
      	if(Charco.shaderLava.getLog().length() != 0)
      		System.out.println(Charco.shaderLava.getLog());
      	Charco.shaderLava.begin();
      	Charco.shaderLava.setUniformi("u_noise", 1);
      	Charco.shaderLava.end();
      	Charco.shaderDisplacement = Assets.textures[8];
	}

	@Override
	public void addDibujablesSueloToList(Array<Dibujable> list) {
		for(Charco charco : charcos.pool) list.add(charco);
		list.add(casita);
	}
	
	@Override
	public void addDibujablesToList(Array<Dibujable> list) {
		list.add(this);
		list.add(stomp);
		for(Tron tron : trons) tron.addDibujablesToList(list);
		for(Fireball fireball : fireballs.pool) list.add(fireball);
	}
	
	@Override
	public void die() {
		sprite.setTexture(Assets.textures[3]);
		
		for(Collider collider : hitColliders) {
			for(Flecha flecha : collider.flechas) {
				flecha.dead = true;
				flecha.tapada = false;
			}
			Flecha.pool.addAll(collider.flechas);
		}
		hitColliders.clear();
		hitColliders.add(new Rectangle(pos, size, size));
		dead = true;
		
		if(ia != null) {
			Packet_boss_dead p = new Packet_boss_dead();
			p.boss_id = this.id;
			net.sendTCP(p);
		}
	}
	
	@Override
	public void attack() {}
	
	@Override
	public void update() {
		
		for(Collider collider : hitColliders) {
			collider.recoger_flechas();
		}

		for(short pj = 0; pj < Partida.chustillas.size; pj++) {
			Chustilla chustilla = Partida.chustillas.get(pj);
			if(chustilla.flechas.size == 0) continue;
			
			for(int c = this.hitColliders.size-1; c >= 0; c--) {
				Collider collider = this.hitColliders.get(c);
				
				Flecha flecha;
				for(int f = chustilla.flechas.size-1; f >= 0; f--) {
					flecha = chustilla.flechas.get(f);
    			
	    			if(collider.collide(flecha.collider)) {
						chustilla.flechas.removeIndex(f);
						flecha.clavarse(collider);
						
						if(!dead) {
							if(ia != null) {
								ia.hit(pj);
							}
							
							// to avoid checking for collisions if dead
							if(dead) {
								return;
							}
						}
	    			}
    			}
	    	}
		}
		
		for(Fireball fireball : fireballs.active) fireball.update();
		for(Charco charco : charcos.active) charco.update();
		for(Tron tron : trons) tron.update();
		casita.update();
		
		if(!dead && ia != null) {
			ia.update();
			stomp.update(net);
		}
	}
	
	@Override
	public float getDrawZ() {
		return pos.y;
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		sprite.draw(batch);
		
		if(GameOptions.debug) {
			for(Collider collider : hitColliders) {
				collider.debug(batch);
			}
		}
	}
	
	// ATTACK -----------
	
	public void stomp(float x, float y) {
		if(stomp.active) return;
		
		ScreenShaker.shake();
		stomp.init(x, y);
		
		if(ia != null) {
			Packet_boss_stomp p = new Packet_boss_stomp();
			p.boss_id = this.id;
			p.x = pos.x;
			p.y = pos.y;
			net.sendTCP(p);
		}
	}
	
	public void stomp_stop() {
		stomp.stop();
		
		if(ia != null) {
			Packet_boss_stomp_stop p = new Packet_boss_stomp_stop();
			p.boss_id = this.id;
			net.sendTCP(p);
		}
	}
	
	public void shootFireball(Vector2 pos, Vector2 dir) {
		if(fireballs.isPoolEmpty()) return;
		
		fireballs.obtain().init(pos, dir);
		
		if(ia != null) {
			Packet_boss_spawn_fireball p = new Packet_boss_spawn_fireball();
			p.boss_id = this.id;
			p.x = pos.x;
			p.y = pos.y;
			p.dir_x = dir.x;
			p.dir_y = dir.y;
			net.sendTCP(p);
		}
	}
	
	public void spawnFirePuddle(float x, float y, int width, int height) {
		
		charcos.obtain().init(x, y, width, height);
		
		if(ia != null) {
			Packet_boss_spawn_firePuddle p = new Packet_boss_spawn_firePuddle();
			p.boss_id = this.id;
			p.x = x;
			p.y = y;
			p.width = width;
			p.height = height;
			net.sendTCP(p);
		}
	}
	
	public void startTron() {
		for(Tron tron : trons) {
			tron.start();
		}
		
		if(ia != null) {
			Packet_boss_tron p = new Packet_boss_tron();
			p.boss_id = this.id;
			net.sendTCP(p);
		}
	}
	
	public void startCasita(float x, float y) {
		if(!casita.active) {
			charcos.freeAll();
			casita.init(x, y);
			
			if(ia != null) {
				Packet_boss_init_casita p = new Packet_boss_init_casita();
				p.boss_id = this.id;
				p.x = x;
				p.y = y;
				net.sendTCP(p);
			}
		}
	}
	
	public void setFireColor(Color...colors) {
		stomp.particleSystem.colors = colors;
		for(Charco charco : charcos.active)
			charco.particleSystem.colors = colors;
		for(Fireball fireball : fireballs.active)
			fireball.particleSystem.colors = colors;
		for(Fireball fireball : fireballs.pool)
			fireball.particleSystem.colors = colors;
		for(Tron tron : trons) {
			for(TronPosition tronPosition : tron.posiciones.active)
				tronPosition.particleSystem.colors = colors;
			for(TronPosition tronPosition : tron.posiciones.pool)
				tronPosition.particleSystem.colors = colors;
		}
			
	}
}