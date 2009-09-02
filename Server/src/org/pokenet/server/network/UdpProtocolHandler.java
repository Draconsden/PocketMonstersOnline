package org.pokenet.server.network;

import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.HashMap;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioDatagramConnector;
import org.pokenet.server.backend.entity.PlayerChar;
import org.pokenet.server.backend.entity.Positionable.Direction;
import org.pokenet.server.network.message.PokenetMessage;

/**
 * Protocol Handler for UDP connections
 * @author tom
 *
 */
public class UdpProtocolHandler extends IoHandlerAdapter {
	private static HashMap<Integer, PlayerChar> m_playerList;
	
	/**
	 * Default Constructor
	 */
	public UdpProtocolHandler() {
		m_playerList = new HashMap<Integer, PlayerChar>();
	}
	
	/**
	 * Called when an exception is caught
	 */
	public void exceptionCaught(IoSession session, Throwable t) throws Exception {
		t.printStackTrace();
	}
	
	@Override 
	public void messageReceived(IoSession session, Object o) throws Exception { 
		String message = (String) o;
		String pid = "";
		PlayerChar p = null;
		/* If the message has no size, just return */
		if(message.length() < 1)
			return;
		switch(message.charAt(0)) {
		case 'U':
			/* Move Up */
			pid = message.substring(6);
			p = m_playerList.get(Integer.parseInt(pid));
			if(p != null) {
				if(p.getUdpCode().compareTo(message.substring(1, 6)) == 0) {
					if(!p.isBattling() && !p.isShopping()) {
						p.setNextMovement(Direction.Up);
						p.setUdpSession(session.getRemoteAddress());
					}
				}
			}
			break;
		case 'D':
			/* Move Down */
			pid = message.substring(6);
			p = m_playerList.get(Integer.parseInt(pid));
			if(p != null) {
				if(p.getUdpCode().compareTo(message.substring(1, 6)) == 0) {
					if(!p.isBattling() && !p.isShopping()) {
						p.setNextMovement(Direction.Down);
						p.setUdpSession(session.getRemoteAddress());
					}
				}
			}
			break;
		case 'L':
			/* Move Left */
			pid = message.substring(6);
			p = m_playerList.get(Integer.parseInt(pid));
			if(p != null) {
				if(p.getUdpCode().compareTo(message.substring(1, 6)) == 0) {
					if(!p.isBattling() && !p.isShopping()) {
						p.setNextMovement(Direction.Left);
						p.setUdpSession(session.getRemoteAddress());
					}
				}
			}
			break;
		case 'R':
			/* Move Right */
			pid = message.substring(6);
			p = m_playerList.get(Integer.parseInt(pid));
			if(p != null) {
				if(p.getUdpCode().compareTo(message.substring(1, 6)) == 0) {
					if(!p.isBattling() && !p.isShopping()) {
						p.setNextMovement(Direction.Right);
						p.setUdpSession(session.getRemoteAddress());
					}
				}
			}
			break;
		}
	} 
	
	/**
	 * Adds a player to the udp player list
	 * @param p
	 */
	public static void addPlayer(PlayerChar p) {
		synchronized(m_playerList) {
			m_playerList.put(p.getId(), p);
		}
	}
	
	/**
	 * Removes a player from the udp player list
	 * @param p
	 */
	public static void removePlayer(PlayerChar p) {
		synchronized(m_playerList) {
			m_playerList.remove(p.getId());
		}
	}
	
	public static void writeMessage(SocketAddress s, PokenetMessage m) {
		try {
			NioDatagramConnector connector = new NioDatagramConnector();
			ConnectFuture c = connector.connect(s);
			c.getSession().write(m.getMessage());
		} catch (Exception e) {}
	}
}