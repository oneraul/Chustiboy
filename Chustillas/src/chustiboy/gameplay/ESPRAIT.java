package chustiboy.gameplay;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import chustiboy.Assets;

public class ESPRAIT extends Sprite {
	
	public ESPRAIT(Texture texture, int width, int height) {
		super(texture);
		this.setSize(width, height);
		this.setOrigin(this.getWidth()/2, 0);
	}
	
	public ESPRAIT(int width, int height) {
		this(Assets.whitePixel, width, height);
	}
	
	public Vector2 getPosition() {
		return new Vector2(this.getX()-this.getWidth()/2, this.getY());
	}
	
	public void setPosition(float x, float y) {
		this.setCenterX(x);
		this.setY(y);
	}

	public void draw(SpriteBatch batch) {
		super.draw(batch);
	}
}
