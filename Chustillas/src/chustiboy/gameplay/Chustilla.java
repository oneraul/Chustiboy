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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import chustiboy.gameplay.AnimationData.Direction;
import chustiboy.gameplay.ChustillaAnimationData.State;

public class Chustilla implements Dibujable {
	
	public Array<Flecha> flechas;
	private Vector2 pos, tmp;
	public Vector2 dir;
	public boolean W, A, S, D, isMoving;
	public String name;
	private Color color;
	public Rectangle collider;
	private int hp;
	public ChustillaAnimationData animationData;
	private AnimatedSprite animation;
	private boolean dead, dying, attacking;
	
	private int size = 15;
	private float scale = 0.75f;
	
	private float immunity_after_hit = 0.5f;
	private float immunity_timer, dying_timer, attack_timer, v; //animation_timer, animation_frame_duration;
	
	private Sound wilhelmScream, shootSound;

	public Chustilla() {
		v = 2.3f;
		hp = 25;
		color = new Color(Color.WHITE);
		pos = new Vector2();
		tmp = new Vector2();
		dir = new Vector2(1, 0);
		collider = new Rectangle(pos, size, size);
		flechas = new Array<Flecha>();
		
		move(0, 0); // to set collider and sprite on position
		
		shootSound = Assets.sounds[0];
		wilhelmScream = Assets.sounds[1];
		
		animationData = new ChustillaAnimationData();
		animation = new AnimatedSprite(Assets.textures[5]);
		animation.setFrames(animationData.getFrames());
	}

	public void setColor(float r, float g, float b) {
		color.set(r, g, b, 1);
	}
	
	public void update() {
		
		if(!dead) {
			float dt = Gdx.graphics.getDeltaTime();
			
			// resetear el "parpadeo" de despues de recibir un golpe
			if(immunity_timer > 0) {
				immunity_timer -= dt;
				if(immunity_timer <= 0) {
					immunity_timer = 0;
				}
			}
			
			if(dying) {
				dying_timer -= dt;
				if(dying_timer <= 0) {
					dying = false;
					dead = true;
					animation.setFrames(animationData.setState(State.dead));
				}
			}
			
			else if(attacking) {
				attack_timer -= Gdx.graphics.getDeltaTime();
				if(attack_timer <= 0) {
					attacking = false;
					processAnimationState();
				}
			}
		}
		
		// comprueba si las flechas se clavan en algun muro
		if(flechas.size == 0) return;
		
		for(int i = flechas.size-1; i >= 0; i--) {
			Flecha flecha = flechas.get(i);
			
			flecha.update();
			
			for(Muro muro : Partida.muros) {
				if(flecha.collider.collide(muro.collider)) {
					flecha.checkTapada(muro.collider_tapar);
					flecha.clavarse(muro.collider);
					flechas.removeIndex(i);
					break;
				}
			}
		}
	}
	
	public void movement(Network net) {
		if(dead || dying) return;
		
		float a = 0, b = 0;

		if(W) b++;
		if(A) a--;
		if(S) b--;
		if(D) a++;

		if(a == 0 && b == 0) {
			if(isMoving){
				isMoving = false;
				processAnimationState();
				
			} else
			
			isMoving = false;
			
		} else {
			
			if(!isMoving){
				isMoving = true;
				processAnimationState();
				
			} else isMoving = true;
			
			dir.set(a, b).nor();
	
			// horizontal movement
			move(dir.x * v, 0);
			for(Muro muro : Partida.muros) {
				if(muro.collider.collide(collider)) {
					     if(D) setPosition(muro.x() - muro.width()/2 - size/2, this.pos.y);
					else if(A) setPosition(muro.x() + muro.width()/2 + size/2, this.pos.y);
				}
			}
			for(Boss boss : Partida.bosses) {
				for(Rectangle bossCollider : boss.movementColliders) {
					if(bossCollider.collide(collider)) {
						     if(D) setPosition(bossCollider.pos.x - bossCollider.w - size/2, this.pos.y);
						else if(A) setPosition(bossCollider.pos.x + bossCollider.w + size/2, this.pos.y);
					}
				}
			}
	
			// vertical movement
	       	move(0, dir.y * v);
			for(Muro muro : Partida.muros) {
				if(muro.collider.collide(collider)) {
					     if(W) setPosition(this.pos.x, muro.y() - size);
					else if(S) setPosition(this.pos.x, muro.y() + muro.height());
				}
			}
			for(Boss boss : Partida.bosses) {
				for(Rectangle bossCollider : boss.movementColliders) {
					if(bossCollider.collide(collider)) {
					         if(W) setPosition(this.pos.x, bossCollider.pos.y - size);
						else if(S) setPosition(this.pos.x, bossCollider.pos.y + bossCollider.h);
					}
				}
			}
		}
		
		//update the network
		Packet_position p = new Packet_position();
    	p.x 		= pos.x;
    	p.y 		= pos.y;
    	p.direction = animationData.getDirection();
    	p.state		= animationData.getState();
    	p.pj_id 	= Partida.pj_id;
    	net.sendUDP(p);
	}
	
	public void processAnimationDirection() {
		
		if(W) {
			if(D) 		animationData.setDirection(Direction.northEast);
			else if(A) 	animationData.setDirection(Direction.northWest);
			else 		animationData.setDirection(Direction.north);
		}
		else if(S) {
			if(D) 		animationData.setDirection(Direction.southEast);
			else if(A) 	animationData.setDirection(Direction.southWest);
			else 		animationData.setDirection(Direction.south);
		}
		else if(D) 		animationData.setDirection(Direction.east);
		else if(A) 		animationData.setDirection(Direction.west);
		
		if(!dying && !dead)
			animation.setFrames(animationData.getFrames());
	}
	
	public void processAnimationState() {
		
		if(dead) 		   animationData.setState(State.dead);
		else if(dying) 	   animationData.setState(State.dying);
		else if(attacking) animationData.setState(State.attack);
		else if(isMoving)  animationData.setState(State.walk);
		else 			   animationData.setState(State.idle);
		
		if(!dying && !dead)
			animation.setFrames(animationData.getFrames());
	}
	
	public void processNetworkAnimation(Packet_position p) {
		if(p.state     != animationData.getState()
		|| p.direction != animationData.getDirection())
		{
			animationData.setState(p.state);
			animationData.setDirection(p.direction);
			animation.setFrames(animationData.getFrames());
		}	
	}
	
	@Override
	public float getDrawZ() {
		return pos.y;
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		animation.animate();
		batch.setColor(color);
		animation.draw(batch, pos.x, pos.y, scale);
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
		flecha.init(pos, dir, animationData.getDirection());
		flechas.add(flecha);
		
		shootSound.play(Assets.volume);
		
		//animation
		attacking = true;
		attack_timer = 0.2f;
		processAnimationState();
	}
	
	public void hit() {
		if(immunity_timer == 0) {
			immunity_timer = immunity_after_hit;

			hp--;
			if(hp <= 0) die();
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
	
	public void die() {
		
		// TODO debería mandar un paquete actualizando a los demás
		
		if(!dying && !dead) {
			attacking = false;
			dying = true;
			dying_timer = 1.475f;
			animation.setFrames(animationData.setState(State.dying));
			
			wilhelmScream.play(Assets.volume);
		}
	}
}

