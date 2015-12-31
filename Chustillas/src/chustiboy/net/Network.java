package chustiboy.net;

import chustiboy.Gameplay_Screen;
import chustiboy.Lobby_Screen;
import chustiboy.Partida;
import chustiboy.net.packets.*;
import chustiboy.net.packets.boss.*;
import chustiboy.net.packets.boss.snorlax.*;

import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.kryonet.Listener;

public abstract class Network {

	protected static final int TCPport = 54000, UDPport = 54000;
	
	protected EndPoint endpoint;
	public Listener lobby_listener, gameplay_listener;
	public Partida partida;
	public Lobby_Screen lobby;
	public Gameplay_Screen gameplay; // init in Gameplay_Screen#constructor
	
	protected void registerPackets(EndPoint endpoint) {
		endpoint.getKryo().register(String.class);
		endpoint.getKryo().register(Packet_with_pj_id.class);
		endpoint.getKryo().register(Packet_set_pj_id.class);
		endpoint.getKryo().register(Packet_player.class);
		endpoint.getKryo().register(Packet_player_disconnected.class);
		endpoint.getKryo().register(Packet_color.class);
		endpoint.getKryo().register(Packet_ready.class);
		endpoint.getKryo().register(Packet_current_stage.class);
		endpoint.getKryo().register(Packet_position.class);
		endpoint.getKryo().register(Packet_shoot.class);
		endpoint.getKryo().register(Packet_pj_hit.class);
		endpoint.getKryo().register(Packet_boss_position.class);
		endpoint.getKryo().register(Packet_boss_attack.class);
		endpoint.getKryo().register(Packet_boss_dead.class);
		endpoint.getKryo().register(Packet_boss_spawn_fireball.class);
		endpoint.getKryo().register(Packet_boss_spawn_firePuddle.class);
		endpoint.getKryo().register(Packet_boss_tron.class);
		endpoint.getKryo().register(Packet_boss_stomp.class);
		endpoint.getKryo().register(Packet_boss_stomp_stop.class);
		endpoint.getKryo().register(Packet_boss_init_casita.class);
	}
	
	public abstract <T> void sendTCP(T packet);
	public abstract <T> void sendUDP(T packet);
	public abstract void setGameplayListener();
	public abstract void setLobbyListener();
	
	public void close() {
		endpoint.close();
	}
}
