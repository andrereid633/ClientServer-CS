package com.finaltry.ap;

import java.io.Serializable;

public class Message implements Serializable {

	private static final long serialVersionUID = 1L;

	private String sender, receiver, message, type = "";
	
	// The different types of message sent by the Client
		// WHOISIN to receive the list of the users connected
		// MESSAGE an ordinary message
		// LOGOUT to disconnect from the Server
		public static final int WHOISIN = 0;

		public static final int MESSAGE = 1;

		public static final int LOGOUT = 2;
	
	public Message(String msg){
		this.message = msg; 
	}

	public Message(String sender, String message){
		this.message = message;
		this.sender = sender;
		type = "";
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getMsg() {
		return message;
	}

	public void setMsg(String msg) {
		this.message = msg;
	}

	@Override
	public String toString() {
		return String.format("Message [sender=%s, receiver=%s, msg=%s]",
				sender, receiver, message);
	}
}
