package tl.tudelft.gbot.chat.server.api;

import java.io.Closeable;
import java.net.ServerSocket;
import java.util.Collection;

public interface IChatServer extends Closeable {
	/**
	 * Get the server's socket
	 * @return the server's socket
	 */
	public ServerSocket getSocket();
	/**
	 * Get the listener thread for inbound connections
	 * @return the inbound listener
	 */
	public IInboundListener getInboundConnectionListener();
	/**
	 * Get all connected users (excluding users without username)
	 * @return all connected users
	 */
	public Collection<IUserConnection> getUsers();
	/**
	 * Get a user by name
	 * @param name target name
	 * @return the user or null if no user with such a name is known
	 */
	public IUserConnection getUser(String name);
}
