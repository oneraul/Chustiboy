package chustiboy.gameplay.boss.snorlax;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import chustiboy.Assets;
import chustiboy.Partida;
import chustiboy.gameplay.AnimatedSprite;
import chustiboy.gameplay.Chustilla;
import chustiboy.gameplay.Dibujable;
import chustiboy.gameplay.Rectangle;

public class Casita implements Dibujable {
	
	Array<Rectangle> suelo;
	AnimatedSprite lava;
	boolean active;
	float duracion, timer;
	int centroWidth = 300, centroHeight = 260;
	Vector2 tmp;
	
	Casita() {
		duracion = 10;
		lava = new AnimatedSprite(Assets.textures[4], 0, 0, 3, 0.10f);
		lava.setAlpha(0.75f);
		suelo = new Array<>();
		centroWidth = 300;
		centroHeight = 260;
		tmp = new Vector2();
	}
	
	void init(float x, float y) {
		lava.setSize(Partida.stage_width, Partida.stage_height);
		lava.setPosition(Partida.stage_width/2, 0);
		
		suelo.clear();
		suelo.add(new Rectangle(x, y, centroWidth, centroHeight));
		
		for(Chustilla chustilla : Partida.chustillas) {
			tmp.set(x, y).sub(chustilla.getPosition());
			suelo.add(new Rectangle(chustilla.getPosition().x+tmp.x/2, chustilla.getPosition().y-5, (int)tmp.x, 20));
			suelo.add(new Rectangle(chustilla.getPosition().x+tmp.x, chustilla.getPosition().y, 20, (int)tmp.y));
		}
		
		timer = duracion;
		active = true;
	}
	
	void update() {
		if(active) {
			timer -= Gdx.graphics.getDeltaTime();
			if(timer <= 0) {
				active = false;
				return;
			}
			
			for(Chustilla chustilla : Partida.chustillas) {
				boolean safe = false;
				for(Rectangle collider : suelo) {
					if(collider.collide(chustilla.collider)) {
						safe = true;
						break;
					}
				}
				if(!safe) {
					chustilla.hit();
				}
			}
		}
	}

	@Override
	public void draw(SpriteBatch batch) {
		if(active) {
			lava.draw(batch);
			for(Rectangle s : suelo) {
				batch.draw(Partida.sueloTexture, s.pos.x-s.w, s.pos.y, s.w*2, s.h);
			}
		}
	}

	@Override
	public float getDrawZ() {
		return 999998;
	}
}
