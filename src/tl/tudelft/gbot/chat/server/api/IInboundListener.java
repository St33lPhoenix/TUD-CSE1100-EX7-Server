package tl.tudelft.gbot.chat.server.api;

import java.util.Collection;

public interface IInboundListener extends Runnable {
	/**
	 * Start the thread
	 * @see Thread#start()
	 */
	public void start();
	/**
	 * Get a synchronized set of all user connections that have not set their name yet (and this are not fully connected)
	 * @return the collection
	 */
	public Collection<IUserConnection> getPendingConnections();
}
