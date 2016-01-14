package chustiboy.gameplay.boss.snorlax;

import chustiboy.Assets;
import chustiboy.GameOptions;
import chustiboy.Partida;
import chustiboy.gameplay.Chustilla;
import chustiboy.gameplay.ParticleSystem;
import chustiboy.gameplay.Rectangle;
import chustiboy.gameplay.Pool.Poolable;
import chustiboy.gameplay.Dibujable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;

public class Charco implements Dibujable, Poolable {
	
	static ShaderProgram shaderLava;
	static float shaderDeltaTime;
	static Texture shaderDisplacement;
	
	Rectangle collider;
	ParticleSystem particleSystem;
	boolean drawing = false, activated;
	private float a;
	float x, y;
	int width, height;
	private Color tmpColor;
	
	public void init(float x, float y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		width  *= 0.75f;
		height *= 0.75f;
		y += (int)(this.height*0.125f);
		
		collider = new Rectangle(x, y, width, height);
		
		particleSystem = new ParticleSystem.Builder(x, y, width, height)
			.max_particles(width * 2).initial_particle_size(4f).build();
		
		drawing = true;
		activated = false;
		tmpColor = new Color();
	}
	
	@Override
	public float getDrawZ() {
		return 999999;
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		if(drawing) {
			if(batch.getShader() != shaderLava) {
				shaderDisplacement.bind(1);
				Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
				batch.setShader(shaderLava);
				shaderDeltaTime += Gdx.graphics.getDeltaTime();
				float angle = shaderDeltaTime * (2*MathUtils.PI);
				if(angle > (2*MathUtils.PI)) angle -= (2*MathUtils.PI);
				shaderLava.setUniformf("u_deltatime", shaderDeltaTime);
			}
			
			tmpColor.set(batch.getColor()).a = a;
			batch.setColor(tmpColor);
			
				batch.draw(Assets.textures[4], x-width/2, y, width, height);
				particleSystem.draw(batch);
			
			tmpColor.a = 1;
			batch.setColor(tmpColor);
			
			if(GameOptions.debug) collider.debug(batch);
		}
	}
	
	public void update() {
		if(!activated) {
			a += 0.02f;
			if(a >= 1f) {
				a = 1;
				activated = true;
			}
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
