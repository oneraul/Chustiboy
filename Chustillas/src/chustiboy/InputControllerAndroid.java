package chustiboy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.glutils.*;

public class InputControllerAndroid extends InputAdapter {

	ShapeRenderer shaper;
	private float screen_w, screen_h;
	private float margin_x, margin_y, button_size, w, h;
	private InputController inputController;
	
	InputControllerAndroid(InputController inputController) {
		this.inputController = inputController;
		
		screen_w = Gdx.graphics.getWidth();
		screen_h = Gdx.graphics.getHeight();
		shaper = new ShapeRenderer();
		
		margin_x  = 50;
		margin_y = 50;
		button_size = 0.1f;
		w = screen_w * button_size;
		h = screen_h * button_size;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		
		screenY = (int)screen_h - screenY;
		
		if(screenX > screen_w-margin_x-w
		&& screenX < screen_w-margin_x
		&& screenY > margin_y
		&& screenY < margin_y + h) {
			inputController.shoot();
		}
		
		if(screenX > screen_w-margin_x-w
		&& screenX < screen_w-margin_x
		&& screenY > screen_h-margin_y-h
		&& screenY < screen_h-margin_y) {
			inputController.menu();
		}

		return false;
	}

	public void polledInput() {
		
		inputController.notMoving();
		
		if(!Gdx.input.isTouched()) {
			return;
		}
		
		float screenX, screenY;
		screenX = Gdx.input.getX();
		screenY = Gdx.graphics.getHeight() - Gdx.input.getY();
		
		if(screenX > margin_x
		&& screenX < margin_x  + w
		&& screenY > margin_y
		&& screenY < margin_y + h * 3)
			inputController.A(true);

		if(screenX > margin_x  + w * 2
		&& screenX < margin_x  + w * 3
		&& screenY > margin_y
		&& screenY < margin_y + h * 3)
			inputController.D(true);

		if(screenX > margin_x
		&& screenX < margin_x  + w * 3
		&& screenY > margin_y
		&& screenY < margin_y + h)
			inputController.S(true);

		if(screenX > margin_x
		&& screenX < margin_x  + w * 3
		&& screenY > margin_y + h * 2
		&& screenY < margin_y + h * 3)
			inputController.W(true);
	}
	
	public void displayControlsOnScreen() {
		shaper.begin(ShapeRenderer.ShapeType.Line);
		shaper.rect(margin_x + w,   margin_y + h*2, w, h); // W
		shaper.rect(margin_x,       margin_y + h,   w, h); // A
		shaper.rect(margin_x + w,   margin_y,       w, h); // S
		shaper.rect(margin_x + w*2, margin_y + h,   w, h); // D

		shaper.rect(screen_w-margin_x-w, margin_y, w, h); // shoot
		shaper.rect(screen_w-margin_x-w, screen_h-margin_y-h, w, h); // menu
		shaper.end();
	}
}
	
