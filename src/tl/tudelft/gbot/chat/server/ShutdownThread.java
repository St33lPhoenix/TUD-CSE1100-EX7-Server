package tl.tudelft.gbot.chat.server;

import java.io.IOException;

import tl.tudelft.gbot.chat.server.api.IChatServer;

public class ShutdownThread extends Thread {
	private final IChatServer server;
	/**
	 * Thread used as shutdown hook to call {@link IChatServer#close()}
	 * @param server server to close
	 * @see Runtime#addShutdownHook(Thread)
	 */
	public ShutdownThread(IChatServer server) {
		super("Server closer thread");
		if (server == null) {
			throw new IllegalArgumentException("Server cannot be null");
		}
		this.server = server;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void run() {
		try {
			server.close();
		} catch (IOException exception) {
			System.err.println("Could not close server properly: " + exception.getMessage());
		}
	}
}
