package chustiboy.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimatedSprite {
	private TextureRegion region;
	private AnimationFrame[] frames;
	private int current;
	private float acumulator;
	
	public AnimatedSprite(Texture texture) {
		region = new TextureRegion(texture);
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
		batch.draw(region, x-frames[current].originX, y-frames[current].originY, region.getRegionWidth()*scale, region.getRegionHeight()*scale);
	}
	
	public void setFrames(AnimationFrame[] frames) {
		acumulator = 0;
		this.frames = frames;
		current = 0;
		region.setRegion(frames[current].x, frames[current].y, frames[current].width, frames[current].height);
	}
}
