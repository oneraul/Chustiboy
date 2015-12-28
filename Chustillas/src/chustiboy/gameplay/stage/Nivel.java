package chustiboy.gameplay.stage;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;

import chustiboy.gameplay.Muro;
import chustiboy.gameplay.boss.Boss;

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
