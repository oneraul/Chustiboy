package chustiboy.gameplay.boss.snorlax;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

import chustiboy.Assets;
import chustiboy.GameOptions;
import chustiboy.Partida;
import chustiboy.gameplay.Chustilla;
import chustiboy.gameplay.Dibujable;
import chustiboy.gameplay.Rectangle;

public class Casita implements Dibujable {
	
	static int centroWidth = 300, centroHeight = 260;
	
	boolean active;
	private Array<TrozoDeSuelo> suelo;
	private float x, y, duracion, timer;
	private int width, height;
	
	Casita() {
		duracion = 10;
		suelo = new Array<>();
	}
	
	void init(float x, float y) {
		
		suelo.clear();
		suelo.add(new TrozoDeSuelo(x, y, centroWidth, centroHeight));
		
		for(Chustilla chustilla : Partida.chustillas) {
			int dstY = (int)(y - chustilla.getPosition().y);
			suelo.add(new TrozoDeSuelo(x, chustilla.getPosition().y, 30, dstY+30));
			int dstX = (int)(x - chustilla.getPosition().x);
			suelo.add(new TrozoDeSuelo(chustilla.getPosition().x+dstX/2, chustilla.getPosition().y, dstX, 30));
		}
		
		timer = duracion;
		active = true;
		
		//para la lava
		this.x =      -Partida.stage_width  * 0.12f;
		this.y =      -Partida.stage_height * 0.075f;
		width  = (int)(Partida.stage_width  * 1.25f);
		height = (int)(Partida.stage_height * 1.2f);
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
				for(TrozoDeSuelo s : suelo) {
					if(s.collider.collide(chustilla.collider)) {
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
			if(batch.getShader() != Charco.shaderLava) {
				Charco.shaderDisplacement.bind(1);
				Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
				batch.setShader(Charco.shaderLava);
				Charco.shaderDeltaTime += Gdx.graphics.getDeltaTime();
				float angle = Charco.shaderDeltaTime * (2*MathUtils.PI);
				if(angle > (2*MathUtils.PI)) angle -= (2*MathUtils.PI);
				Charco.shaderLava.setUniformf("u_deltatime", Charco.shaderDeltaTime);
			}
				
			batch.draw(Assets.textures[4], x, y, width, height);
			for(TrozoDeSuelo s : suelo) {
				s.draw(batch);
			}
			
			if(GameOptions.debug) {
				for(TrozoDeSuelo s : suelo) {
					s.collider.debug(batch);
				}
			}
		}
	}

	@Override
	public float getDrawZ() {
		return 999998;
	}
	
	private class TrozoDeSuelo {
		float x, y;
		int width, height;
		Rectangle collider;
		
		TrozoDeSuelo(float x, float y, int width, int height) {
			collider = new Rectangle(x, y, width, height);
			
			this.x = x;
			this.y = y - height * 0.125f;
			this.width  = width  *= 1.25f;
			this.height = height *= 1.25f;
		}

		void draw(SpriteBatch batch) {
			batch.draw(Assets.textures[9], x-width/2, y, width, height);
		}
	}
}
