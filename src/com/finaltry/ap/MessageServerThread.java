package com.finaltry.ap;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class MessageServerThread implements Runnable {
	private ObjectInputStream is;
	private ObjectOutputStream os;
	private Socket socket;
	//private AuthenticateServerThread serverThread = new AuthenticateServerThread();
	private ArrayList<String> retrieveUsersList;
	

	public MessageServerThread(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		System.out.println(AuthenticateServerThread.getUsersList());
		String readStream = "";
		
		try {
			os = new ObjectOutputStream(socket.getOutputStream());
			is = new ObjectInputStream(socket.getInputStream());

			while (true) {
				System.out.println("USERS LOGED-IN: " + AuthenticateServerThread.getUsersList());
				readStream = (String) is.readObject();
				System.out.println("I got this from the client: " + readStream);
				os.writeObject("server: " + readStream);
			}
		} catch (IOException e) {
			System.err.println("MessgaeServerThread: " + e.getMessage());
		} catch (ClassNotFoundException e) {
			System.err.println("MessageServerThread: " + e.getMessage());
		}
	}
	
	public synchronized void sendMessageToAll(String message){		
		
		System.out.println("RetrieveUSERListSize: " + retrieveUsersList.size());
		//Loops through all the users that are connected
//		for(int i=0; i < retrieveUsersList.size(); i++){
//		}
	}
}
