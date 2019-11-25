package tl.tudelft.gbot.chat.server.api;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.net.Socket;
import java.util.Queue;

public interface IUserConnection extends Closeable {
	/**
	 * Get the server this user is connected to
	 * @return the user's server
	 */
	public IChatServer getServer();
	/**
	 * Get the socket this user is connected with
	 * @return the user's socket
	 */
	public Socket getSocket();
	/**
	 * Get the user's input reader
	 * @return the user's input reader
	 */
	public BufferedReader getInput();
	/**
	 * Get the user's output writer
	 * @return the user's output writer
	 */
	public BufferedWriter getOutput();
	/**
	 * Get a queue holding all pending messages to be sent
	 * @return all pending messages
	 */
	public Queue<String> getPendingWrites();
	/**
	 * Get the user's name
	 * @return the user's name
	 */
	public String getName();
	/**
	 * Set the user's name
	 * @param name name to use
	 */
	public void setName(String name);
}
