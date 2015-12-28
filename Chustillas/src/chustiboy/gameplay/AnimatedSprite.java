package chustiboy.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class AnimatedSprite {

	private int width, height, n, srcX;
	private float x, y, timer, duration;
	public boolean animate_manually;
	Texture texture;
	Color color;
	
	public AnimatedSprite(Texture texture, int width, int height, int n, float duration) {
		this.texture = texture;
		this.width = width;
		this.height = height;
		this.n = n;
		this.duration = duration;
		this.color = new Color(Color.WHITE);
	}
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public void setPosition(float x, float y) {
		this.x = x - width/2;
		this.y = y;
	}
	
	public void setAlpha(float alpha) {
		this.color.a = alpha;
	}
	
	public void setColor(Color color) {
		this.color.set(color);
	}
	
	public void draw(SpriteBatch batch) {
		animate();
		
		batch.setColor(color);
		batch.draw(texture, x, y, srcX, 0, width, height);
		batch.setColor(Color.WHITE);
	}
	
	public void animate() {
		if((timer += Gdx.graphics.getDeltaTime()) >= duration) {
			timer -= duration;
			
			srcX += texture.getWidth()/n;
			if(srcX >= texture.getWidth())
				srcX = 0;
		}
	}
}
