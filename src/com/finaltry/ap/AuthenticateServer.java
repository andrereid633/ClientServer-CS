package com.finaltry.ap;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class AuthenticateServer { 
	private ServerSocket serverSocket;
	private Socket socket;
	public Map<String, ObjectOutputStream> userMap = new HashMap<String, ObjectOutputStream>();

	public AuthenticateServer() {
		try {
			serverSocket = new ServerSocket(4200);
			System.out.println("Authenticate Server Sucessfully Statred!");
			while (true) {
				socket = serverSocket.accept();
				new Thread(new AuthenticateServerThread(socket)).start();
			}
		} catch (IOException e) {
			System.err.println("ServerClass-Constructor: " + e.getMessage());
		}
	}

	public static void main(String args[]) {
		new AuthenticateServer();
	}

}
