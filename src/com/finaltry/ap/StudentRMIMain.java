package com.finaltry.ap;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class StudentRMIMain {

	public static void main(String[] args) {
		try {
			StudentService obj = new StudentService();
			Registry r = LocateRegistry.createRegistry(4500);
			r.bind("Remote2", obj);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
