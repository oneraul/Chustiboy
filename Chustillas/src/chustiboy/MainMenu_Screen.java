package chustiboy;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.util.dialog.DialogUtils;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;

public class MainMenu_Screen extends ScreenAdapter {
	
	private Stage stage;
	
	MainMenu_Screen() {
		stage = new Stage();
		Table layout = new VisTable();
		layout.setFillParent(true);
		stage.addActor(layout);

		VisTextButton create_button = new VisTextButton("Crear partida");
		create_button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				try {
					TheGAME.pushScreen(new Lobby_Screen());
				} catch(IOException e) {
					DialogUtils.showErrorDialog(stage, "Error al crear partida", e);
				}
			}
		});

		final VisTextField ip_textfield = new VisTextField();
		ip_textfield.setText("localhost");
		ip_textfield.setMessageText("IP del server");

		VisTextButton join_button = new VisTextButton("Unirse");
		join_button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				try {
					TheGAME.pushScreen(new Lobby_Screen(ip_textfield.getText()));
				} catch(IOException e) {
					DialogUtils.showErrorDialog(stage, "Error al conectar", e);
				}
			}
		});
		
		VisTextButton exit_game_button = new VisTextButton("Salir del juego");
		exit_game_button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			}
		});

		layout.add(create_button).fillX().colspan(2).row();
		layout.add(ip_textfield).padTop(10).padRight(3);
		layout.add(join_button).fillX().padTop(10).row();
		layout.add(exit_game_button).fillX().colspan(2).padTop(25).row();
		
		
		// TODO ver bien donde lo meto
		final VisTextField name_textfield = new VisTextField();
		name_textfield.setMessageText("nombre");
		name_textfield.setText(Assets.preferences.getString("name"));

		VisTextButton name_button = new VisTextButton("guardar");
		name_button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Assets.preferences.putString("name", name_textfield.getText());
				Assets.preferences.flush();
			}
		});
		
		layout.add(name_textfield).fillX().padTop(50);
		layout.add(name_button).padTop(50);
	}
	
	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
	}
	
    @Override
    public void resize(int width, int height) {
		stage.getViewport().update(width, height);
		stage.getCamera().viewportWidth = width;
		stage.getCamera().viewportHeight = height;
		stage.getCamera().update();
    }
	
	@Override
	public void render(float dt) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    	Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
    	
    	stage.act();
    	stage.draw();
	}
	
	@Override
	public void dispose() {
		stage.dispose();
	}
}
