package chustiboy.net;

import java.io.IOException;

import chustiboy.Assets;
import chustiboy.EventSystem;
import chustiboy.Lobby_Screen;
import chustiboy.Partida;
import chustiboy.Player;
import chustiboy.net.packets.Packet_color;
import chustiboy.net.packets.Packet_current_stage;
import chustiboy.net.packets.Packet_player;
import chustiboy.net.packets.Packet_player_disconnected;
import chustiboy.net.packets.Packet_position;
import chustiboy.net.packets.Packet_ready;
import chustiboy.net.packets.Packet_set_pj_id;
import chustiboy.net.packets.Packet_shoot;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class NetworkHost extends Network {
	
	Server server;
	private short number_of_players, ready_players;
	
	public NetworkHost(final Lobby_Screen lobby) throws IOException {
		super(lobby);
		
		server = new Server();
		endpoint = server;
		registerPackets(endpoint);
		
		lobby_listener = new Listener() {
			@Override
			public void received(Connection connection, Object o) {
				
				if(o instanceof Packet_color) {
					Packet_color p = (Packet_color)o;
					sendTCP(p);
				}
				
				else if(o instanceof Packet_ready) {
					Packet_ready p = (Packet_ready)o;
					sendTCP(p);
					
					if(p.ready) ready_players++;
					else ready_players--;
				
					check_ready();
				}
				
				else if(o instanceof Packet_player) {
					Packet_player p = (Packet_player)o;
					p.connectionID = (byte) connection.getID();
					sendTCP(p);
					
					number_of_players++;
				}
				
				EventSystem.produceMessage(o, lobby.messagesQueue);
			}
			
			@Override
			public void connected(Connection connection) {
				// nuevo jugador!
				// - ponerle al dia de lo que hay
				
				for(Actor actor : lobby.players_table.getChildren()) {
					Player player = (Player)actor;
					
					Packet_player p = new Packet_player();
					p.name = player.getName();
					p.playerID = player.playerID;
					p.connectionID = player.connectionID;
					p.r = player.r;
					p.g = player.g;
					p.b = player.b;
					p.ready = player.isReady();
					server.sendToTCP(connection.getID(), p);
				}
				
				Packet_current_stage q = new Packet_current_stage();
				q.stage = lobby.currentStage;
				sendTCP(q);
			}
			
			@Override
			public void disconnected(Connection connection) {
				
				number_of_players--;
				if(lobby.players.get((byte)connection.getID()).isReady()) {
					ready_players--;
				}
				
				Packet_player_disconnected p = new Packet_player_disconnected();
				p.connection_id = (byte)connection.getID();
				server.sendToAllTCP(p);
				EventSystem.produceMessage(p, lobby.messagesQueue);
			}
		};
		
		gameplay_listener = new Listener() {
			@Override
			public void received(Connection connection, Object o) {
				if(o instanceof Packet_position || o instanceof Packet_shoot) {
					
					EventSystem.produceMessage(o, gameplay.messagesQueue);

					if(o instanceof Packet_position)
						server.sendToAllExceptUDP(connection.getID(), (Packet_position)o);
	
					if(o instanceof Packet_shoot) 
						server.sendToAllExceptTCP(connection.getID(), (Packet_shoot)o);
				}
			}
		};
		
		server.addListener(lobby_listener);
		server.start();
		server.bind(TCPport, UDPport);
	}

	@Override
	public <T> void sendTCP(T packet) {
		server.sendToAllTCP(packet);
	}

	@Override
	public <T> void sendUDP(T packet) {
		server.sendToAllUDP(packet);
	}

	@Override
	public void setGameplayListener() {
		server.removeListener(lobby_listener);
		server.addListener(gameplay_listener);
	}
	
	@Override
	public void setLobbyListener() {
		server.removeListener(gameplay_listener);
		server.addListener(lobby_listener);
	}
	
	public void addMyPlayer() {
		Packet_player p = new Packet_player();
		p.name = Assets.preferences.getString("name");
		p.playerID = lobby.getPlayerID();
		p.connectionID = 0;
		p.r = MathUtils.random();
		p.g = MathUtils.random();
		p.b = MathUtils.random();
		
		EventSystem.produceMessage(p, lobby.messagesQueue);
		
		number_of_players = 1;
	}
	
	public void check_ready() {
		if(ready_players == number_of_players-1
		&& lobby.players.get(lobby.connectionID).isReady()) {
			ready_players = 0;
			
			// set pj_ids
			Partida.pj_id = 0;
			byte i = 0;
			for(Byte connectionID : lobby.players.keySet()) {
				if(i != 0) {
					Packet_set_pj_id q = new Packet_set_pj_id();
					q.pj_id = i;
					server.sendToTCP(connectionID, q);
				}
				i++;
			}
			
			sendTCP("[START]");
		 	EventSystem.produceMessage("[START]", lobby.messagesQueue);
		}
	}
}
