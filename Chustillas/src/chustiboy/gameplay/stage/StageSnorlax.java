package chustiboy.gameplay.stage;

import com.badlogic.gdx.graphics.Color;

import chustiboy.gameplay.Muro;
import chustiboy.gameplay.boss.snorlax.BigBigMaloMaloso;
import chustiboy.net.Network;

public class StageSnorlax extends Nivel{
	
	public final static Color color0 = new Color(0.5f, 0.25f, 0.25f, 1),
							  color1 = new Color(0.6f, 0.30f, 0.10f, 1);
	
	public StageSnorlax(Network net) {
		width = 1000;
		height = 800;
		
		spawn_x = width / 2;
		spawn_y =  height / 4;
		
		muros.add(new Muro(width/2,      0, width, 10));
		muros.add(new Muro(width/2, height, width, 10));
		muros.add(new Muro(      0,      0,    10, height + 10));
		muros.add(new Muro(  width,      0,    10, height + 10));
		
		BigBigMaloMaloso boss = new BigBigMaloMaloso(net);
		boss.setPosition(width/2, height/2);
		bosses.add(boss);
		
		suelo0 = color0;
		suelo1 = color1;
	}
}
