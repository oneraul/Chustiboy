package chustiboy;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisTable;

import chustiboy.gameplay.Boss;
import chustiboy.gameplay.Chustilla;
import chustiboy.gameplay.Muro;
import chustiboy.gameplay.stage.Nivel;

public class Partida {
	public static Array<Chustilla> chustillas = new Array<>();
	public static Array<Boss> bosses = new Array<>();
	public static Array<Muro> muros = new Array<>();
	public static byte pj_id;
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
	
	public static void setLevel(Nivel level) {
		//clear
		bosses.clear();
		muros.clear();
		
		//set
		bosses.addAll(level.bosses);
		muros.addAll(level.muros);
		stage_width = level.width;
		stage_height = level.height;
		
		for(short i = 0; i < bosses.size; i++) {
			bosses.get(i).id = i;
		}
		
		for(Chustilla chustilla : chustillas) {
			chustilla.setPosition(level.spawn_x, level.spawn_y);
		}
		
		sueloTexture = Assets.suelo(level.suelo0, level.suelo1);
	}
}
