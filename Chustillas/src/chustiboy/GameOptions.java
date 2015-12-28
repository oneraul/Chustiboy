package chustiboy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisSlider;
import com.kotcrab.vis.ui.widget.VisTable;

public class GameOptions extends VisTable {
	
	public static boolean debug = false;
	
	GameOptions() {
		VisCheckBox fullscreen_checkbox = new VisCheckBox("Pantalla completa");
		fullscreen_checkbox.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(!Gdx.graphics.isFullscreen()) {
					Assets.preferences.putInteger("width", Gdx.graphics.getDesktopDisplayMode().width);
					Assets.preferences.putInteger("height", Gdx.graphics.getDesktopDisplayMode().height);
				} else {
					Assets.preferences.putInteger("width", 800);
					Assets.preferences.putInteger("height", 600);
				}
				Assets.preferences.putBoolean("fullscreen", !Gdx.graphics.isFullscreen());
				Assets.preferences.flush();
				
				Gdx.graphics.setDisplayMode(Assets.preferences.getInteger("width"), Assets.preferences.getInteger("height"), Assets.preferences.getBoolean("fullscreen"));
			}
		});
		
		final VisSlider volume_slider = new VisSlider(0, 1f, 0.05f, false);
		volume_slider.setValue(Assets.volume);
		volume_slider.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Assets.volume = volume_slider.getValue();
				Assets.preferences.putFloat("volume", volume_slider.getValue());
				Assets.preferences.flush();
			}
		});
		
		addSeparator().colspan(2).padTop(20).padBottom(20);
		add(fullscreen_checkbox).colspan(2).row();
		addSeparator().colspan(2).padTop(20).padBottom(20).row();
		add("Volumen ");
		add(volume_slider).padBottom(20).row();
	}
}
