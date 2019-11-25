package tl.tudelft.gbot.chat.server.listener;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import tl.tudelft.gbot.chat.server.UserConnection;
import tl.tudelft.gbot.chat.server.api.IChatServer;
import tl.tudelft.gbot.chat.server.api.IInboundListener;
import tl.tudelft.gbot.chat.server.api.IUserConnection;

public class InboundConnectionListenerThread extends Thread implements IInboundListener {
	private final IChatServer server;
	private final Collection<IUserConnection> pending = Collections.synchronizedSet(new HashSet<>());
	public InboundConnectionListenerThread(IChatServer server) {
		super("Inbound connection listener thread");
		if (server == null) {
			throw new IllegalArgumentException("Server cannot be null");
		}
		this.server = server;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Collection<IUserConnection> getPendingConnections() {
		return pending;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void run() {
		while (true) {

			// Try to create a new user connection
			try {
				pending.add(new UserConnection(server, server.getSocket().accept()));
			} catch (IOException exception) {
				System.err.println("Could not establish connection: " + exception.getMessage());
			}
		}
	}
}
