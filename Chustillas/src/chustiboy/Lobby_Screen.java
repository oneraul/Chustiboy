package chustiboy;

import java.util.HashMap;
import java.util.Map;

import chustiboy.EventSystem.EventConsumer;
import chustiboy.gameplay.stage.*;
import chustiboy.net.NetworkHost;
import chustiboy.net.packets.Packet_color;
import chustiboy.net.packets.Packet_current_stage;
import chustiboy.net.packets.Packet_player;
import chustiboy.net.packets.Packet_player_disconnected;
import chustiboy.net.packets.Packet_ready;
import chustiboy.net.packets.Packet_set_pj_id;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.util.dialog.DialogUtils;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import chustiboy.net.*;
import com.kotcrab.vis.ui.util.dialog.*;

public class Lobby_Screen extends ScreenAdapter implements EventConsumer {
	
	public Network net;
	private Stage stage;
	public Array<Object> messagesQueue;
	public VisTable players_table;
	public VisTextButton exit_button, ready_button, stage_button;
	public VisLabel server_ip_label;
	public Map<Byte, Player> players;
	private String playerID;
	public byte connectionID;
	private Texture[] stage_drawables;
	private VisImage stage_image;
	public int currentStage;
	
	Lobby_Screen() {
		Partida.pj_id = -1;
		messagesQueue = new Array<>();
		players = new HashMap<>();
		
		playerID = Assets.preferences.getString("name", "Chustilla") + MathUtils.random(10000);
		
		stage = new Stage();
		Table layout = new VisTable();
		layout.setFillParent(true);
		layout.top();
		stage.addActor(layout);
		players_table = new VisTable();
		
		server_ip_label = new VisLabel();
		
		exit_button = new VisTextButton("Salir");
		exit_button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(net instanceof NetworkHost) {
					net.sendTCP("[EXIT]");
				}
				
				exitToMainMenu();
			}
		});
		
		ready_button = new VisTextButton("Listo");
		ready_button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Player player = players.get(connectionID);
				player.setReady(!player.isReady());
				
				Packet_ready p = new Packet_ready();
				p.pj_id = connectionID;
				p.ready = player.isReady();
				net.sendTCP(p);
				
				if(net instanceof NetworkHost) {
					((NetworkHost)net).check_ready();
				}
			}
		});
		
		
		// Stage button
		stage_drawables = new Texture[] {
			Assets.suelo(EmptyStage.color0, EmptyStage.color1),
			Assets.suelo(StageSnorlax.color0, StageSnorlax.color1),
		};
		
		stage_image = new VisImage();
		stage_image.setDrawable(stage_drawables[0]);
		
		stage_button = new VisTextButton("");
		stage_button.addListener(new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				currentStage++;
				if(currentStage >= stage_drawables.length) currentStage = 0;
				
				stage_image.setDrawable(stage_drawables[currentStage]);
				
				Packet_current_stage p = new Packet_current_stage();
				p.stage = currentStage;
				net.sendTCP(p);
			}
		});
		stage_button.add(stage_image);
		stage_button.setDisabled(true);
		
		layout.add(server_ip_label).colspan(2).row();
		layout.add(exit_button).padTop(50).width(100);
		layout.add(ready_button).padTop(50).width(100).row();
		layout.add(players_table).colspan(2).padTop(15).left().row();
		layout.add(stage_button).colspan(2).padTop(15).center().row();
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
	
	private void startGame() {
		Partida.setChustillas(players_table);
		
		switch(currentStage) {
			case 0:
				Partida.setLevel(new EmptyStage());
				break;
			case 1:
				Partida.setLevel(new StageSnorlax(net));
				break;
		}
		
		net.setGameplayListener();
		synchronized(messagesQueue) { messagesQueue.clear(); }
		TheGAME.pushScreen(new Gameplay_Screen(net));
	}
	
	public String getPlayerID() {
		return playerID;
	}
	
	@Override
	public void render(float dt) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    	Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);

    	EventSystem.consumeMessages(messagesQueue, this);
    	
    	stage.act();
    	stage.draw();
	}
	
	@Override
	public void dispose() {
		stage.dispose();
		for(Texture texture : stage_drawables) {
			texture.dispose();
		}
		for(Actor actor : players_table.getChildren()) {
			((Player)actor).dispose();
		}
	}
	
	@Override
	public void consumeMessage(Object o) {
		
		if(o instanceof Packet_player) {
			Packet_player p = (Packet_player)o;
			
			Player player = new Player(net);
			player.setName(p.name);
			player.playerID = p.playerID;
			player.connectionID = p.connectionID;
			player.r = p.r;
			player.g = p.g;
			player.b = p.g;
			player.set_img_texture(p.r, p.g, p.b);
			player.setReady(p.ready);
			
			if(player.playerID.equals(this.playerID)) {
				this.connectionID = p.connectionID;
				player.set_mine();
			}

			players_table.add(player).row();
			players.put(player.connectionID, player);
		}
		
		else if(o instanceof Packet_color) {
			Packet_color color = (Packet_color)o;
			players.get(color.pj_id).set_img_texture(color.r, color.g, color.b);
		}
		
		else if(o instanceof Packet_ready) {
			Packet_ready p = (Packet_ready)o;
			Player player = players.get(p.pj_id);
			player.setReady(p.ready);
		}
		
		else if(o instanceof Packet_current_stage) {
			Packet_current_stage p = (Packet_current_stage)o;
			currentStage = p.stage;
			stage_image.setDrawable(stage_drawables[p.stage]);
		}
		
		else if(o instanceof String) {
			switch((String)o) {
				case "[START]":
					startGame();
					break;
			
				case "[EXIT]":
					exitToMainMenu();
					break;
				case "[POPSCREEN]":
					TheGAME.popScreen();
					break;
					
				case "[DISCONNECTED]":
					// TODO replace by ConfirmDialog
					DialogUtils.showOptionDialog(stage, "", "Has sido desconectado del servidor\npulsa yes para volver al menu principal", DialogUtils.OptionDialogType.YES_CANCEL, new OptionDialogAdapter() {
						@Override
						public void yes() {
							exitToMainMenu();
						}
					});
					break;
			}
		}
		
		else if(o instanceof Packet_player_disconnected) {
			byte id = ((Packet_player_disconnected)o).connection_id;
			players.get(id).remove(); // remove from players_table
			players.remove(id);		  // remove from players
		}
		
		else if(o instanceof Packet_set_pj_id) {
			Partida.pj_id = ((Packet_set_pj_id)o).pj_id;
		}
	}
	
	private void exitToMainMenu() {
		Partida.pj_id = -1;
		server_ip_label.setText("saliendo de la partida...");
		
		new Thread() {
			@Override
			public void run() {
				net.close();
				EventSystem.produceMessage("[POPSCREEN]", messagesQueue);
			}
		}.start();
	}
}
