package chustiboy.gameplay;

import chustiboy.Assets;
import chustiboy.GameOptions;
import chustiboy.Partida;
import chustiboy.net.Network;
import chustiboy.net.packets.Packet_position;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Chustilla implements Dibujable {
	
	public Array<Flecha> flechas;
	private Vector2 pos, tmp;
	public Vector2 dir;
	public boolean W, A, S, D, isMoving;
	public byte facing;
	public String name;
	private Color color;
	public Rectangle collider;
	private int hp;
	private TextureRegion region;
	private boolean dead;
	
	private float scale = 0.75f;
	private int size = 15;
	
	private float immunity_after_hit = 0.5f;
	private float immunity_timer, v, animation_timer, animation_frame_duration;
	
	private Sound wilhelmScream, shootSound;

	public Chustilla() {
		v = 1.5f;
		hp = 3;
		dead = false;
		color = new Color(Color.WHITE);
		pos = new Vector2();
		tmp = new Vector2();
		dir = new Vector2(1, 0);
		collider = new Rectangle(pos, size, size);
		flechas = new Array<Flecha>();
		
		move(0, 0); // to set collider and sprite on position
		
		shootSound = Assets.sounds[0];
		wilhelmScream = Assets.sounds[1];
		
		region = new TextureRegion(Assets.textures[5]);
		region.setRegionWidth(Assets.textures[5].getWidth()/6);
		region.setRegionHeight(Assets.textures[5].getHeight()/8);
		face((byte)0);
		animation_timer = animation_frame_duration = 0.15f;
	}

	public void setColor(float r, float g, float b) {
		color.set(r, g, b, 1);
	}
	
	public void update() {
		// resetear el "parpadeo" de despues de recibir un golpe
		if(immunity_timer > 0) {
			immunity_timer -= Gdx.graphics.getDeltaTime();
			if(immunity_timer <= 0) {
				immunity_timer = 0;
			}
		}
		
		// comprueba si las flechas se clavan en algun muro
		if(flechas.size == 0) return;
		
		for(int i = flechas.size-1; i >= 0; i--) {
			Flecha flecha = flechas.get(i);
			
			flecha.update();
			
			for(Muro muro : Partida.muros) {
				if(flecha.collider.collide(muro.collider)) {
					flecha.clavarse(muro.collider);
					flechas.removeIndex(i);
					break;
				}
			}
		}
	}
	
	public void movement(Network net) {
		if(dead) return;
		
		float a = 0, b = 0;

		if(W) b++;
		if(A) a--;
		if(S) b--;
		if(D) a++;

		if(a == 0 && b == 0) {
			isMoving = false;
			
		} else {
		
			isMoving = true;
			dir.set(a, b).nor();
	
			move(dir.x * v, 0);
			for(Muro muro : Partida.muros) {
				if(muro.collider.collide(collider)) {
					if(D) setPosition(muro.x - muro.w/2 - size/2, this.pos.y);
					else if(A) setPosition(muro.x + muro.w/2 + size/2, this.pos.y);
				}
			}
	
	       	move(0, dir.y * v);
			for(Muro muro : Partida.muros) {
				if(muro.collider.collide(collider)) {
					if(W) setPosition(this.pos.x, muro.y - size);
					else if(S) setPosition(this.pos.x, muro.y + muro.h);
				}
			}
			
			face(dir.angle());
		}
		
		//update the network
		Packet_position p = new Packet_position();
    	p.x = pos.x;
    	p.y = pos.y;
    	p.moving = isMoving;
    	p.facing = facing;
    	p.pj_id = Partida.pj_id;
    	net.sendUDP(p);
	}
	
	private void face(float angle) {
		switch((int)angle) {
			case 0: 
				face((byte)0);
				break;
			case 45: 
				face((byte)1);
				break;
			case 90: 
				face((byte)2);
				break;
			case 135: 
				face((byte)3);
				break;
			case 180: 
				face((byte)4);
				break;
			case 225: 
				face((byte)5);
				break;
			case 270: 
				face((byte)6);
				break;
			case 315: 
				face((byte)7);
				break;
		}
	}
	
	public void face(byte direction) {
		facing = direction;
		
		switch(direction) {
			case 0: 
				region.setV(2f/8);
				region.setV2(3f/8);
				break;
			case 1: 
				region.setV(7f/8);
				region.setV2(8f/8);
				break;
			case 2: 
				region.setV(3f/8);
				region.setV2(4f/8);
				break;
			case 3: 
				region.setV(6f/8);
				region.setV2(7f/8);
				break;
			case 4: 
				region.setV(1f/8);
				region.setV2(2f/8);
				break;
			case 5: 
				region.setV(4f/8);
				region.setV2(5f/8);
				break;
			case 6: 
				region.setV(0f/8);
				region.setV2(1f/8);
				break;
			case 7: 
				region.setV(5f/8);
				region.setV2(6f/8);
				break;
		}
	}
	
	private void animate() {
		if(isMoving) {
			animation_timer -= Gdx.graphics.getDeltaTime();
			if(animation_timer <= 0) {
				animation_timer += animation_frame_duration;
				float u = region.getU() + 1f/6;
				if(u + 1f/6 > 1f) u = 0;

				region.setU(u);
				region.setU2(u + 1f/6);
			}
		}
		else {
			region.setU(1f/6);
			region.setU2(2f/6);
		}
	}
	
	@Override
	public float getDrawZ() {
		return pos.y;
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		animate();
		batch.setColor(color);
		batch.draw(region, pos.x-region.getRegionWidth()/2, pos.y, 0, 0, region.getRegionWidth(), region.getRegionHeight(), scale, scale, 0);
		batch.setColor(Color.WHITE);
		
		if(GameOptions.debug) collider.debug(batch);
	}
	
	public void shoot() {
		shoot(this.pos, this.dir);
	}
	
	public void shoot(Vector2 pos, Vector2 dir) {
		if(Flecha.pool.size == 0) {
			System.out.println("joder! Flecha.pool vacio!");
			return;
		}
		
		Flecha flecha = Flecha.pool.get(Flecha.pool.size-1);
		Flecha.pool.removeIndex(Flecha.pool.size-1);
		flecha.init(new Vector2(pos).add(0, size/2), dir);
		flechas.add(flecha);
		
		shootSound.play(Assets.volume);
	}
	
	public void hit() {
		if(immunity_timer == 0) {
			immunity_timer = immunity_after_hit;

			System.out.println(this.name + ": Aaaaagh!");
			hp--;
			
			// muere
			if(hp <= 0) {
				//dead = true;
				//W = A = S = D = false;
				wilhelmScream.play(Assets.volume);
			}
		}
	}
	
	public int getHp() {
		return hp;
	}
	
	public boolean isDead() {
		return dead;
	}
	
	public void move(float x, float y) {
		this.setPosition(pos.x + x, pos.y + y);
	}
	
	public void setPosition(float x, float y) {
		pos.set(x, y);
		collider.setPosition(x, y);
	}
	
	public Vector2 getPosition() {
		return tmp.set(pos);
	}
}

