package chustiboy;

import java.io.IOException;

import org.ipify.Ipify;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;

import chustiboy.EventSystem.EventConsumer;
import chustiboy.net.NetworkClient;
import chustiboy.net.NetworkHost;

public class MainMenu_Screen extends ScreenAdapter implements EventConsumer {
	
	private Stage stage;
	private Label status;
	private Array<Object> messagesQueue;
	
	MainMenu_Screen() {
		messagesQueue = new Array<>();
		
		stage = new Stage();
		Table layout = new VisTable();
		layout.setFillParent(true);
		stage.addActor(layout);
		
		status = new VisLabel();

		final VisTextButton create_button = new VisTextButton("Crear partida");

		final VisTextField ip_textfield = new VisTextField();
		ip_textfield.setText("localhost");
		ip_textfield.setMessageText("IP del server");

		final VisTextButton join_button = new VisTextButton("Unirse");
		
		final VisTextButton exit_game_button = new VisTextButton("Salir del juego");
		exit_game_button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			}
		});

		layout.add(status).colspan(2).padBottom(20).row();
		layout.add(create_button).fillX().colspan(2).row();
		layout.add(ip_textfield).padTop(10).padRight(3);
		layout.add(join_button).fillX().padTop(10).row();
		layout.add(exit_game_button).fillX().colspan(2).padTop(25).row();
		
		
		// TODO ver bien donde lo meto
		final VisTextField name_textfield = new VisTextField();
		name_textfield.setMessageText("nombre");
		name_textfield.setText(Assets.preferences.getString("name"));

		final VisTextButton name_button = new VisTextButton("guardar");
		name_button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Assets.preferences.putString("name", name_textfield.getText());
				Assets.preferences.flush();
			}
		});
		
		layout.add(name_textfield).fillX().padTop(50);
		layout.add(name_button).padTop(50);
		
		create_button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				status.setColor(Color.WHITE);
				status.setText("Creando servidor...");
				create_button.setDisabled(true);
				join_button.setDisabled(true);
				ip_textfield.setDisabled(true);
				name_textfield.setDisabled(true);
				name_button.setDisabled(true);
				
				final Lobby_Screen lobby = new Lobby_Screen();
				new Thread() {
					@Override
					public void run() {
						try {
							lobby.net = new NetworkHost();lobby.net.lobby = lobby;
							((NetworkHost)lobby.net).addMyPlayer();
							lobby.stage_button.setDisabled(false);
							try {
								lobby.server_ip_label.setText(Ipify.getPublicIp());
							} catch(IOException e) {
								lobby.server_ip_label.setText("Error: no se puede mostrar la ip\nEstas conectado a internet?");
							}
							EventSystem.produceMessage(lobby, messagesQueue);
							
						} catch(IOException e) {
							status.setColor(Color.RED);
							status.setText("Error al crear el servidor");
						}
						
						create_button.setDisabled(false);
						join_button.setDisabled(false);
						ip_textfield.setDisabled(false);
						name_textfield.setDisabled(false);
						name_button.setDisabled(false);
					}
				}.start();
			}
		});
		
		join_button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				status.setColor(Color.WHITE);
				status.setText("Conectando...");
				create_button.setDisabled(true);
				join_button.setDisabled(true);
				ip_textfield.setDisabled(true);
				name_textfield.setDisabled(true);
				name_button.setDisabled(true);
				
				final Lobby_Screen lobby = new Lobby_Screen();
				new Thread() {
					@Override
					public void run() {
						try {
							lobby.net = new NetworkClient(ip_textfield.getText());
							lobby.net.lobby = lobby;
							lobby.server_ip_label.setText(ip_textfield.getText());
							EventSystem.produceMessage(lobby, messagesQueue);
						} catch(IOException e) {
							status.setColor(Color.RED);
							status.setText("Error al conectar");
						}
		
						create_button.setDisabled(false);
						join_button.setDisabled(false);
						ip_textfield.setDisabled(false);
						name_textfield.setDisabled(false);
						name_button.setDisabled(false);
					}
				}.start();
			}
		});
	}
	
	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		status.setText("");
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
		EventSystem.consumeMessages(messagesQueue, this);
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    	Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
    	stage.act();
    	stage.draw();
	}
	
	@Override
	public void dispose() {
		stage.dispose();
	}
	
	@Override
	public void consumeMessage(Object o) {

		if(o instanceof Lobby_Screen) {
			TheGAME.pushScreen((Lobby_Screen)o);
		}
		
	}
}
