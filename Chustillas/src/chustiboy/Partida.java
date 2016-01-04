package chustiboy;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisTable;

import chustiboy.gameplay.Boss;
import chustiboy.gameplay.Chustilla;
import chustiboy.gameplay.Muro;

public class Partida {
	public static byte pj_id;
	public static Array<Chustilla> chustillas = new Array<>();
	public static Array<Boss> bosses = new Array<>();
	public static Array<Muro> muros = new Array<>();
	public static int stage_width, stage_height;
	public static Texture sueloTexture;
	
	public static void setChustillas(VisTable lobby_table) {
		//clear
		Partida.chustillas.clear();
		
		//set
		for(Actor actor : lobby_table.getChildren()) {
			Player player = (Player)actor;
			player.setReady(false);

			Chustilla chustilla = new Chustilla();
			chustilla.name = player.getName();
			chustilla.setColor(player.r, player.g, player.b);
			Partida.chustillas.add(chustilla);
		}
	}
}
