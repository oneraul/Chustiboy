package chustiboy.gameplay.stage;

import com.badlogic.gdx.graphics.Color;

import chustiboy.Assets;
import chustiboy.Gameplay_Screen;
import chustiboy.Partida;
import chustiboy.gameplay.Chustilla;
import chustiboy.gameplay.Muro;
import chustiboy.gameplay.boss.snorlax.BigBigMaloMaloso;
import chustiboy.net.Network;

public class StageSnorlax extends Gameplay_Screen {
	
	public final static Color color0 = new Color(0.5f, 0.25f, 0.25f, 1),
							  color1 = new Color(0.6f, 0.30f, 0.10f, 1);
	
	private Color postColor;
	
	public StageSnorlax(Network net) {
		super(net);
		
		width = 1000;
		height = 800;
		
		Partida.stage_width = width;
		Partida.stage_height = height;
		
		spawn_x = width / 2;
		spawn_y =  height / 4;
		
		muros.add(new Muro(width/2,      0, width, 20));
		muros.add(new Muro(width/2, height, width, 20));
		muros.add(new Muro(      0,      0,    20, height + 10));
		muros.add(new Muro(  width,      0,    20, height + 10));
		
		BigBigMaloMaloso boss = new BigBigMaloMaloso(net);
		boss.setPosition(width/2, height/2);
		bosses.add(boss);
		postColor = boss.stageColor;
		
		sueloTexture = Assets.suelo(new Color(color0),new Color(color1));
		
		init();
		for(Chustilla chustilla : chustillas) {
			chustilla.setPosition(spawn_x, spawn_y);
		}
	}
	
	@Override
	protected void postProcessing() {
		batch.begin();
		batch.setColor(postColor);
		batch.draw(fboRegion, 0, 0);
		batch.end();
	}
}
