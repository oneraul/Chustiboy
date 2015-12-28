package chustiboy;

import com.badlogic.gdx.utils.Array;

import chustiboy.gameplay.Chustilla;
import chustiboy.net.Network;
import chustiboy.net.packets.*;

public class InputController {

	private Chustilla pj;
	private Network net;
	private Array<Object> messagesQueue;
	
	InputController(Network net, Array<Object> messagesQueue) {
		this.pj = Partida.chustillas.get(Partida.pj_id);
		this.net = net;
		this.messagesQueue = messagesQueue;
	}
	
	boolean isPjDead() {
		return pj.isDead();
	}
	
	void W(boolean moving) {
		pj.W = moving;
	}
	
	void A(boolean moving) {
		pj.A = moving;
	}
	
	void S(boolean moving) {
		pj.S = moving;
	}
	
	void D(boolean moving) {
		pj.D = moving;
	}
	
	void notMoving() {
		pj.W = pj.A = pj.S = pj.D = false;
	}
	
	void shoot() {
		pj.shoot();

		Packet_shoot p = new Packet_shoot();
		p.pj_id = Partida.pj_id;
		p.pos_x = pj.getPosition().x;
		p.pos_y = pj.getPosition().y;
		p.dir_x = pj.dir.x;
		p.dir_y = pj.dir.y;

		net.sendTCP(p);
	}
	
	void menu() {
		EventSystem.produceMessage("[MENU]", messagesQueue);
	}
}
