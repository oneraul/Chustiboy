package chustiboy;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;

public class InputControllerPc extends InputAdapter {

	private InputController inputController;

	InputControllerPc(InputController inputController) {
		this.inputController = inputController;
	}

	@Override
	public boolean keyDown(int keycode) {
		switch(keycode) {
			case Keys.W:
				inputController.W(true);
				break;
				
			case Keys.S:
				inputController.S(true);
				break;
				
			case Keys.A:
				inputController.A(true);
				break;
				
			case Keys.D:
				inputController.D(true);
				break;

			case Keys.SPACE:
				inputController.shoot();
				break;
		}
		
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		switch(keycode) {
			case Keys.W:
				inputController.W(false);
				break;
				
			case Keys.S:
				inputController.S(false);
				break;
				
			case Keys.A:
				inputController.A(false);
				break;
				
			case Keys.D:
				inputController.D(false);
				break;
		}
		
		if(keycode == Keys.ESCAPE) {
			inputController.menu();
		}
		
		return false;
	}
}
