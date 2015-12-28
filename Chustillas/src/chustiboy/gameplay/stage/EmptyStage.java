package chustiboy.gameplay.stage;

import com.badlogic.gdx.graphics.Color;

import chustiboy.gameplay.Muro;

public class EmptyStage extends Nivel {

	public final static Color color0 = new Color(0.3f, 0.4f, 0.4f, 1),
			  				  color1 = new Color(0.1f, 0.6f, 0.4f, 1);
	
	public EmptyStage() {
		width = 500;
		height = 500;
		
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
		
		suelo0 = color0;
		suelo1 = color1;
	}
}
