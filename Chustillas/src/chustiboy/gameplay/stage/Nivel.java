package chustiboy.gameplay.stage;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;

import chustiboy.gameplay.Boss;
import chustiboy.gameplay.Muro;

public class Nivel {
	public Array<Muro> muros;
	public Array<Boss> bosses;
	public int width, height;
	public float spawn_x, spawn_y;
	public Color suelo0, suelo1;
	
	Nivel() {
		muros = new Array<>();
		bosses = new Array<>();
	}
}
