package tl.tudelft.gbot.chat.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import tl.tudelft.gbot.chat.server.api.IChatServer;
import tl.tudelft.gbot.chat.server.api.IUserConnection;
import tl.tudelft.gbot.chat.server.listener.UserListenerThread;

public class UserConnection implements IUserConnection {
	private final IChatServer server;
	private final Socket socket;
	private final BufferedReader in;
	private final BufferedWriter out;
	private final Queue<String> pendingOut = new SynchronizedQueue<>(new ArrayBlockingQueue<>(1000));
	private String name = null;
	public UserConnection(IChatServer server, Socket socket) throws IOException {
		if (server == null) {
			throw new IllegalArgumentException("Server cannot be null");
		}
		if (socket == null) {
			throw new IllegalArgumentException("Socket cannot be null");
		}
		this.server = server;
		this.socket = socket;
		
		// Create a BufferedReader from InputStream with UTF-8 encoding
		this.in = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8")));
		
		// Create a BufferedWriter from OutputStream with UTF-8 encoding
		this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), Charset.forName("UTF-8")));
		
		// Start the Thread listening for user input
		new UserListenerThread(this).start();
		
		// Status message
		System.out.println("User connected from " + socket.getInetAddress().getHostName());
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final IChatServer getServer() {
		return server;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Socket getSocket() {
		return socket;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final BufferedReader getInput() {
		return in;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final BufferedWriter getOutput() {
		return out;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Queue<String> getPendingWrites() {
		return pendingOut;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String getName() {
		return name;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setName(String name) {
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("Name cannot be null");
		}
		if (this.name != null) {
			throw new IllegalStateException("Name already set");
		}
		this.name = name;
	}
	@Override
	public final void close() throws IOException {
		in.close();
		out.close();
		socket.close();
	}
}
