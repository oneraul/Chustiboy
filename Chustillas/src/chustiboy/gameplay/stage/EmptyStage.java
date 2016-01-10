package chustiboy.gameplay.stage;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

import chustiboy.Assets;
import chustiboy.Partida;
import chustiboy.ColorGraded_Gameplay_Screen;
import chustiboy.gameplay.Chustilla;
import chustiboy.gameplay.Muro;
import chustiboy.net.Network;

public class EmptyStage extends ColorGraded_Gameplay_Screen {

	public final static Color color0 = new Color(0.3f, 0.4f, 0.4f, 1),
							  color1 = new Color(0.1f, 0.6f, 0.4f, 1);

	public EmptyStage(Network net) {
		super(net);
		setLUT(new Texture("assets/lut-desaturate.jpg"));

		int width = 500;
		int height = 500;

		Partida.stage_width = width;
		Partida.stage_height = height;

		spawn_x = width / 2;
		spawn_y =  height / 2;

		muros.add(new Muro(width/2,      0, width, 10));
		muros.add(new Muro(width/2, height, width, 10));
		muros.add(new Muro(      0,      0,    10, height + 10));
		muros.add(new Muro(  width,      0,    10, height + 10));

		muros.add(new Muro(width*0.25f, height*0.25f, 20, 20));
		muros.add(new Muro(width*0.75f, height*0.25f, 20, 20));
		muros.add(new Muro(width*0.25f, height*0.75f, 20, 20));
		muros.add(new Muro(width*0.75f, height*0.75f, 20, 20));

		sueloTexture = Assets.suelo(new Color(color0), new Color(color1));

		init();
		for(Chustilla chustilla : chustillas) {
			chustilla.setPosition(spawn_x, spawn_y);
		}
	}
}

