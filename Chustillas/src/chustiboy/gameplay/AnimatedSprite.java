package chustiboy.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class AnimatedSprite {
	private TextureRegion region;
	private AnimationFrame[] frames;
	private int current;
	private float acumulator;
	private Vector2 tmpOrigin;
	
	public AnimatedSprite(Texture texture) {
		region = new TextureRegion(texture);
		tmpOrigin = new Vector2();
	}
	
	public void animate() {
		acumulator += Gdx.graphics.getDeltaTime();
		if(acumulator >= frames[current].duration) {
			acumulator -= frames[current].duration;
			
			if(current < frames.length-1) current++;
			else current = 0;
			
			region.setRegion(frames[current].x, frames[current].y, frames[current].width, frames[current].height);
		}
	}
	
	public void draw(SpriteBatch batch, float x, float y) {
		this.draw(batch, x, y, 1f);
	}
	
	public void draw(SpriteBatch batch, float x, float y, float scale) {
		batch.draw(
			region, 							// TextureRegion
			x-frames[current].originX * scale,	// x
			y-frames[current].originY * scale,	// y
			frames[current].width	  * scale,	// width
			frames[current].height	  * scale	// height
		);
	}
	
	public void setFrames(AnimationFrame[] frames) {
		acumulator  = 0;
		current     = 0;
		this.frames = frames;
		region.setRegion(frames[current].x, frames[current].y, frames[current].width, frames[current].height);
	}
	
	public Vector2 getOrigin() {
		return tmpOrigin.set(frames[current].originX, frames[current].originY);
	}
}
