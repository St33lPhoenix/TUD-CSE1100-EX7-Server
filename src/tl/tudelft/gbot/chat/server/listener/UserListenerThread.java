package tl.tudelft.gbot.chat.server.listener;

import java.io.IOException;
import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tl.tudelft.gbot.chat.server.SynchronizedQueue;
import tl.tudelft.gbot.chat.server.api.IUserConnection;

public class UserListenerThread extends Thread {
	private static final Pattern PATTERN = Pattern.compile("^(@)([^ ]+)( +)(.+)$");
	private final IUserConnection connection;
	private final Queue<String> pending = new SynchronizedQueue<>(new ArrayBlockingQueue<>(1000));
	public UserListenerThread(IUserConnection connection) {
		super(connection.getSocket().getInetAddress().getHostAddress() + " Listener Thread");
		this.connection = connection;
	}
	@Override
	public final void run() {
		while (true) {

			// Check socket state
			if (!checkState()) {
				return;
			}

			// Flush all pending writes
			writeAll();
			
			// Read the next line
			String line;
			try {
				line = connection.getInput().readLine();
			} catch (IOException exception) {
				System.err.println("Could not read line: " + exception.getMessage());
				continue;
			}
			
			// If no name is set
			if (connection.getName() == null) {
				
				// Name cannot be empty
				if (line.isEmpty()) {
					pending.add("Name cannot be empty.");
					continue;
				}
				
				// System is reserved
				if (line.equalsIgnoreCase("System")) {
					pending.add("Illegal name.");
					continue;
				}
				
				// Name cannot be in use
				if (connection.getServer().getUser(line) != null) {
					pending.add("A user with that name already exists.");
					continue;
				}
				
				// Set the name
				connection.setName(line);
				System.out.println(connection.getSocket().getInetAddress().getHostName() + " set name to '" + line + "'");
				connection.getServer().getInboundConnectionListener().getPendingConnections().remove(connection);
				connection.getServer().getUsers().add(connection);
				
				// Send usage to user after connecting
				sendUsage();
				continue;
			}
			
			// Log
			System.out.println("[LOG] " + connection.getName() + "> " + line);
			
			// /users command
			if (line.equalsIgnoreCase("/users")) {
				pending.add(Arrays.toString(connection.getServer().getUsers().stream().map(user -> user.getName()).sorted(String.CASE_INSENSITIVE_ORDER).toArray()));
				continue;
			}
			
			// /receive command
			if (line.equalsIgnoreCase("/receive")) {
				
				// Iterate over every message in the queue and send it
				String msg;
				while ((msg = connection.getPendingWrites().poll()) != null) {
					pending.add(msg);
				}
				continue;
			}
			
			// If the input is a message to an other user
			Matcher matcher = PATTERN.matcher(line);
			if (matcher.matches()) {
				IUserConnection user = connection.getServer().getUser(matcher.group(2));
				
				// Unknown user
				if (user == null) {
					pending.add("Unknown user: '" + matcher.group(2) + '\n');
					continue;
				}
				
				// User is this user
				if (user == connection) {
					pending.add("You cannot message yourself.");
					continue;
				}
				
				// Add the input message to queue
				user.getPendingWrites().offer(connection.getName() + "> " + matcher.group(4));
				continue;
			}
			
			// Invalid input
			pending.add("System> Invalid input: '" + line + '\'');
			sendUsage();
		}
	}
	
	/**
	 * Write all pending messages to this user
	 */
	private final void writeAll() {
		
		// Don't do anything if there are no pending messages
		if (pending.isEmpty()) {
			return;
		}
		try {
			
			// Write all messages to the connection's OutputStream
			String message;
			while ((message = pending.poll()) != null) {
				connection.getOutput().append(message).append('\n');
			}
			
			// Flush the OutputStream
			connection.getOutput().flush();
		} catch (IOException exception) {
			System.err.println("Could not write line: " + exception.getMessage());
		}
	}
	/**
	 * Add all command usages to pending message queue
	 */
	private final void sendUsage() {
		pending.add("System> /users - get a list of all online users");
		pending.add("System> /receive - get all messages sent to you");
		pending.add("System> @user <message> - send a message to user");
	}
	/**
	 * Check if the connection is still usable
	 * @return true if usable
	 */
	private final boolean checkState() {
		try {
			connection.getSocket().getInputStream().read(new byte[0]);
			return true;
		} catch (IOException exception) {
			System.out.println("User " + (connection.getName() == null ? "" : connection.getName()) + " from " + connection.getSocket().getInetAddress().getHostAddress() + " disconnected.");
			connection.getServer().getUsers().remove(connection);
			try {
				connection.close();
			} catch (IOException exception2) {
				System.err.println("Could not close connection: " + exception2.getMessage());
			}
		}
		return false;
	}
}