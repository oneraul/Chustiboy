package chustiboy;

import chustiboy.EventSystem.EventConsumer;
import chustiboy.gameplay.Chustilla;
import chustiboy.gameplay.Dibujable;
import chustiboy.gameplay.Flecha;
import chustiboy.gameplay.Muro;
import chustiboy.gameplay.ScreenShaker;
import chustiboy.gameplay.boss.Boss;
import chustiboy.gameplay.boss.snorlax.BigBigMaloMaloso;
import chustiboy.net.Network;
import chustiboy.net.NetworkClient;
import chustiboy.net.packets.Packet_pj_hit;
import chustiboy.net.packets.Packet_position;
import chustiboy.net.packets.Packet_set_pj_id;
import chustiboy.net.packets.Packet_shoot;
import chustiboy.net.packets.Packet_with_pj_id;
import chustiboy.net.packets.boss.Packet_boss;
import chustiboy.net.packets.boss.Packet_boss_dead;
import chustiboy.net.packets.boss.Packet_boss_position;
import chustiboy.net.packets.boss.snorlax.Packet_boss_init_casita;
import chustiboy.net.packets.boss.snorlax.Packet_boss_spawn_firePuddle;
import chustiboy.net.packets.boss.snorlax.Packet_boss_spawn_fireball;
import chustiboy.net.packets.boss.snorlax.Packet_boss_stomp;
import chustiboy.net.packets.boss.snorlax.Packet_boss_stomp_stop;
import chustiboy.net.packets.boss.snorlax.Packet_boss_tron;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisWindow;
import java.util.Comparator;

public class Gameplay_Screen extends ScreenAdapter implements EventConsumer {
	
	private Network net;
    private Camera cam;
    private SpriteBatch batch;
	private Comparator<Dibujable> comparator;
    private Stage stage;
	private VisLabel hp_label;
	private VisWindow menu_window;
	private TextureRegion suelo;
    private Array<Chustilla> chustillas;
	private Array<Dibujable> dibujables;
    public Array<Object> messagesQueue;
    private InputControllerAndroid inputControllerAndroid;
	
	Gameplay_Screen(final Network net) {
		this.net = net;
		net.gameplay = this;
		
    	messagesQueue = new Array<>();
    	
    	cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		ScreenShaker.setCam(cam);
		
		batch = new SpriteBatch();
		comparator = new Comparator<Dibujable>() {
			public int compare(Dibujable a, Dibujable b) {
				return (int)(b.getDrawZ() - a.getDrawZ());
			}
		};
		
		suelo = new TextureRegion(Partida.sueloTexture);
		suelo.setRegionWidth(Partida.stage_width/2);
		suelo.setRegionHeight(Partida.stage_height/2);
		
		chustillas = Partida.chustillas; 
		Flecha.fillPool(chustillas.size);
		
		dibujables = new Array<>();
		for(Boss boss : Partida.bosses)
			boss.addDibujablesToList(dibujables);
		for(Muro muro : Partida.muros)
			dibujables.add(muro);
		for(Flecha flecha : Flecha.pool)
			dibujables.add(flecha);
		for(Chustilla chustilla : chustillas)
			dibujables.add(chustilla);
		
		// GUI -----------------------------
    	stage = new Stage();
		Table layout = new VisTable();
		layout.setFillParent(true);
		layout.top().left();
		stage.addActor(layout);
		hp_label = new VisLabel();
		layout.add(hp_label);
		
		VisTextButton exit_button = new VisTextButton("");
		if(net instanceof NetworkClient) {
			exit_button.setText("salir de la partida");
			exit_button.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					net.close();
					TheGAME.popScreen(2);
				}
			});
		} else {
			exit_button.setText("volver al lobby");
			exit_button.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					net.sendTCP("[LOBBY]");
					goBackToLobby();
				}
			});
		}
		
		VisCheckBox debug_checkbox = new VisCheckBox("Debug");
		debug_checkbox.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				GameOptions.debug = !GameOptions.debug;
			}
		});
		
		menu_window = new VisWindow("");
		menu_window.setVisible(false);
		menu_window.setMovable(false);
		menu_window.setPosition(Gdx.graphics.getWidth()/2 + menu_window.getWidth()/2, Gdx.graphics.getHeight()/2 - menu_window.getHeight()/2);
		stage.addActor(menu_window);
		menu_window.add(exit_button).row();
		menu_window.add(debug_checkbox).row();
		menu_window.add(new GameOptions());
		menu_window.setSize(Gdx.graphics.getWidth()/2 - menu_window.getWidth()/2 - 15, menu_window.getMinHeight());
		
		// INPUT ---------------------------
		InputController inputController = new InputController(net, messagesQueue);
		
		for(Controller controller : Controllers.getControllers()) {
			System.out.println(controller.getName());
			if(controller.getName().toLowerCase().contains("xbox")
			   && controller.getName().contains("360")) {
					InputControllerGamepad360 inputcontrollergamepad = new InputControllerGamepad360(inputController);
					inputcontrollergamepad.controller = Controllers.getControllers().peek();
					Controllers.addListener(inputcontrollergamepad);
			} else {
				// TODO add normal pad controller (no 360)
			}
		}
		
		InputMultiplexer multiplexer = new InputMultiplexer();
		// TODO
		multiplexer.addProcessor(new InputAdapter() {
			@Override
			public boolean scrolled(int amount) {
				((OrthographicCamera)cam).zoom += amount * 0.05f;
				return false;
			}
		});
		
		multiplexer.addProcessor(stage);
		if(Gdx.app.getType() == ApplicationType.Desktop) {
			if(Controllers.getControllers().size == 0)
				multiplexer.addProcessor(new InputControllerPc(inputController));
		}
		else {
			inputControllerAndroid = new InputControllerAndroid(inputController);
			multiplexer.addProcessor(inputControllerAndroid);
		}
		Gdx.input.setInputProcessor(multiplexer);
	}
    
    @Override
    public void resize(int width, int height) {
		stage.getViewport().update(width, height);
		stage.getCamera().viewportWidth = width;
		stage.getCamera().viewportHeight = height;
		stage.getCamera().update();
		cam.viewportWidth = width;
		cam.viewportHeight = height;
		cam.update();
    }
    
    @Override
    public void render(float dt) {
    	EventSystem.consumeMessages(messagesQueue, this);
    	
    	if(Gdx.app.getType() == ApplicationType.Android) {
    		inputControllerAndroid.polledInput();
    	}
		
		logic();
   		draw();
    }
    
    private void logic() {
    	chustillas.get(Partida.pj_id).movement(net);
    	for(Chustilla chustilla : chustillas)
    		chustilla.update();
    	
    	for(Boss boss: Partida.bosses)
			boss.update();
		
		for(Muro muro : Partida.muros) // para las flechas clavadas en el muro
			muro.update();
		
    	stage.act();
    }
	
	private void draw() {
    	cam.position.set(chustillas.get(Partida.pj_id).getPosition(), cam.position.z);
    	cam.update();
    	ScreenShaker.update();
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
    	Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
		
		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		{
			batch.draw(suelo, 0, 0, Partida.stage_width, Partida.stage_height);
			dibujables.sort(comparator);
			for(Dibujable dibujable : dibujables)
				dibujable.draw(batch);
		}
		batch.end();
			
		if(Gdx.app.getType() == ApplicationType.Android) {
			inputControllerAndroid.displayControlsOnScreen();
		}

		hp_label.setText(chustillas.get(Partida.pj_id).getHp() + " vidas \n" + Flecha.pool.size + " flechas\n" + Gdx.graphics.getFramesPerSecond() + " fps");
		stage.draw();
	}
	
	@Override
	public void consumeMessage(Object o) {
		
		if(o instanceof Packet_position) {
			Packet_position p = (Packet_position)o;
			Partida.chustillas.get(p.pj_id).setPosition(p.x, p.y);
			Partida.chustillas.get(p.pj_id).processNetworkAnimation(p);
		}
		
		else if(o instanceof Packet_shoot) {
			Packet_shoot p = (Packet_shoot)o;
			Vector2 pos = new Vector2(p.pos_x, p.pos_y);
			Vector2 dir = new Vector2(p.dir_x, p.dir_y);
			Partida.chustillas.get(p.pj_id).shoot(pos, dir);
		}
		
		else if(o instanceof Packet_pj_hit) {
			Partida.chustillas.get(((Packet_pj_hit)o).pj_id).hit();
		}
		
		else if(o instanceof Packet_boss) {
		
			if(o instanceof Packet_boss_position) {
				Packet_boss_position p = (Packet_boss_position)o;
				Partida.bosses.get(p.boss_id).setPosition(p.x, p.y);
			}
		
			else if(o instanceof Packet_boss_dead) {
				Partida.bosses.get(((Packet_boss_dead)o).boss_id).die();
			}
		
			// BOSS: Snorlax
			else if(Partida.bosses.get(((Packet_boss)o).boss_id) instanceof BigBigMaloMaloso) {
		
				if(o instanceof Packet_boss_spawn_fireball) {
					Packet_boss_spawn_fireball p = (Packet_boss_spawn_fireball)o;
					Vector2 pos = new Vector2(p.x, p.y);
					Vector2 dir = new Vector2(p.dir_x, p.dir_y);
					((BigBigMaloMaloso)(Partida.bosses.get(p.boss_id))).shootFireball(pos, dir);
				}
		
				else if(o instanceof Packet_boss_spawn_firePuddle) {
					Packet_boss_spawn_firePuddle p = (Packet_boss_spawn_firePuddle)o;
					float x = p.x;
					float y = p.y;
					int w = p.width;
					int h = p.height;
					((BigBigMaloMaloso)(Partida.bosses.get(p.boss_id))).spawnFirePuddle(x, y, w, h);
				}
		
				else if(o instanceof Packet_boss_tron) {
					((BigBigMaloMaloso)(Partida.bosses.get(((Packet_boss_tron)o).boss_id))).startTron();
				}
		
				else if(o instanceof Packet_boss_stomp) {
					Packet_boss_stomp p = (Packet_boss_stomp)o;
					((BigBigMaloMaloso)(Partida.bosses.get(p.boss_id))).stomp(p.x, p.y);
				}
		
				else if(o instanceof Packet_boss_stomp_stop) {
					((BigBigMaloMaloso)(Partida.bosses.get(((Packet_boss_stomp_stop)o).boss_id))).stomp_stop();
				}
				
				else if(o instanceof Packet_boss_init_casita) {
					Packet_boss_init_casita p = (Packet_boss_init_casita)o;
					((BigBigMaloMaloso)(Partida.bosses.get(p.boss_id))).startCasita(p.x, p.y);
				}
			}
		}
		// ------------
		
		else if(o instanceof Packet_set_pj_id) {
			Partida.pj_id = ((Packet_with_pj_id)o).pj_id;
		}
		
		else if(o instanceof String) {
			if(((String)o).equals("[MENU]")) {
				menu_window.setVisible(!menu_window.isVisible());
			}
			else if(((String)o).equals("[LOBBY]")) {
				goBackToLobby();
			}
		}
	}
    
    private void goBackToLobby() {
		net.gameplay = null;
		net.setLobbyListener();
		TheGAME.popScreen();
    }
    
    @Override
    public void dispose() {
    	Partida.sueloTexture.dispose();
		batch.dispose();
		if(Gdx.app.getType() == ApplicationType.Android) {
			inputControllerAndroid.shaper.dispose();
		}
    }
}
