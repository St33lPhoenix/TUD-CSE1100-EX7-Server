package tl.tudelft.gbot.chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import tl.tudelft.gbot.chat.server.api.IChatServer;
import tl.tudelft.gbot.chat.server.api.IInboundListener;
import tl.tudelft.gbot.chat.server.api.IUserConnection;
import tl.tudelft.gbot.chat.server.listener.InboundConnectionListenerThread;

public class ChatServer implements IChatServer {
	private final ServerSocket socket;
	private final IInboundListener listener;
	private final Collection<IUserConnection> connections = Collections.synchronizedSet(new HashSet<>());
	public ChatServer(ServerSocket socket) {
		if (socket == null) {
			throw new IllegalArgumentException("Socket cannot be null");
		}
		this.socket = socket;
		
		// Status message
		System.out.println("Server started.");
		
		// Start a new Thread to listen for incoming connections
		this.listener = new InboundConnectionListenerThread(this);
		listener.start();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final ServerSocket getSocket() {
		return socket;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final IInboundListener getInboundConnectionListener() {
		return listener;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Collection<IUserConnection> getUsers() {
		return connections;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final IUserConnection getUser(String name) {
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("Name cannot be null");
		}
		return connections.stream().filter(user -> user.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void close() throws IOException {
		for (IUserConnection connection : listener.getPendingConnections()) {
			connection.close();
		}
		for (IUserConnection connection : connections) {
			connection.close();
		}
		socket.close();
	}
}