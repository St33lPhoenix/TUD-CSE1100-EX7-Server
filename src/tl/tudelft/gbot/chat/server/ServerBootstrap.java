package tl.tudelft.gbot.chat.server;

import java.io.IOException;
import java.net.ServerSocket;

import tl.tudelft.gbot.chat.server.api.IChatServer;

public class ServerBootstrap {
	private static final int PORT = 50000;
	/**
	 * Application entry point
	 * @param args first argument is port. This is optional
	 */
	public static final void main(String[] args) {
		
		// Check Java version
		if (Double.parseDouble(System.getProperty("java.class.version")) < 52D) {
			System.err.println("This application requires Java 8 or higher");
			return;
		}
		
		// Get the port from command line arguments or use default if unavailable
		int port = PORT;
		if (args.length > 0) {
			try {
				port = Integer.parseInt(args[0]);
			} catch (NumberFormatException exception) {
				// Nothing
			}
		}
		
		// Try to create a new server
		System.out.println("Attempting listen on port " + port);
		IChatServer server;
		try {
			server = new ChatServer(new ServerSocket(port));
		} catch (IOException exception) {
			System.err.println("Could not listen to port " + port + ": " + exception.getMessage());
			return;
		}
		
		// If server creation is successful, we create a new ShutdownThread for it
		// This is to properly close all I/O, because the assignment just ignores it
		Runtime.getRuntime().addShutdownHook(new ShutdownThread(server));
	}
}
