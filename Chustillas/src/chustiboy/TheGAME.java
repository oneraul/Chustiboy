package chustiboy;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.VisUI;

public class TheGAME extends Game {

	private static Array<Screen> screens;
	static private TheGAME GAME;
	
	public TheGAME() {
		GAME = this;
	}
	
	@Override
	public void create() {
		VisUI.load();
		Assets.load();
		
		screens = new Array<>();
		pushScreen(new MainMenu_Screen());
	}
	
	public static void pushScreen(Screen screen) {
		screens.add(screen);
		GAME.setScreen(screens.peek());
	}
	
	public static void popScreen() {
		popScreen(1);
	}
	
	public static void popScreen(int screens_to_pop) {
		for(int i = 0; i < screens_to_pop; i++) {
			screens.pop();
		}
		GAME.setScreen(screens.peek());
	}
	
	@Override
	public void dispose() {
		VisUI.dispose();
		Assets.dispose();
	}
}
