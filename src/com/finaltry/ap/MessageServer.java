package com.finaltry.ap;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MessageServer {
	private Socket socket;
	private ServerSocket serverSocket;
	
	public MessageServer(){
		try{
			serverSocket = new ServerSocket(4300);
			System.out.println("Message Server Sucessfully started!");
			while(true){
				socket = serverSocket.accept();
				new Thread(new MessageServerThread(socket)).start();
//				new MessageServerThread(socket);
//				Thread t = new Thread(new MessageServerThread(socket));
//				t.start();
			}
		}catch(IOException e){
			System.err.println("Message-Server-Con: " + e.getMessage());
		}
	}

	public static void main(String args[]){
		new MessageServer();
	}
}
