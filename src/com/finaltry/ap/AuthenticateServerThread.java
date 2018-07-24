package com.finaltry.ap;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;

public class AuthenticateServerThread implements Runnable {

	private ObjectOutputStream os;
	private ObjectInputStream is;
	private Socket socket;

	public static ArrayList<String> usersList = new ArrayList<String>();
	private String client;
	private String readStream, username, password;
	private Queries queries = new Queries();
	private boolean authenticateUser;

	public AuthenticateServerThread(Socket socket) {
		this.socket = socket;
	}
	
	public AuthenticateServerThread(){//ArrayList<String> usersList) {
		//usersList;
		System.out.println("User-IN-List: " + getUsersList());
	}

	private void getStream(){
		try {
			os = new ObjectOutputStream(socket.getOutputStream());
			is = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			System.err.println("GeT-Stream: " + e.getMessage());
		}
		
	}
	
	@Override
	public void run() {
		try {
			getStream();

			System.out.println("Connection successfully made to the server!");

			while (true) {
				readStream = (String) is.readObject();
				System.out.println(readStream);
				
				authenticateEmployee(readStream);

//				// determine who if an employee is signing in
//				if (readStream.equalsIgnoreCase("employee")) {
//					username = (String) is.readObject();
//					password = (String) is.readObject();
//					authenticateUser = queries.signInEmployeeUser(username,	password);
//					System.out.println("Employee Authentication: " + authenticateUser);
//
//					if (authenticateUser) {// adds user to the list
//						client = username;
//						System.out.println(client + " has just Connected ");
//						usersList.add(client);// adds the client to the list
//						this.setUsersList(usersList);
//						os.writeObject(authenticateUser);
//					}
//				}// ends main if
				
				//determine if a student user is signing in
				if(readStream.equalsIgnoreCase("students")){
					username = (String) is.readObject();
					password = (String) is.readObject();
					authenticateUser = queries.signInStudentUser(username, password);
					
					System.out.println("Student Authentication: " + authenticateUser);
					
					if(authenticateUser){
						client = username;
						System.out.println("Student " + client + " has just connected");
						usersList.add(client);
						
						//ADDS THE USER TO THE LIST SO THAT THE MESSAGE-THREAD CAN GET IT
						this.setUsersList(usersList);
						
						os.writeObject(authenticateUser);
					}
				}//end if
				
				System.out.println("AuthenticateThread#OfUSers: " + getUsersList());
			}// end while
		} catch (IOException e) {
			System.err.println("ThreadClass-RUN: " + e.getMessage());
		} catch (ClassNotFoundException e) {
			System.err.println("ThreadClass-RUN1: " + e.getMessage());
		}
	}//end run method

	
	private void authenticateEmployee(String readStream){
		try {
			if (readStream.equalsIgnoreCase("employee")) {
				username = (String) is.readObject();
				password = (String) is.readObject();
				authenticateUser = queries.signInEmployeeUser(username,	password);
				System.out.println("Employee Authentication: " + authenticateUser);

				if (authenticateUser) {// adds user to the list
					client = username;
					System.out.println(client + " has just Connected ");
					usersList.add(client);// adds the client to the list
					this.setUsersList(usersList);
					os.writeObject(authenticateUser);
				}
			}// ends main if
		} catch (IOException e) {
			System.err.println("AuthenticateServerThread-AuthenticateEmployee: " + e.getMessage());
		} catch (ClassNotFoundException e) {
			System.err.println("AuthenticateServerThread-AuthenticateEmployee: " + e.getMessage());
		}
		
		
		
	}
	public static ArrayList<String> getUsersList() {
		return usersList;
	}

	public void setUsersList(ArrayList<String> usersList) {
		this.usersList = usersList;
	}
}
