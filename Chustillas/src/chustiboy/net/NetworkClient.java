package chustiboy.net;

import java.io.IOException;

import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import chustiboy.*;
import chustiboy.net.packets.Packet_player;

public class NetworkClient extends Network {
	
	private Client client;
	
	public NetworkClient(final Lobby_Screen lobby, String ip) throws IOException {
		
		this.lobby = lobby;
		
		client = new Client();
		endpoint = client;
		registerPackets(endpoint);
		
		lobby_listener = new Listener() {
			@Override
			public void received(Connection connection, Object o) {
				EventSystem.produceMessage(o, lobby.messagesQueue);
			}
			
			@Override
			public void disconnected(Connection connection) {
				EventSystem.produceMessage("[DISCONNECTED]", lobby.messagesQueue);
			}
			
			@Override
			public void connected(Connection connection) {
				Packet_player p = new Packet_player();
				p.name = Assets.preferences.getString("name");
				p.playerID = lobby.getPlayerID();
				p.r = MathUtils.random();
				p.g = MathUtils.random();
				p.b = MathUtils.random();
				client.sendTCP(p);
			}
		};
		
		gameplay_listener = new Listener() {
			@Override
			public void received(Connection connection, Object o) {
				EventSystem.produceMessage(o, gameplay.messagesQueue);
			}
		};
		
		client.addListener(lobby_listener);
		client.start();
		
		client.connect(5000, ip, TCPport, UDPport);
	}

	@Override
	public <T> void sendTCP(T packet) {
		client.sendTCP(packet);
	}

	@Override
	public <T> void sendUDP(T packet) {
		client.sendUDP(packet);
	}
	
	@Override
	public void setGameplayListener() {
		client.removeListener(lobby_listener);
		client.addListener(gameplay_listener);
	}
	
	@Override
	public void setLobbyListener() {
		client.removeListener(gameplay_listener);
		client.addListener(lobby_listener);
	}
}
