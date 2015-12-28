package chustiboy;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.PovDirection;

public class InputControllerGamepad360 extends ControllerAdapter {
	
	private InputController inputController;
	Controller controller;
	
	InputControllerGamepad360(InputController inputController) {
		this.inputController = inputController;
	}
	
	@Override
	public boolean axisMoved(Controller controller, int axisCode, float value) {
		
		if(axisCode == XBox360Pad.AXIS_LEFT_X) {
			if(value > 0.1f) inputController.D(true);
			else if(value < -0.1f) inputController.A(true);
			else {
				inputController.D(false);
				inputController.A(false);
			}
		}
		
		if(axisCode == XBox360Pad.AXIS_LEFT_Y) {
			if(value > 0.1f) inputController.S(true);
			else if(value < -0.1f) inputController.W(true);
			else {
				inputController.W(false);
				inputController.S(false);
			}
		}
		
		return false;
	}
	
	@Override
	public boolean buttonDown(Controller controller, int buttonIndex) {
		if(buttonIndex == XBox360Pad.BUTTON_A)
			inputController.shoot();
		
		else if(buttonIndex == XBox360Pad.BUTTON_START)
			inputController.menu();
		
		return false;
	}
	
	@Override
	public boolean povMoved(Controller controller, int povCode, PovDirection value) {
		
		if(value.equals(PovDirection.center)) {
			inputController.notMoving();
			
		} else {
			
			if(value.equals(PovDirection.north)) {
				inputController.W(true);
			}
			else if(value.equals(PovDirection.northWest)) {
				inputController.W(true);
				inputController.A(true);
			}
			else if(value.equals(PovDirection.west)) {
				inputController.A(true);
			}
			else if(value.equals(PovDirection.southWest)) {
				inputController.S(true);
				inputController.A(true);
			}
			else if(value.equals(PovDirection.south)) {
				inputController.S(true);
			}
			else if(value.equals(PovDirection.southEast)) {
				inputController.S(true);
				inputController.D(true);
			}
			else if(value.equals(PovDirection.east)) {
				inputController.D(true);
			}
			else if(value.equals(PovDirection.northEast)) {
				inputController.W(true);
				inputController.D(true);
			}
		}
		
		return false;
	}
	
	//This code was taken from http://www.java-gaming.org/index.php?topic=29223.0
	@SuppressWarnings("unused")
	private static class XBox360Pad {
		 public static final int BUTTON_X = 2;
		 public static final int BUTTON_Y = 3;
		 public static final int BUTTON_A = 0;
		 public static final int BUTTON_B = 1;
		 public static final int BUTTON_BACK = 6;
		 public static final int BUTTON_START = 7;
		 public static final PovDirection BUTTON_DPAD_UP = PovDirection.north;
		 public static final PovDirection BUTTON_DPAD_DOWN = PovDirection.south;
		 public static final PovDirection BUTTON_DPAD_RIGHT = PovDirection.east;
		 public static final PovDirection BUTTON_DPAD_LEFT = PovDirection.west;
		 public static final int BUTTON_LB = 4;
		 public static final int BUTTON_L3 = 8;
		 public static final int BUTTON_RB = 5;
		 public static final int BUTTON_R3 = 9;
		 public static final int AXIS_LEFT_X = 1; //-1 is left | +1 is right
		 public static final int AXIS_LEFT_Y = 0; //-1 is up | +1 is down
		 public static final int AXIS_LEFT_TRIGGER = 4; //value 0 to 1f
		 public static final int AXIS_RIGHT_X = 3; //-1 is left | +1 is right
		 public static final int AXIS_RIGHT_Y = 2; //-1 is up | +1 is down
		 public static final int AXIS_RIGHT_TRIGGER = 4; //value 0 to -1f
	}
}
